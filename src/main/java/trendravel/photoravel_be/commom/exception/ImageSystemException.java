package trendravel.photoravel_be.commom.exception;

import lombok.Getter;
import trendravel.photoravel_be.commom.error.ErrorCodeIfs;

@Getter
public class ImageSystemException extends RuntimeException{

    private final ErrorCodeIfs errorCodeIfs;
    private final String errorDescription;


    public ImageSystemException(ErrorCodeIfs errorCodeIfs) {
        super(errorCodeIfs.getErrorDescription());
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorCodeIfs.getErrorDescription();
    }

    public ImageSystemException(ErrorCodeIfs errorCodeIfs, Throwable cause) {
        super(cause);
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorCodeIfs.getErrorDescription();
    }


}
