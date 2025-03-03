package trendravel.photoravel_be.common.exception.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LocationErrorCode implements ErrorCodeIfs{

    LOCATION_NOT_FOUND(404, "장소를 찾을 수 없음")
    ;


    private final Integer httpStatusCode;
    private final String errorDescription;
}
