package xyz.klenkiven.kmall.product.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.klenkiven.kmall.product.entity.AttrGroupEntity;
import xyz.klenkiven.kmall.product.service.AttrGroupService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.R;
import xyz.klenkiven.kmall.product.service.AttrService;
import xyz.klenkiven.kmall.product.vo.AttrRelationVO;
import xyz.klenkiven.kmall.product.vo.AttrVO;

/**
 * 属性分组
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
@RestController
@RequestMapping("product/attrgroup")
@RequiredArgsConstructor
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    private final AttrService attrService;

    /**
     * 列表
     */
    @GetMapping("/list")
    // @RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * Get page by category ID
     */
    @GetMapping("/list/{catalogId}")
    public R listCatId(@RequestParam Map<String, Object> params,
                       @PathVariable("catalogId") Long catalogId) {
        PageUtils page = attrGroupService.queryPage(params, catalogId);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{attrGroupId}")
    // @RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getByIdWithCatPath(attrGroupId);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 获取指定分组关联的所有属性
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable String attrGroupId) {
        List<AttrVO> result = attrGroupService.listAllAttrRelation(attrGroupId);
        return R.ok().put("data", result);
    }

    /**
     * 添加属性与分组关联关系
     */
    @PostMapping("/attr/relation")
    public R attrRelationSave(@RequestBody List<AttrRelationVO> attrRelationList) {
        attrGroupService.saveBatchAttrRelation(attrRelationList);

        return R.ok();
    }

    /**
     * 删除属性与分组的关联关系
     */
    @PostMapping("/attr/relation/delete")
    public R attrRelationDelete(@RequestBody List<AttrRelationVO> attrRelationList) {
        attrGroupService.removeBatchAttrRelation(attrRelationList);

        return R.ok();
    }

    /**
     * 获取属性分组没有关联的其他属性
     */
    @GetMapping("/{attrGroupId}/noattr/relation")
    public R attrNoRelation(@RequestParam Map<String, Object> params,
                            @PathVariable Long attrGroupId) {
        PageUtils page = attrService.pageAttrNoRelation(params, attrGroupId);
        return R.ok().put("page", page);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
