package xyz.klenkiven.kmall.product.vo;
import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2021-10-15 20:45:48
 *
 * @author bejson.com (i@bejson.com)
 */
@Data
public class SpuSaveVO {

    private String spuName;

    private String spuDescription;

    private int catalogId;

    private String brandId;

    private int weight;

    private int publishStatus;

    private List<String> decript;

    private List<String> images;

    private Bounds bounds;

    private List<BaseAttrs> baseAttrs;

    private List<Skus> skus;
}