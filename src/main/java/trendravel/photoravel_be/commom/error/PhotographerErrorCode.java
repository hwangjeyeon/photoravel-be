package trendravel.photoravel_be.commom.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PhotographerErrorCode implements ErrorCodeIfs{
    
    PHOTOGRAPHER_NOT_FOUND(404, "사진작가를 찾을 수 없음");


    private final Integer httpStatusCode;
    private final String errorDescription;

}
