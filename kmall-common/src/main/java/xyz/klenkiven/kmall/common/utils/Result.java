package xyz.klenkiven.kmall.common.utils;

import lombok.Data;
import org.apache.http.HttpStatus;
import xyz.klenkiven.kmall.common.exception.ExceptionCodeEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Generalized Result
 *
 * @author klenkiven
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    private T data;

    public Result() {
        code = 0;
        msg = "success";
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                     Static Method                                       //
    /////////////////////////////////////////////////////////////////////////////////////////////

    public static <T> Result<T> error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static <T> Result<T> error(T data) {
        Result<T> r =  error();
        r.setData(data);
        return r;
    }

    public static <T> Result<T> error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static <T> Result<T> error(ExceptionCodeEnum e) {
        return error(e.getCode(), e.getMessage());
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static <T> Result<T> ok(String msg) {
        Result<T> r = new Result<>();
        r.setMsg(msg);
        return r;
    }

    public static <T> Result<T> ok() {
        return new Result<>();
    }

    public static <T> Result<T> ok(T data) {
        Result<T> r = ok();
        r.setData(data);
        return r;
    }

}

