package trendravel.photoravel_be.domain.authentication.oauth2.social;

import lombok.Getter;

@Getter
public class KakaoToken {

    // 카카오 인증 서버 토큰 형식

    private String id_token;
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;
    private String refresh_token_expires_in;
}
