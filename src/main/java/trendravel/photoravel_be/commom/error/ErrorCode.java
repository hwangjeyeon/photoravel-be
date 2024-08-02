package trendravel.photoravel_be.commom.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeIfs{

    OK(200, "성공"),
    READ(200, "READ"),
    UPDATED(200, "UPDATED"),
    DELETED(200, "DELETED"),
    CREATED(201, "CREATED"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "잘못된 요청"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내 오류"),
    NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), "null point"),
    FORBIDDEN_ERROR(HttpStatus.FORBIDDEN.value(), "권한 없음"),
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED.value(), "인증 안 됨");

    private final Integer httpStatusCode;   // 클라이언트에 보여줄 status code
    private final String errorDescription;  // 에러 설명
}
