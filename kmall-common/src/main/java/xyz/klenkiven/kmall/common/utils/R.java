package xyz.klenkiven.kmall.common.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import xyz.klenkiven.kmall.common.exception.ExceptionCodeEnum;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	private static final transient ObjectMapper objectMapper = new ObjectMapper();
	
	public R() {
		put("code", 0);
		put("msg", "success");
	}
	
	public static R error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static R error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}

	public static R error(ExceptionCodeEnum e) {
		return error(e.getCode(), e.getMessage());
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	public int getCode() {
		return (Integer) getOrDefault("code", 0);
	}

	public <T> T getData(String key, TypeReference<T> typeReference) {
		try {
			String s = objectMapper.writeValueAsString(get(key));
			return objectMapper.readValue(s, typeReference);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
