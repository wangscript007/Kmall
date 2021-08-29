package xyz.klenkiven.kmall.ware.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xyz.klenkiven.kmall.ware.entity.WmsPurchaseDetailEntity;
import xyz.klenkiven.kmall.ware.service.WmsPurchaseDetailService;
import xyz.klenkiven.common.utils.PageUtils;
import xyz.klenkiven.common.utils.R;



/**
 * 
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 21:12:37
 */
@RestController
@RequestMapping("ware/wmspurchasedetail")
public class WmsPurchaseDetailController {
    @Autowired
    private WmsPurchaseDetailService wmsPurchaseDetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("ware:wmspurchasedetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wmsPurchaseDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("ware:wmspurchasedetail:info")
    public R info(@PathVariable("id") Long id){
		WmsPurchaseDetailEntity wmsPurchaseDetail = wmsPurchaseDetailService.getById(id);

        return R.ok().put("wmsPurchaseDetail", wmsPurchaseDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("ware:wmspurchasedetail:save")
    public R save(@RequestBody WmsPurchaseDetailEntity wmsPurchaseDetail){
		wmsPurchaseDetailService.save(wmsPurchaseDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("ware:wmspurchasedetail:update")
    public R update(@RequestBody WmsPurchaseDetailEntity wmsPurchaseDetail){
		wmsPurchaseDetailService.updateById(wmsPurchaseDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("ware:wmspurchasedetail:delete")
    public R delete(@RequestBody Long[] ids){
		wmsPurchaseDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
