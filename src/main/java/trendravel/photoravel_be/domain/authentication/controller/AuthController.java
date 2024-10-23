package trendravel.photoravel_be.domain.authentication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import trendravel.photoravel_be.commom.response.Api;
import trendravel.photoravel_be.domain.authentication.dto.RefreshTokenRequest;
import trendravel.photoravel_be.db.inmemorydb.entity.Token;
import trendravel.photoravel_be.domain.token.model.TokenDto;
import trendravel.photoravel_be.domain.token.service.TokenService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;
    private final RedisTemplate<String, Token> redisTemplate;

    @PostMapping("/refresh-token")
    public ResponseEntity<Api<Object>> refreshAccessToken(
            @RequestBody RefreshTokenRequest request
    ) {

        String memberId = tokenService.validationTokenWithMemberId(request.getRefreshToken());

        Token storedRefreshToken = (Token) redisTemplate.opsForHash().get("member_refresh_token", memberId);

        log.info("stored refresh token: {}", storedRefreshToken.getRefreshToken());

        // 레디스에 리프레쉬 토큰이 저장되어 있는지 && 요청으로 보낸 토큰과 레디스에 저장된 토큰이 일치하는지
        if (storedRefreshToken != null && request.getRefreshToken().equals(storedRefreshToken.getRefreshToken())) {
            // 정상적인 경우 액세스 토큰 재발급
            TokenDto accessToken = tokenService.issueAccessToken(memberId);
            return ResponseEntity.ok(Api.OK(accessToken));
        } else {
            // http status code를 명확히 하기 위해 ResponseEntity 사용
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Api.ERROR(400, "Invalid refresh token"));
            // 정상적이지 않은 경우 bad request
        }
    }

    @PostMapping("/logout")
    public Api<?> logout(@RequestBody RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        // memberId null check는 validation 메소드 안에서 이미 진행
        String memberId = tokenService.validationTokenWithMemberId(refreshToken);

        // 소셜 로그인 및 로컬 로그인 모두 로그아웃 프로세스는 동일
        redisTemplate.opsForHash().delete("member_refresh_token", memberId);
        // 저장된 컨텍스트 안의 principal 삭제
        SecurityContextHolder.clearContext();

        return Api.OK("로그아웃되었습니다.");
    }
}
