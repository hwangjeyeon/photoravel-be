package trendravel.photoravel_be.domain.authentication.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import trendravel.photoravel_be.db.enums.Region;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotographerSession implements UserDetails {

    private String accountId;
    private String password;
    private String name;
    private Region region;
    private String description;
    private String profileImg;
    private Integer careerYear;
    private Integer matchingCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_PHOTOGRAPHER"));

    }

    @Override
    public String getPassword() {
        return password;
    }

    // 메소드 명은 시큐리티 기본으로 username이지만
    //* !!!! IMPORTANT !!!! *

    // 실제로 내부 로직에서 사용하는 것은 memberId입니다.
    @Override
    public String getUsername() {
        return accountId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
