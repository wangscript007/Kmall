package xyz.klenkiven.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.klenkiven.common.utils.R;

/**
 * Kmall Exception Handler
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 */
@Slf4j
@RestControllerAdvice
public class SystemExceptionHandler {

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(SystemException.class)
	public R handleSystemException(SystemException e){
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		log.error(e.getMessage(), e);
		return R.error();
	}
}
