package trendravel.photoravel_be.common.authentication.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import trendravel.photoravel_be.common.authentication.session.UserSession;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.respository.member.MemberRepository;

@Service("memberAuthenticationService")
@RequiredArgsConstructor
public class MemberAuthenticationService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // spring security 기본 설정은 username 이지만
        // 내부 로직에서 사용하는 필드는 member의 memberId 필드입니다!!
        MemberEntity memberEntity = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));

        return UserSession.builder()
                .email(memberEntity.getEmail())
                .memberId(memberEntity.getMemberId())
                .password(memberEntity.getPassword())
                .name(memberEntity.getName())
                .nickname(memberEntity.getNickname())
                .profileImg(memberEntity.getProfileImg())
                .createdAt(memberEntity.getCreatedAt())
                .updatedAt(memberEntity.getUpdatedAt())
                .role("ROLE_MEMBER")
                .build();
    }
}
