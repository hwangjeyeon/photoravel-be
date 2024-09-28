package trendravel.photoravel_be.commom.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.client.HttpClientErrorException;

@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCodeIfs{

    USER_NOT_FOUND(400, "유저를 찾을 수 없음"),
    EXISTS_USER(400, "이미 존재하는 회원입니다."),
    PASSWORD_NOT_MATCH(400, "패스워드가 일치하지 않습니다."),
    MEMBER_ID_NOT_MATCH(400, "아이디가 일치하지 않습니다."),
    UNAUTHORIZED(401, "자신의 정보만 접근 가능합니다.")
    ;

    private final Integer httpStatusCode;
    private final String errorDescription;
}
