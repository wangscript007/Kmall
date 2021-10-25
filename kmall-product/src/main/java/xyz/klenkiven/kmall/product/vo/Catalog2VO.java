package xyz.klenkiven.kmall.product.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Index Page Required JSON VO
 * @author klenkiven
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Catalog2VO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long catalog1Id;

    private List<Catalog3VO> catalog3List;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Catalog3VO {

        @JsonSerialize(using = ToStringSerializer.class)
        private Long catalog2Id;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;

        private String name;
    }

}
