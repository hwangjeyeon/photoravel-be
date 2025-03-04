package trendravel.photoravel_be.common.authentication.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import trendravel.photoravel_be.common.authentication.session.PhotographerSession;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.db.respository.photographer.PhotographerRepository;

@Service("photographerAuthenticationService")
@RequiredArgsConstructor
public class PhotographerAuthenticationService implements UserDetailsService {

    private final PhotographerRepository photographerRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Photographer photographer = photographerRepository.findByAccountId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사진작가입니다."));

        return PhotographerSession.builder()
                .accountId(photographer.getAccountId())
                .password(photographer.getPassword())
                .name(photographer.getName())
                .region(photographer.getRegion())
                .description(photographer.getDescription())
                .profileImg(photographer.getProfileImg())
                .careerYear(photographer.getCareerYear())
                .matchingCount(photographer.getMatchingCount())
                .createdAt(photographer.getCreatedAt())
                .updatedAt(photographer.getUpdatedAt())
                .role("ROLE_PHOTOGRAPHER")
                .build();
        }
}
