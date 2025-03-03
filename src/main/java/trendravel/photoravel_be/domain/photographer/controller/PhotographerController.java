package trendravel.photoravel_be.domain.photographer.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.common.response.Api;
import trendravel.photoravel_be.common.response.Result;
import trendravel.photoravel_be.db.inmemorydb.entity.Token;
import trendravel.photoravel_be.domain.authentication.dto.RefreshTokenRequest;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerLoginRequestDto;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerRequestDto;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerUpdateDto;
import trendravel.photoravel_be.domain.photographer.dto.response.PhotographerListResponseDto;
import trendravel.photoravel_be.domain.photographer.dto.response.PhotographerSingleResponseDto;
import trendravel.photoravel_be.domain.photographer.service.PhotographerService;
import trendravel.photoravel_be.domain.token.model.TokenDto;
import trendravel.photoravel_be.domain.token.model.TokenResponse;
import trendravel.photoravel_be.domain.token.service.TokenService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PhotographerController {
    
    private final PhotographerService photographerService;
    private final TokenService tokenService;
    private final RedisTemplate<String, Token> redisTemplate;

    @Schema(description = "사진작가 회원 가입(CREATE) 요청",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/public/photographers/join",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result createPhotographer(
            @RequestPart(value = "data") PhotographerRequestDto photographerRequestDto,
            @RequestPart(value = "image", required = false) List<MultipartFile> images) {

        photographerService.createPhotographer(photographerRequestDto, images);

        return Result.CREATED();
    }

    @Schema(description = "사진작가 로그인 요청",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/public/photographers/login")
    public Api<TokenResponse> loginPhotographer(@RequestBody PhotographerLoginRequestDto loginRequestDto) {

        TokenResponse response = photographerService.authenticate(loginRequestDto.getUsername(),
                loginRequestDto.getPassword());

        return Api.OK(response);
    }

    @Schema(description = "사진작가 목록 READ 요청",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/public/photographers")
    public Api<List<PhotographerListResponseDto>> getPhotographerList(@RequestParam String region) {

        return Api.READ(photographerService.getPhotographerList(region));
    }

    @Schema(description = "사진작가 상세 정보 READ 요청")
    @GetMapping("/public/photographers/{photographerId}/detail")
    public Api<PhotographerSingleResponseDto> getPhotographer(@PathVariable String photographerId) {

        return Api.READ(photographerService.getPhotographer(photographerId));
    }
    
    
    @Schema(description = "사진작가 정보 UPDATE 요청 (이미지 미포함)",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @PatchMapping("/private/photographers/update")
    public Api<PhotographerSingleResponseDto> updatePhotographer(
            @RequestBody PhotographerUpdateDto photographerUpdateDto) {
        
        return Api.UPDATED(photographerService.updatePhotographer(photographerUpdateDto));
    }
    
    @Schema(description = "사진작가 정보 UPDATE 요청 (이미지 포함)",
            contentEncoding = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PatchMapping(value = "/private/photographers/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Api<PhotographerSingleResponseDto> updatePhotographer(
            @RequestPart(value = "data") PhotographerUpdateDto photographerUpdateDto,
            @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        
        return Api.UPDATED(photographerService.updatePhotographer(photographerUpdateDto, images));
    }
    
    
    
    @Schema(description = "사진작가 정보 DELETE 요청",
            contentEncoding = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping("/private/photographers/{photographerId}/delete")
    public Result deletePhotographer(@PathVariable String photographerId) {
        
        photographerService.deletePhotographer(photographerId);
        return Result.DELETED();
    }

    @PostMapping("/private/photographers/logout")
    public Api<?> logout(@RequestBody RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        // accountId null check는 validation 메소드 안에서 이미 진행
        String accountId = tokenService.validationTokenWithAccountId(refreshToken);

        // 소셜 로그인 및 로컬 로그인 모두 로그아웃 프로세스는 동일
        redisTemplate.opsForHash().delete("photographer_refresh_token", accountId);
        // 저장된 컨텍스트 안의 principal 삭제
        SecurityContextHolder.clearContext();

        return Api.OK("로그아웃되었습니다.");
    }

    @PostMapping("/private/photographers/refresh-token")
    public ResponseEntity<Api<Object>> refreshAccessToken(
            @RequestBody RefreshTokenRequest request
    ) {

        String accountId = tokenService.validationTokenWithAccountId(request.getRefreshToken());

        Token storedRefreshToken = (Token) redisTemplate.opsForHash().get("photographer_refresh_token", accountId);

        // 레디스에 리프레쉬 토큰이 저장되어 있는지 && 요청으로 보낸 토큰과 레디스에 저장된 토큰이 일치하는지
        if (storedRefreshToken != null && request.getRefreshToken().equals(storedRefreshToken.getRefreshToken())) {
            // 정상적인 경우 액세스 토큰 재발급
            TokenDto accessToken = tokenService.issuePhotographerAccessToken(accountId);
            return ResponseEntity.ok(Api.OK(accessToken));
        } else {
            // http status code를 명확히 하기 위해 ResponseEntity 사용
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Api.ERROR(400, "Invalid refresh token"));
            // 정상적이지 않은 경우 bad request
        }
    }
    
}
