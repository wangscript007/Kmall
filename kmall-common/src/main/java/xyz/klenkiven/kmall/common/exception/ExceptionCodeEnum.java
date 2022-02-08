package xyz.klenkiven.kmall.common.exception;

/**
 * Kmall Exception Enum
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 */
public enum ExceptionCodeEnum {

    /** Kmall Validation Error */
    VALIDATION_ERROR(100001, "Arguments validation incorrect."),

    /** Product Up Status Error */
    PRODUCT_UP_ERROR(110000, "Product Up Status Error"),

    /** SMS code send too frequently */
    SMS_CODE_ERROR(100002, "SMS code send too frequently"),

    /** Username Exist Error */
    USERNAME_EXIST_ERROR(150001, "Username has existed"),

    /** Phone Exist Error */
    PHONE_EXIST_ERROR(150002, "Phone has existed");

    Integer code;
    String message;
    ExceptionCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
