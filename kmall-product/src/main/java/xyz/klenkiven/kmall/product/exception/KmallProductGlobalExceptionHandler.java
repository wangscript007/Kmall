package xyz.klenkiven.kmall.product.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.klenkiven.common.exception.ExceptionCodeEnum;
import xyz.klenkiven.common.utils.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Kmall Product Exception Handler
 * <p>It's in order to catch exception in controller when in run time</p>
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 */
@Slf4j
@RestControllerAdvice(basePackages = {"xyz.klenkiven.kmall.product.controller"})
public class KmallProductGlobalExceptionHandler {

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
