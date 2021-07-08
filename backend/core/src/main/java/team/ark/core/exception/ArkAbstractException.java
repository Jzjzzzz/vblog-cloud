package team.ark.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * 自定义抽象异常
 *
 * @author Ashinch
 * @date 2021/02/21
 */
public abstract class ArkAbstractException extends RuntimeException {
    private static final long serialVersionUID = -5639698651743707504L;

    public ArkAbstractException(String message) {
        super(message);
    }

    public ArkAbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 获取响应码
     *
     * @return {@link HttpStatus}
     */
    @NonNull
    public abstract HttpStatus getStatus();
}
