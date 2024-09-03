package trendravel.photoravel_be.domain.authentication.oauth2.social;

import lombok.Getter;

@Getter
public class GoogleToken {

    // 구글 인증 서버 토큰 형식

    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
