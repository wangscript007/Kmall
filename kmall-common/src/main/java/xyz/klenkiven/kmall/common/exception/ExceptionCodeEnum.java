package xyz.klenkiven.kmall.common.exception;

/**
 * Kmall Exception Enum
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 */
public enum ExceptionCodeEnum {

    /**
     * Kmall Validation Error
     */
    VALIDATION_ERROR(100001, "Arguments validation incorrect.");

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
