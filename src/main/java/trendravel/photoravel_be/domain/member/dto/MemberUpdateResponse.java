package trendravel.photoravel_be.domain.member.dto;

import lombok.*;
import trendravel.photoravel_be.domain.token.model.TokenResponse;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateResponse {

    private String memberId;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String profileImg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private TokenResponse token;
}
