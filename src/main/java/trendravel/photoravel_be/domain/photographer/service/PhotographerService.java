package trendravel.photoravel_be.domain.photographer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.error.MemberErrorCode;
import trendravel.photoravel_be.commom.error.PhotographerErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.commom.image.service.ImageServiceFacade;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.inmemorydb.entity.Token;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.db.respository.photographer.PhotographerRepository;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.authentication.service.PhotographerAuthenticationService;
import trendravel.photoravel_be.domain.authentication.session.PhotographerSession;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerRequestDto;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerUpdateDto;
import trendravel.photoravel_be.domain.photographer.dto.response.PhotographerListResponseDto;
import trendravel.photoravel_be.domain.photographer.dto.response.PhotographerSingleResponseDto;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;
import trendravel.photoravel_be.domain.token.model.TokenDto;
import trendravel.photoravel_be.domain.token.model.TokenResponse;
import trendravel.photoravel_be.domain.token.service.TokenService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotographerService {
    
    private final PhotographerRepository photographerRepository;
    private final ImageServiceFacade imageServiceFacade;
    private final PasswordEncoder photographerBCryptPasswordEncoder;
    private final TokenService tokenService;
    private final RedisTemplate<String, Token> redisTemplate;
    private final PhotographerAuthenticationService photographerAuthenticationService;
    
    @Transactional
    public List<PhotographerListResponseDto> getPhotographerList(String region) {
        
        List<Photographer> photographers;
        
        if (region.equals("count")) {
            photographers = photographerRepository.getPhotographerByMatchingCount();
        }  else if (region.equals("career")){
            photographers = photographerRepository.getPhotographerByCareer();
        } else { // 지역으로 검색
            photographers = photographerRepository.getPhotographerByRegion(Region.valueOf(region));
        }
        
        if (photographers.isEmpty()) {
            throw new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND);
        }
        
        return photographers.stream()
                .map(photographer -> PhotographerListResponseDto.builder()
                        .accountId(photographer.getAccountId())
                        .name(photographer.getName())
                        .profileImg(photographer.getProfileImg())
                        .region(photographer.getRegion())
                        .description(photographer.getDescription())
                        .createdAt(photographer.getCreatedAt())
                        .updatedAt(photographer.getUpdatedAt())
                        .ratingAvg(String.format("%.2f", ratingAverage(photographer.getReviews())))
                        .reviewCount(photographer.getReviews().size())
                        .careerYear(photographer.getCareerYear())
                        .matchingCount(photographer.getMatchingCount())
                        .build())
                .collect(Collectors.toList());
    }
    
    @Transactional
    public PhotographerSingleResponseDto getPhotographer(String photographerId) {
        
        Photographer photographer = photographerRepository.findByAccountId(photographerId).orElseThrow(() ->
                new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));
        
        List<RecentReviewsDto> reviews = photographerRepository.recentReviews(photographer.getId());
        
        return PhotographerSingleResponseDto.builder()
                .id(photographer.getId())
                .accountId(photographer.getAccountId())
                .name(photographer.getName())
                .region(photographer.getRegion())
                .description(photographer.getDescription())
                .profileImg(photographer.getProfileImg())
                .recentReviewDtos(reviews)
                .createdAt(photographer.getCreatedAt())
                .updatedAt(photographer.getUpdatedAt())
                .ratingAvg(String.format("%.2f", ratingAverage(photographer.getReviews())))
                .careerYear(photographer.getCareerYear())
                .matchingCount(photographer.getMatchingCount())
                .build();
    }
    
    
    /*
    이미지 타입 변경
     */
    
    @Transactional
    public void createPhotographer(PhotographerRequestDto photographerRequestDto, List<MultipartFile> images) {
        String profileImgUrl;
        if (images == null || images.isEmpty()) {
            profileImgUrl = "https://photravle-images.shop/default_profile.png";
        } else {
            profileImgUrl = imageServiceFacade.uploadImageFacade(images).get(0);
        }

        photographerRepository.save(Photographer.builder()
                .accountId(photographerRequestDto.getAccountId())
                .password(photographerBCryptPasswordEncoder.encode(photographerRequestDto.getPassword()))
                .name(photographerRequestDto.getName())
                .region(photographerRequestDto.getRegion())
                .description(photographerRequestDto.getDescription())
                .profileImg(profileImgUrl)
                .careerYear(photographerRequestDto.getCareerYear())
                .matchingCount(0)
                .build());
    }
    
    @Transactional
    public TokenResponse authenticate(String username, String password) {
        
        Photographer photographer = photographerRepository.findByAccountId(username).orElseThrow(() ->
                new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));

        if (photographerBCryptPasswordEncoder.matches(password, photographer.getPassword())) {
            return issueTokenResponse(photographer);
        } else {
            throw new ApiException(MemberErrorCode.PASSWORD_NOT_MATCH);
        }

    }
    
    @Transactional
    public PhotographerSingleResponseDto updatePhotographer(PhotographerUpdateDto photographerUpdateDto,
                                                            List<MultipartFile> images) {

        PhotographerSession principal = (PhotographerSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Photographer photographer = photographerRepository.findByAccountId(principal.getUsername())
                .orElseThrow(() -> new ApiException(PhotographerErrorCode.UNAUTHORIZED));

        redisTemplate.opsForHash().delete("photographer_refresh_token", photographer.getAccountId());

        //기존 이미지 삭제
        List<String> originImage = new ArrayList<>();
        originImage.add(photographer.getProfileImg());
        imageServiceFacade.deleteAllImagesFacade(originImage);

        photographer.updatePhotographer(photographerUpdateDto, imageServiceFacade.uploadImageFacade(images));

        List<RecentReviewsDto> reviews = photographerRepository.recentReviews(photographer.getId());

        TokenDto accessToken = tokenService.issuePhotographerAccessToken(photographer.getAccountId());
        TokenDto refreshToken = tokenService.issuePhotographerRefreshToken(photographer.getAccountId());

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // 사진작가 정보 수정 후 UserDetails를 업데이트
        PhotographerSession photographerSession = (PhotographerSession) photographerAuthenticationService.loadUserByUsername(photographerUpdateDto.getAccountId());
        Authentication authentication = new UsernamePasswordAuthenticationToken(photographerSession, null, photographerSession.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return PhotographerSingleResponseDto.builder()
                .accountId(photographer.getAccountId())
                .name(photographer.getName())
                .region(photographer.getRegion())
                .description(photographer.getDescription())
                .profileImg(photographer.getProfileImg())
                .createdAt(photographer.getCreatedAt())
                .updatedAt(photographer.getUpdatedAt())
                .ratingAvg(String.format("%.2f", ratingAverage(photographer.getReviews())))
                .recentReviewDtos(reviews)
                .careerYear(photographer.getCareerYear())
                .tokenResponse(tokenResponse)
                .build();
    }
    
    @Transactional
    public PhotographerSingleResponseDto updatePhotographer(PhotographerUpdateDto photographerUpdateDto) {

        PhotographerSession principal = (PhotographerSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Photographer photographer = photographerRepository.findByAccountId(principal.getUsername())
                .orElseThrow(() -> new ApiException(PhotographerErrorCode.UNAUTHORIZED));
        redisTemplate.opsForHash().delete("photographer_refresh_token", photographer.getAccountId());

        photographer.updatePhotographer(photographerUpdateDto);
        List<RecentReviewsDto> reviews = photographerRepository.recentReviews(photographer.getId());

        TokenDto accessToken = tokenService.issuePhotographerAccessToken(photographer.getAccountId());
        TokenDto refreshToken = tokenService.issuePhotographerRefreshToken(photographer.getAccountId());

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // 사진작가 정보 수정 후 UserDetails를 업데이트
        PhotographerSession photographerSession = (PhotographerSession) photographerAuthenticationService.loadUserByUsername(photographer.getAccountId());
        Authentication authentication = new UsernamePasswordAuthenticationToken(photographerSession, null, photographerSession.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return PhotographerSingleResponseDto.builder()
                // id는 만들어 놓고 안 넣으신 이유가 무엇인지..?
                .accountId(photographer.getAccountId())
                .name(photographer.getName())
                .region(photographer.getRegion())
                .description(photographer.getDescription())
                .profileImg(photographer.getProfileImg())
                .createdAt(photographer.getCreatedAt())
                .updatedAt(photographer.getUpdatedAt())
                .ratingAvg(String.format("%.2f", ratingAverage(photographer.getReviews())))
                .recentReviewDtos(reviews)
                .careerYear(photographer.getCareerYear())
                .tokenResponse(tokenResponse)
                .build();
    }
    
    @Transactional
    public void deletePhotographer(String photographerId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PhotographerSession principal = (PhotographerSession) authentication.getPrincipal();

        Photographer photographer = photographerRepository.findByAccountId(principal.getUsername())
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));

        if (photographer.getAccountId().equals(photographerId)){
            List<String> img = new ArrayList<>();
            img.add(photographer.getProfileImg());
            imageServiceFacade.deleteAllImagesFacade(img);
            photographerRepository.deleteByAccountId(photographerId);
            redisTemplate.opsForHash().delete("photographer_refresh_token", photographerId);
            SecurityContextHolder.clearContext();
        }else {
            throw new ApiException(MemberErrorCode.UNAUTHORIZED);
        }

        
        /*
        단일 이미지 삭제 로직 구현 필요
        if (!photographer.getProfileImg().isEmpty()) {
            imageService.deleteAllImages();
        }
        */
        
    }
    
    private double ratingAverage(List<Review> reviews) {
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / reviews.size();
    }

    private TokenResponse issueTokenResponse(Photographer photographer) {
        TokenDto accessTokenDto = tokenService.issuePhotographerAccessToken(photographer.getAccountId());
        TokenDto refreshTokenDto = tokenService.issuePhotographerRefreshToken(photographer.getAccountId());

        return TokenResponse.builder()
                .accessToken(accessTokenDto)
                .refreshToken(refreshTokenDto)
                .build();
    }
    
    
}
