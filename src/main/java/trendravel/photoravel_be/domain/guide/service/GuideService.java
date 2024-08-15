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

@Service
@RequiredArgsConstructor
public class GuideService {
    
    private final GuideRepository guideRepository;
    private final ImageService imageService;
    
    @Transactional
    public List<GuideResponseDto> getGuideList(String keyword) {
        
        List<Guide> guides = guideRepository.findByNameContaining(keyword);
        
        List<GuideResponseDto> responseDtoList = new ArrayList<>();
        
        for (Guide guide : guides) {
            GuideResponseDto dto = GuideResponseDto.builder()
                    .accountId(guide.getAccountId())
                    .region(guide.getRegion())
                    .ratingAvg(String.format("%.2f", ratingAverage(guide.getReviews())))
                    .reviewCount(guide.getReviews().size())
                    .build();
            
            responseDtoList.add(dto);
        }
        
        return responseDtoList;
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
                .ratingAvg(String.format("%.2f", ratingAverage(guide.getReviews())))
                //.recentReviewDtos(reviews)
                .build();
    }
    
    @Transactional
    public void createGuide(GuideRequestDto guideRequestDto, String image) {
        
        Guide guide = guideRepository.save(Guide.builder()
                        .accountId(guideRequestDto.getAccountId())
                        .password(guideRequestDto.getPassword())
                        .name(guideRequestDto.getName())
                        .region(guideRequestDto.getRegion())
                        .description(guideRequestDto.getDescription())
                        .profileImg(guideRequestDto.getProfileImg())
                        .build());
        
    }
    @Transactional
    public GuideResponseDto authenticate(String username, String password) {
        
        //인증 로직 필요
        Optional<Guide> guideOpt = guideRepository.findByAccountId(username);
        
        if (guideOpt.isPresent()) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
        
        return GuideResponseDto.builder()
                .name(guideOpt.get().getName())
                .build();
    }
    
    @Transactional
    public GuideResponseDto updateGuide(String accountId,
                                        GuideRequestDto guideRequestDto,
                                        List<MultipartFile> images) {
        
        Optional<Guide> guideOpt = guideRepository.findByAccountId(accountId);
        
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
