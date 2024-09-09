package trendravel.photoravel_be.commom.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenErrorCode implements ErrorCodeIfs{

    // 토큰 관련 status 코드는 직접 정의한 code를 사용합니다.
    INVALID_TOKEN(2000, "유효하지 않은 토큰"),
    EXPIRED_TOKEN(2001, "만료된 토큰입니다."),
    TOKEN_EXCEPTION(2002, "알 수 없는 토큰 에러"),
    AUTHORIZATION_TOKEN_NOT_FOUND(2003, "인증 헤더 토큰 없음"),
    REFRESH_TOKEN_NOT_VALID(2004, "저장된 리프레쉬 토큰 없음(로그인 후 이용 가능)")
    ;

    private final Integer httpStatusCode;
    private final String errorDescription;
}
