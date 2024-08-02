package trendravel.photoravel_be.commom.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCodeIfs{

    USER_NOT_FOUND(400, "유저를 찾을 수 없음"),
    EXISTS_USER(400, "이미 존재하는 회원입니다."),
    ;

    private final Integer httpStatusCode;
    private final String errorDescription;
}
