package trendravel.photoravel_be.commom.error;

public interface ErrorCodeIfs {

    Integer getHttpStatusCode();
    String getErrorDescription();
}
