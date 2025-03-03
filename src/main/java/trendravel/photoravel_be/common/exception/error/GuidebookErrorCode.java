package trendravel.photoravel_be.common.exception.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GuidebookErrorCode implements ErrorCodeIfs{
    
    GUIDEBOOK_NOT_FOUND(404, "가이드북을 찾을 수 없음");


    private final Integer httpStatusCode;
    private final String errorDescription;

}
