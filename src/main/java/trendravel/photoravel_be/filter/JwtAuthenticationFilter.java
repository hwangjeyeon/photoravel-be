package trendravel.photoravel_be.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import trendravel.photoravel_be.commom.error.TokenErrorCode;
import trendravel.photoravel_be.commom.exception.JwtException;
import trendravel.photoravel_be.db.inmemorydb.entity.Token;
import trendravel.photoravel_be.domain.authentication.session.UserSession;
import trendravel.photoravel_be.domain.token.service.TokenService;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
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

        if(StringUtils.hasText(token)){
            String memberId = tokenService.validationTokenWithMemberId(token);
            Token storedRefreshToken = (Token) redisTemplate.opsForHash().get("refresh_token", memberId);

            if (storedRefreshToken != null) {
                String storedMemberId = tokenService.validationTokenWithMemberId(storedRefreshToken.getRefreshToken());
                if (memberId.equals(storedMemberId)) {
                    UserSession userSession = (UserSession) userDetailsService.loadUserByUsername(memberId);
                    log.info("user session : {}", userSession.getUsername());
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userSession, null, userSession.getAuthorities());
                    // 인증 정보 생성
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //인증 정보를 SecurityContextHolder 에 저장
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    filterChain.doFilter(request, response);
                } else {
                    throw new JwtException(TokenErrorCode.INVALID_TOKEN);
                }
            } else {
                throw new JwtException(TokenErrorCode.REFRESH_TOKEN_NOT_VALID);
            }
        }else {
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
