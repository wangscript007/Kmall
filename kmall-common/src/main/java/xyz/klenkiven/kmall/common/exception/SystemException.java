/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package xyz.klenkiven.kmall.common.exception;

/**
 * 自定义异常
 *
 * @author KlenKiven
 */
public class SystemException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public SystemException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public SystemException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public SystemException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public SystemException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
