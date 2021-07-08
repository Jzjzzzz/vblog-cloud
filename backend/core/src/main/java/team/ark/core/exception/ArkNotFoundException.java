package team.ark.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * 资源未找到异常
 *
 * @author Ashinch
 * @date 2021/02/21
 */
public class ArkNotFoundException extends ArkAbstractException {
    private static final long serialVersionUID = 4569948049423773161L;

    public ArkNotFoundException(String message) {
        super(message);
    }

    public ArkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @return 404
     */
    @NonNull
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
