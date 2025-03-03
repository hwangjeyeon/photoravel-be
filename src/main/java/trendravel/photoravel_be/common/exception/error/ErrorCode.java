package trendravel.photoravel_be.common.exception.error;

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
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED.value(), "인증 안 됨"),
    IMAGES_NOT_FOUND(404, "이미지 없음"),
    IMAGES_UPLOAD_ERROR(500, "이미지 저장소 업로드 실패"),
    HTTP_INPUT_NOT_READABLE(400, "잘못된 HTTP 입력 요청"),
    ENUM_TYPE_NOT_READABLE(400, "잘못된 ENUM 타입 전달, [LOCATION, SPOT, PHOTOGRAPHER] 중 하나로 보낼 것"),
    INPUT_FORMAT_ERROR(400, "잘못된 타입의 입력 요청"),
    IMAGE_SIZE_EXCEED_ERROR(400, "요청 이미지 최대 용량 초과");

    private final Integer httpStatusCode;   // 클라이언트에 보여줄 status code
    private final String errorDescription;  // 에러 설명
}
