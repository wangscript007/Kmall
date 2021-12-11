package xyz.klenkiven.kmall.search.vo;

import lombok.Data;

/**
 * Brand Info from Product Service
 * DTO
 * @author klenkiven
 */
@Data
public class BrandVO {
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名
     */
    private String name;
    /**
     * 品牌logo地址
     */
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    private Integer showStatus;
    /**
     * 检索首字母
     */
    private String firstLetter;
    /**
     * 排序
     */
    private Integer sort;
}
