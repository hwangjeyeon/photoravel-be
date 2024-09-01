package trendravel.photoravel_be.domain.member.dto;

import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberModRequest {

    private String memberId;
    private String password;
    private String name;
    private String nickname;
    private String profileImg;
}
