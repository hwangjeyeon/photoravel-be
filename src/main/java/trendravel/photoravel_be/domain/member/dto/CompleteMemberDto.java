package trendravel.photoravel_be.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteMemberDto {

    private String provider;
    private String id;
    private String name;
    private String nickname;
    private String email;
    private String profileImg;
}
