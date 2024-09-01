package trendravel.photoravel_be.domain.member.convertor;

import org.springframework.stereotype.Component;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.domain.member.dto.MemberResponse;

@Component
public class MemberConvertor {


    public MemberResponse toMemberResponse(MemberEntity saved) {
        return MemberResponse.builder()
                .memberId(saved.getMemberId())
                .email(saved.getEmail())
                .password(saved.getPassword())
                .name(saved.getName())
                .nickname(saved.getNickname())
                .profileImg(saved.getProfileImg())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
}
