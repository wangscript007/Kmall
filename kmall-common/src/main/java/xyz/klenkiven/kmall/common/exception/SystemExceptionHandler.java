package xyz.klenkiven.kmall.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.klenkiven.kmall.common.utils.R;

import java.util.HashMap;
import java.util.Map;

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

	/**
	 * Kmall Validation Exception Handler
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public R globalHandler(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		Map<String, String> map = new HashMap<>();
		bindingResult.getFieldErrors().forEach(err -> {
			String field = err.getField();
			String message = err.getDefaultMessage();
			map.put(field, message);
		});
		log.info(map.toString());
		return R.error(ExceptionCodeEnum.VALIDATION_ERROR).put("data", map);
	}
}
