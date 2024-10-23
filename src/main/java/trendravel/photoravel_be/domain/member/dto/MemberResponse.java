package trendravel.photoravel_be.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

    private String memberId;
    private String email;
    private String name;
    private String nickname;
    private String profileImg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
