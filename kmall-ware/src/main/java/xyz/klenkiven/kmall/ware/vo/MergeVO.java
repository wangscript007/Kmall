package xyz.klenkiven.kmall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * Merge Purchase VO
 * @author klenkiven
 */
@Data
public class MergeVO {

    private Long purchaseId;

    private List<Long> items;

}
