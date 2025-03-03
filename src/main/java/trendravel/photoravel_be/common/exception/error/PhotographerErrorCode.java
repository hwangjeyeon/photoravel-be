package trendravel.photoravel_be.common.exception.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PhotographerErrorCode implements ErrorCodeIfs{
    
    PHOTOGRAPHER_NOT_FOUND(404, "사진작가를 찾을 수 없음"),
    UNAUTHORIZED(401, "자신의 정보만 접근 가능합니다.");


    private final Integer httpStatusCode;
    private final String errorDescription;

}
