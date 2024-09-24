package trendravel.photoravel_be.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterRequest {

    private String memberId;
    private String email;
    private String password;
    private String name;
    private String nickname;
}
