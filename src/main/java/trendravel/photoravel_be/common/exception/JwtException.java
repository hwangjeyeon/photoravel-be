package trendravel.photoravel_be.common.exception;

import lombok.Getter;
import trendravel.photoravel_be.common.exception.error.ErrorCodeIfs;

@Getter
public class JwtException extends RuntimeException{

    private final ErrorCodeIfs errorCodeIfs;
    private final String errorDescription;

    public JwtException(ErrorCodeIfs errorCodeIfs){
        super(errorCodeIfs.getErrorDescription());
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorCodeIfs.getErrorDescription();
    }

    public JwtException(ErrorCodeIfs errorCodeIfs, Throwable cause) {
        super(cause);
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorCodeIfs.getErrorDescription();
    }

    public JwtException(ErrorCodeIfs errorCodeIfs, String errorDescription) {
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorDescription;
    }

    public JwtException(String message, ErrorCodeIfs errorCodeIfs, String errorDescription) {
        super(message);
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorDescription;
    }

    public JwtException(String message, Throwable cause, ErrorCodeIfs errorCodeIfs, String errorDescription) {
        super(message, cause);
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorDescription;
    }

    public JwtException(Throwable cause, ErrorCodeIfs errorCodeIfs, String errorDescription) {
        super(cause);
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorDescription;
    }

    public JwtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCodeIfs errorCodeIfs, String errorDescription) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorDescription;
    }
}
