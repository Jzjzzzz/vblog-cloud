package team.ark.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * 配置异常
 *
 * @author Ashinch
 * @date 2021/02/21
 */
public class ArkConfigException extends ArkAbstractException {
    private static final long serialVersionUID = -4264742337239260407L;

    public ArkConfigException(String message) {
        super(message);
    }

    public ArkConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @return 500
     */
    @NonNull
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
