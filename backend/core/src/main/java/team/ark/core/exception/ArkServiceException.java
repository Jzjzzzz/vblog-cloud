package team.ark.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * 服务异常
 *
 * @author Ashinch
 * @date 2021/02/21
 */
public class ArkServiceException extends ArkAbstractException {
    private static final long serialVersionUID = 2041977051697910435L;

    public ArkServiceException(String message) {
        super(message);
    }

    public ArkServiceException(String message, Throwable cause) {
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
