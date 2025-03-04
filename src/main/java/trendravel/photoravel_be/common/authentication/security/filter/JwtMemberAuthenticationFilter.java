package trendravel.photoravel_be.common.authentication.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import trendravel.photoravel_be.common.exception.error.TokenErrorCode;
import trendravel.photoravel_be.common.exception.JwtException;
import trendravel.photoravel_be.db.inmemorydb.entity.Token;
import trendravel.photoravel_be.common.authentication.session.UserSession;
import trendravel.photoravel_be.common.authentication.token.service.TokenService;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Order(2)
public class JwtMemberAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService memberAuthenticationService;
    private final TokenService tokenService;
    private final RedisTemplate<String, Token> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 URI를 가져옴
        String path = request.getRequestURI();

        // 인증이 필요한지 체크
        if (!isAuthenticationRequired(path)) {
            // 인증이 필요 없는 경우, 필터를 통과.
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request);

        // Authorization 헤더에 토큰이 있는지 확인
        if (StringUtils.hasText(token)) {
            String role = tokenService.validationTokenWithMap(token).get("role").toString();

            if (role.equals("member")) {
                String tokenMemberId = tokenService.validationTokenWithMemberId(token);
                Token storedRefreshToken = (Token) redisTemplate.opsForHash().get("member_refresh_token", tokenMemberId);

                // redis 에 저장된 refresh token이 있는지 확인
                if (storedRefreshToken != null) {
                    String storedMemberId = tokenService.validationTokenWithMemberId(storedRefreshToken.getRefreshToken());

                    // refresh token payload의 memberId와 redis token의 memberId를 비교
                    if (tokenMemberId.equals(storedMemberId)) {
                        UserSession userSession = (UserSession) memberAuthenticationService.loadUserByUsername(tokenMemberId);

                        // 인증 정보 생성 후
                        // 인증 정보를 SecurityContextHolder 에 저장
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userSession, null, userSession.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                        filterChain.doFilter(request, response);
                    } else {
                        throw new JwtException(TokenErrorCode.INVALID_TOKEN);
                    }
                } else {
                    throw new JwtException(TokenErrorCode.REFRESH_TOKEN_NOT_VALID);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            throw new JwtException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }
    }

    private String getToken(HttpServletRequest req){
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    private boolean isAuthenticationRequired(String path) {
        // 인증이 필요 없는 경로를 정의
        return !(path.startsWith("/public") || path.startsWith("/login") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs"));
    }
}
