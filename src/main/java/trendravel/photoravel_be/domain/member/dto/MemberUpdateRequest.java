package trendravel.photoravel_be.domain.member.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequest {

    private String memberId;
    private String password;
    private String name;
    private String nickname;
    private String email;
}
