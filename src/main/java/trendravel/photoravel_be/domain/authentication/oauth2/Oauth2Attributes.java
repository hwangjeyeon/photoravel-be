package trendravel.photoravel_be.domain.authentication.oauth2;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
public class Oauth2Attributes {

    private final String ID;
    private final String name;
    private final String email;
    private final String profileImg;

    @Builder
    public Oauth2Attributes(String id, String name, String email, String profileImg) {
        ID = id;
        this.name = name;
        this.email = email;
        this.profileImg = profileImg;
    }

    public static Oauth2Attributes of(String registrationId, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(attributes);
        } else if ("google".equals(registrationId)) {
            return ofGoogle(attributes);
        }
        throw new RuntimeException("지원하지 않는 provider입니다.");
    }

    //google
    private static Oauth2Attributes ofGoogle(Map<String, Object> attributes) {
        Object googleID = attributes.get("google_id");
        Object googleEmail = attributes.get("email");
        Object googleName = attributes.get("name");
        Object googleProfileImg = attributes.get("profileImg");


        return Oauth2Attributes.builder()
//                .id("id")
                .name(String.valueOf(googleName))
                .email(googleEmail.toString())
                .profileImg(googleProfileImg.toString())
                .build();
    }

    //kakao
    private static Oauth2Attributes ofKakao(Map<String, Object> attributes) {

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) attributes.get("properties");

        return Oauth2Attributes.builder()
                .id(String.valueOf(attributes.get("id")))
                .name(String.valueOf(kakaoProfile.get("nickname")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .profileImg(String.valueOf(kakaoProfile.get("profile_image")))
                .build();
    }
}
