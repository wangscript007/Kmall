package xyz.klenkiven.kmall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * Attribute Group Response VO
 * @author klenkiven
 */
@Data
public class AttrGroupRespVO {

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * Attributes belonging to attrGroup
     */
    private List<AttrVO> attrs;

}
