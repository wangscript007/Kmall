package xyz.klenkiven.kmall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.klenkiven.kmall.product.entity.AttrEntity;
import xyz.klenkiven.kmall.product.entity.ProductAttrValueEntity;
import xyz.klenkiven.kmall.product.service.AttrService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.R;
import xyz.klenkiven.kmall.product.service.ProductAttrValueService;
import xyz.klenkiven.kmall.product.vo.AttrRespVO;
import xyz.klenkiven.kmall.product.vo.AttrVO;


/**
 * 商品属性
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
@RestController
@RequestMapping("product/attr")
@RequiredArgsConstructor
public class AttrController {

    private final AttrService attrService;
    private final ProductAttrValueService productAttrValueService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取spu规格
     * /product/attr/base/listforspu/{spuId}
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R listForSpu(@PathVariable String spuId) {
        List<ProductAttrValueEntity> result = productAttrValueService.listBaseAttrForSpu(spuId);

        return R.ok().put("data", result);
    }

    /**
     * List
     */
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params,
                      @PathVariable("catelogId") Long catalogId,
                      @PathVariable("attrType") String attrType) {
        PageUtils page = attrService.queryBasePage(catalogId, params, attrType);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    // @RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVO attr = attrService.getDetailById(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVO attr){
		attrService.saveVO(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVO attr){
		attrService.updateVO(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeCascadeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
