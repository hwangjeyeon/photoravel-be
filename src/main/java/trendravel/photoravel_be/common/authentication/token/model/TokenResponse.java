package trendravel.photoravel_be.common.authentication.token.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    private TokenDto accessToken;
    private TokenDto refreshToken;
}
