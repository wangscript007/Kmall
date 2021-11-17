package xyz.klenkiven.kmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.CategoryDao;
import xyz.klenkiven.kmall.product.entity.CategoryEntity;
import xyz.klenkiven.kmall.product.service.CategoryBrandRelationService;
import xyz.klenkiven.kmall.product.service.CategoryService;
import xyz.klenkiven.kmall.product.vo.Catalog2VO;


@Service("categoryService")
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    private final CategoryBrandRelationService categoryBrandRelationService;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        return categoryEntities.stream()
                .filter(categoryEntity -> categoryEntity.getCatLevel() == 1)
                .peek(categoryEntity ->
                        categoryEntity.setChildren(getChildrenCategory(categoryEntity, categoryEntities)))
                // 比较排序
                .sorted((c1, c2) -> c1.getSort() - c2.getSort())
                .collect(Collectors.toList());
    }

    @Override
    public void removeBatch(Long[] catIds) {
        // TODO 检测当前ID是否正在被引用
        List<Long> catIdList = new ArrayList<>(Arrays.asList(catIds));
        baseMapper.deleteBatchIds(catIdList);
    }

    @Override
    public void updateDetailById(CategoryEntity category) {
        if (!StringUtils.isBlank(category.getName())) {
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
            // TODO Other relationship
        }
        this.updateById(category);
    }

    public List<Long> getCatalogPath(Long catalogId) {
        List<Long> result = new ArrayList<>();
        CategoryEntity catalog;
        do {
            catalog = this.getById(catalogId);
            if (catalog != null) {
                result.add(catalog.getCatId());
                catalogId = catalog.getParentCid();
            }
        } while (catalogId != 0);
        Collections.reverse(result);
        return result;
    }

    @Cacheable({"catalog"}) // this is cacheable
    @Override
    public List<CategoryEntity> listCategoryByLevel(Integer level) {
        return baseMapper.selectList(
                new QueryWrapper<CategoryEntity>()
                        .eq("cat_level", level)
                        .eq("show_status", 1)
        );
    }

    @Override
    public Map<Long, List<Catalog2VO>> getCatalogJson() {
        // Cache Penetration: Set Expire Time
        // Cache Breakdown: Lock
        // Cache Avalanche: Set Expire Time Random

        String catalogJson = stringRedisTemplate.opsForValue().get("getCatalogJson");
        Map<Long, List<Catalog2VO>> catalogJsonFromDb;
        if (StringUtils.isEmpty(catalogJson)) {

            // Get Catalog From Database
            System.out.println("Get Data From DB");
            catalogJsonFromDb = getCatalogJsonFromDb();

            // In concurrency situation, Get data from DB and Save to Redis
            catalogJson = JSON.toJSONString(catalogJsonFromDb);
            // TODO Set Expire Time Properly
             stringRedisTemplate.opsForValue().set("getCatalogJson", catalogJson, 1, TimeUnit.DAYS);
        } else {
            System.out.println("Get Data From Redis");
            catalogJsonFromDb = JSON.parseObject(catalogJson, new TypeReference<>() {  });
        }

        // new TypeReference<Map<Long, List<Catalog2VO>>>(){}
        return catalogJsonFromDb;
    }


    /**
     * Get Catalog Data From Database with Distribute Lock
     *
     * @return Data Map
     */
    @Override
    public Map<Long, List<Catalog2VO>> getCatalogJsonFromDbWithDistributeLock() {
        String uuid = UUID.randomUUID().toString();
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        // GET LOCK: Atomic Operation Set Lock
        Boolean lock = ops.setIfAbsent("lock", uuid, 30, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(lock)) {
            System.out.println("Success Get Distribute LOCK");
            Map<Long, List<Catalog2VO>> catalogJsonFromDb;
            try {
                // DO BUSINESS LOGIC
                catalogJsonFromDb = getCatalogJson();

            } finally {
                // RELEASE LOCK: Atomic Operation Delete Lock
                // MUST BE EXECUTED
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] " +
                        "then " +
                        "   return redis.call('del',KEYS[1]) " +
                        "else " +
                        "   return 0 " +
                        "end";
                stringRedisTemplate.execute(
                        new DefaultRedisScript<>(script, Long.class),
                        List.of("lock"),
                        uuid
                );
                System.out.println("Delete Lock By: " + uuid);
            }
            return catalogJsonFromDb;
        } else {
            try {
                System.out.println("Cannot GET Distribute LOCK...Waiting...");
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            // Spinlock
            System.out.println("Retry GET Distribute LOCK");
            return getCatalogJsonFromDbWithDistributeLock();
        }
    }

    /**
     * Get Catalog Data From Database
     *
     * @return Data Map
     */
    private Map<Long, List<Catalog2VO>> getCatalogJsonFromDb() {
        List<CategoryEntity> categoryEntities = listCategoryByLevel(1);
        if (categoryEntities == null || categoryEntities.size() == 0) {
            return new HashMap<>();
        }

        List<CategoryEntity> category2Level = listCategoryByLevel(2);
        List<CategoryEntity> category3Level = listCategoryByLevel(3);
        return categoryEntities.stream()
                .collect(Collectors.toMap(CategoryEntity::getCatId, v -> {
                    if (category2Level == null) {
                        return new ArrayList<>();
                    }
                    // Get Category Level 2
                    return category2Level.stream()
                            .filter(c2l -> c2l.getParentCid().equals(v.getCatId()))
                            .map(c2l -> {
                                Catalog2VO catalog2VO = new Catalog2VO();
                                catalog2VO.setCatalog1Id(v.getCatId());
                                catalog2VO.setId(c2l.getCatId());
                                catalog2VO.setName(c2l.getName());

                                // Get Category Level 3
                                if (category3Level == null) {
                                    catalog2VO.setCatalog3List(new ArrayList<>());
                                } else {
                                    List<Catalog2VO.Catalog3VO> catalog3VOList = category3Level.stream()
                                            .filter(c3l -> c3l.getParentCid().equals(c2l.getCatId()))
                                            .map(c3l -> {
                                                Catalog2VO.Catalog3VO catalog3VO = new Catalog2VO.Catalog3VO();
                                                catalog3VO.setCatalog2Id(c2l.getCatId());
                                                catalog3VO.setId(c3l.getCatId());
                                                catalog3VO.setName(c3l.getName());
                                                return catalog3VO;
                                            }).collect(Collectors.toList());
                                    catalog2VO.setCatalog3List(catalog3VOList);
                                }
                                return catalog2VO;
                            })
                            .collect(Collectors.toList());
                }));
    }

    /**
     * 获取此分类下的所有子分类，以及递归子分类
     *
     * @param parent      父分类
     * @param allCategory 所有分类
     * @return 子分类
     */
    private List<CategoryEntity> getChildrenCategory(CategoryEntity parent, List<CategoryEntity> allCategory) {
        return allCategory.stream()
                .filter(c -> c.getParentCid().equals(parent.getCatId()))
                .peek(c -> c.setChildren(getChildrenCategory(c, allCategory)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

}