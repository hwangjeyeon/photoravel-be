package trendravel.photoravel_be.commom.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpotErrorCode implements ErrorCodeIfs{

    SPOT_NOT_FOUND(404, "스팟을 찾을 수 없음")
    ;



    private final Integer httpStatusCode;
    private final String errorDescription;

}
