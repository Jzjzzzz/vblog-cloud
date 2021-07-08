package team.ark.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.ark.core.response.C;
import team.ark.core.response.R;

/**
 * 全局异常处理
 *
 * @author Ashinch
 * @date 2021/01/01
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public R exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @ExceptionHandler(ArkAbstractException.class)
    public R abstractExceptionHandler(ArkAbstractException e) {
        return R.with(e);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public R accessDeniedExceptionHandler(AccessDeniedException e) {
        return R.with(C.ACCESS_DENIED);
    }
}
