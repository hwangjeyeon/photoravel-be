package trendravel.photoravel_be.commom.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenErrorCode implements ErrorCodeIfs{

    INVALID_TOKEN(400, "유효하지 않은 토큰"),
    EXPIRED_TOKEN(400, "만료된 토큰입니다."),
    TOKEN_EXCEPTION(400, "알 수 없는 토큰 에러"),
    ;

    private final Integer httpStatusCode;
    private final String errorDescription;
}
