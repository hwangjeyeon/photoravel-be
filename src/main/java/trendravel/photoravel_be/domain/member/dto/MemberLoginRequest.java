package trendravel.photoravel_be.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRequest {


    private String memberId;
    private String password;
}
