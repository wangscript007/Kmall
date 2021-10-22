package xyz.klenkiven.kmall.common.utils;

import org.apache.http.HttpStatus;
import xyz.klenkiven.kmall.common.exception.ExceptionCodeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R<T> extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	private T data;

	public R() {
		put("code", 0);
		put("msg", "success");
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	//                                     Static Method                                       //
	/////////////////////////////////////////////////////////////////////////////////////////////

	public static <T> R<T> error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}

	public static <T> R<T> error(T data) {
		R<T> r =  error();
		r.setData(data);
		return r;
	}
	
	public static <T> R<T> error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}

	public static <T> R<T> error(ExceptionCodeEnum e) {
		return error(e.getCode(), e.getMessage());
	}
	
	public static <T> R<T> error(int code, String msg) {
		R<T> r = new R<>();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static <T> R<T> ok(String msg) {
		R<T> r = new R<>();
		r.put("msg", msg);
		return r;
	}
	
	public static <T> R<T> ok(Map<String, Object> map) {
		R<T> r = new R<>();
		r.putAll(map);
		return r;
	}
	
	public static <T> R<T> ok() {
		return new R<>();
	}

	public static <T> R<T> ok(T data) {
		return new R<T>().setData(data);
	}

	@Override
	public R<?> put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	/**
	 * Get Code
	 */
	public int getCode() {
		return (Integer) getOrDefault("code", 0);
	}

	public T getData() {
		return data;
	}

	public R<T> setData(T data) {
		this.data = data;
		return this;
	}
}
