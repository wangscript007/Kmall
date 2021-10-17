package xyz.klenkiven.kmall.common.constant;

/**
 * Kmall - Ware
 * <p>Kmall-Ware to use</p>
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 */
public class WareConstant {

    /**
     * Ware - Purchase Status
     * @author klenkiven
     */
    public enum PurchaseStatus {

        CREATED(0, "Created Just Now"), ASSIGNED(1, "Assigned to Someone"),
        RECEIVED(2, "Received by someone who has be assigned"), FINISHED(3, "Purchase Finished"),
        HAS_ERROR(4, "Purchase has error");

        Integer code;
        String msg;

        PurchaseStatus(Integer code, String msg) {
            this.code = code;
            this.msg  = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

    }

    /**
     * Ware - Purchase Status
     * @author klenkiven
     */
    public enum PurchaseDetailStatus {

        CREATED(0, "Created Just Now"), ASSIGNED(1, "Assigned to Purchase"),
        BUYING(2, "Buying product"), FINISHED(3, "Purchase Finished"),
        HAS_ERROR(4, "Purchase has error");

        Integer code;
        String msg;

        PurchaseDetailStatus(Integer code, String msg) {
            this.code = code;
            this.msg  = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

    }

}
