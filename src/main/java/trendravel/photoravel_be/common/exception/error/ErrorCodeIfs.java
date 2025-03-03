package trendravel.photoravel_be.common.exception.error;

public interface ErrorCodeIfs {

    Integer getHttpStatusCode();
    String getErrorDescription();
}
