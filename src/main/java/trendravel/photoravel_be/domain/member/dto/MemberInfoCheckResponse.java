package trendravel.photoravel_be.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoCheckResponse {

    private boolean isDuplicated;
}
