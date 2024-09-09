package trendravel.photoravel_be.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseMemberDto {

    private String provider;
    private String ID;
    private String profileImg;
    private String email;
    private String nickname;

    private boolean isExist;

}
