package trendravel.photoravel_be.domain.guide.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.error.ErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.commom.service.ImageService;
import trendravel.photoravel_be.db.guide.Guide;
import trendravel.photoravel_be.db.respository.guide.GuideRepository;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.guide.dto.request.GuideRequestDto;
import trendravel.photoravel_be.domain.guide.dto.response.GuideResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuideService {
    
    private final GuideRepository guideRepository;
    private final ImageService imageService;
    
    @Transactional
    public List<GuideResponseDto> getGuideList(String keyword) {
        
        List<Guide> guides = guideRepository.findByNameContaining(keyword);
        
        return guides.stream()
                .map(guide -> GuideResponseDto.builder()
                        .accountId(guide.getAccountId())
                        .region(guide.getRegion())
                        .ratingAvg(String.format("%.2f", ratingAverage(guide.getReviews())))
                        .reviewCount(guide.getReviews().size())
                        .build())
                .collect(Collectors.toList());
    }
    
    @Transactional
    public GuideResponseDto getGuide(String guideId) {
        
        Optional<Guide> guideOpt = guideRepository.findByAccountId(guideId);
        if (guideOpt.isEmpty()) {
            
        }
        Guide guide = guideOpt.get();
        
        //List<RecentReviewsDto> reviews = guideRepository.recentReviews(guide.getId());
        
        return GuideResponseDto.builder()
                .accountId(guide.getAccountId())
                .name(guide.getName())
                .region(guide.getRegion())
                .description(guide.getDescription())
                .profileImg(guide.getProfileImg())
                .createdAt(guide.getCreatedAt())
                .updatedAt(guide.getUpdatedAt())
                .ratingAvg(String.format("%.2f", ratingAverage(guide.getReviews())))
                //.recentReviewDtos(reviews)
                .build();
    }
    
    
    /*
    이미지 타입 변경
     */
    
    @Transactional
    public void createGuide(GuideRequestDto guideRequestDto, List<MultipartFile> images) {
        
        guideRepository.save(Guide.builder()
                .accountId(guideRequestDto.getAccountId())
                .password(guideRequestDto.getPassword())
                .name(guideRequestDto.getName())
                .region(guideRequestDto.getRegion())
                .description(guideRequestDto.getDescription())
                //이미지 업로드 처리는 List이고 엔티티는 문자열이기에 get(0)으로 처리 
                .profileImg(imageService.uploadImages(images).get(0))
                .build());
    }
    
    @Transactional
    public void authenticate(String username, String password) {
        
        //인증 로직 필요
        Optional<Guide> guideOpt = guideRepository.findByAccountId(username);
        
        if (guideOpt.isPresent()) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
    }
    
    @Transactional
    public GuideResponseDto updateGuide(GuideRequestDto guideRequestDto,
                                        List<MultipartFile> images) {
        
        Optional<Guide> guideOpt = guideRepository.findByAccountId(guideRequestDto.getAccountId());
        
        if (guideOpt.isEmpty()) {
            
        }
        
        Guide guide = guideOpt.get();
        guide.updateGuide(guideRequestDto, imageService.uploadImages(images));
        
        //List<RecentReviewsDto> reviews = guideRepository.recentReviews(guide.getId());
        
        return GuideResponseDto.builder()
                .accountId(guide.getAccountId())
                .name(guide.getName())
                .region(guide.getRegion())
                .description(guide.getDescription())
                .profileImg(guide.getProfileImg())
                .createdAt(guide.getCreatedAt())
                .updatedAt(guide.getUpdatedAt())
                .ratingAvg(String.format("%.2f", ratingAverage(guide.getReviews())))
                //.recentReviewDtos(reviews)
                .build();
    }
    
    @Transactional
    public GuideResponseDto updateGuide(GuideRequestDto guideRequestDto) {
        
        Optional<Guide> guideOpt = guideRepository.findByAccountId(guideRequestDto.getAccountId());
        
        if (guideOpt.isEmpty()) {
            
        }
        
        Guide guide = guideOpt.get();
        guide.updateGuide(guideRequestDto);
        
        //List<RecentReviewsDto> reviews = guideRepository.recentReviews(guide.getId());
        
        return GuideResponseDto.builder()
                .accountId(guide.getAccountId())
                .name(guide.getName())
                .region(guide.getRegion())
                .description(guide.getDescription())
                .profileImg(guide.getProfileImg())
                .createdAt(guide.getCreatedAt())
                .updatedAt(guide.getUpdatedAt())
                .ratingAvg(String.format("%.2f", ratingAverage(guide.getReviews())))
                //.recentReviewDtos(reviews)
                .build();
    }
    
    
    @Transactional
    public void deleteGuide(String guideId) {
        guideRepository.deleteByAccountId(guideId);
    }
    
    private double ratingAverage(List<Review> reviews) {
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / reviews.size();
    }
    
    
}
