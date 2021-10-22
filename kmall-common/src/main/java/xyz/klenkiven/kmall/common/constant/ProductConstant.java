package xyz.klenkiven.kmall.common.constant;

/**
 * Kmall - Product
 * <p>Kmall-Product to use</p>
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 */
public class ProductConstant {

    /**
     * Product - Attribute Type
     * @author klenkiven
     */
    public enum AttrType {
        /**
         * Sale Attribute Type
         */
        ATTR_TYPE_SALE(0, "Sale Attribute Type"),
        /**
         * Base Attribute Type
         */
        ATTR_TYPE_BASE(1, "Base Attribute Type");

        Integer code;
        String msg;
        AttrType(Integer code, String msg) {
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
     * Product - SPU Status
     * @author klenkiven
     */
    public enum SpuStatus {
        /**
         * Created
         */
        CREATED(0, "Created"),

        /**
         * SPU Up
         */
        SPU_UP(1, "Spu Up"),

        /**
         * SPU Down
         */
        SPU_DOWN(2, "SPU Down");


        Integer code;
        String msg;
        SpuStatus(Integer code, String msg) {
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
