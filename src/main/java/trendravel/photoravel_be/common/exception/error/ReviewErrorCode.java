package trendravel.photoravel_be.common.exception.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements ErrorCodeIfs{


    REVIEW_NOT_FOUND(404, "리뷰를 찾을 수 없음")
            ;


    private final Integer httpStatusCode;
    private final String errorDescription;

}
