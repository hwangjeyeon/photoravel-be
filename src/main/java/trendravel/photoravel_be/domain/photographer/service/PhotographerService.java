package trendravel.photoravel_be.domain.photographer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.error.PhotographerErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.commom.image.service.ImageServiceFacade;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.db.respository.photographer.PhotographerRepository;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerRequestDto;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerUpdateDto;
import trendravel.photoravel_be.domain.photographer.dto.response.PhotographerListResponseDto;
import trendravel.photoravel_be.domain.photographer.dto.response.PhotographerSingleResponseDto;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotographerService {
    
    private final PhotographerRepository photographerRepository;
    private final ImageServiceFacade imageServiceFacade;
    
    @Transactional
    public List<PhotographerListResponseDto> getPhotographerList(String region) {
        
        List<Photographer> photographers;
        
        if (region.equals("all")) {
            photographers = photographerRepository.findAll();
        } else { // 지역으로 검색
            photographers = photographerRepository.findByRegion(Region.valueOf(region));
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
                .build();
    }
    
    
    /*
    이미지 타입 변경
     */
    
    @Transactional
    public void createPhotographer(PhotographerRequestDto photographerRequestDto, List<MultipartFile> images) {
        
        photographerRepository.save(Photographer.builder()
                .accountId(photographerRequestDto.getAccountId())
                .password(photographerRequestDto.getPassword())
                .name(photographerRequestDto.getName())
                .region(photographerRequestDto.getRegion())
                .description(photographerRequestDto.getDescription())
                //이미지 업로드 처리는 List이고 엔티티는 문자열이기에 get(0)으로 처리 
                .profileImg(imageServiceFacade.uploadImageFacade(images).get(0))
                .build());
    }
    
    @Transactional
    public void authenticate(String username, String password) {
        
        Photographer photographer = photographerRepository.findByAccountId(username).orElseThrow(() ->
                new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));
        
        //인증 로직 필요
        
    }
    
    @Transactional
    public PhotographerSingleResponseDto updatePhotographer(PhotographerUpdateDto photographerUpdateDto,
                                                          List<MultipartFile> images) {
        
        Photographer photographer = photographerRepository.findByAccountId(photographerUpdateDto.getAccountId()).orElseThrow(
                () -> new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));
        
        //기존 이미지 삭제
        List<String> originImage = new ArrayList<>();
        originImage.add(photographer.getProfileImg());
        imageServiceFacade.deleteAllImagesFacade(originImage);
        
        photographer.updatePhotographer(photographerUpdateDto, imageServiceFacade.uploadImageFacade(images));
        
        //List<RecentReviewsDto> reviews = guideRepository.recentReviews(guide.getId());
        
        return PhotographerSingleResponseDto.builder()
                .accountId(photographer.getAccountId())
                .name(photographer.getName())
                .region(photographer.getRegion())
                .description(photographer.getDescription())
                .profileImg(photographer.getProfileImg())
                .createdAt(photographer.getCreatedAt())
                .updatedAt(photographer.getUpdatedAt())
                .ratingAvg(String.format("%.2f", ratingAverage(photographer.getReviews())))
                //.recentReviewDtos(reviews)
                .build();
    }
    
    @Transactional
    public PhotographerSingleResponseDto updatePhotographer(PhotographerUpdateDto photographerUpdateDto) {
        
        Photographer photographer = photographerRepository.findByAccountId(photographerUpdateDto.getAccountId()).orElseThrow(
                () -> new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));
        
        photographer.updatePhotographer(photographerUpdateDto);
        
        //List<RecentReviewsDto> reviews = guideRepository.recentReviews(guide.getId());
        
        return PhotographerSingleResponseDto.builder()
                .accountId(photographer.getAccountId())
                .name(photographer.getName())
                .region(photographer.getRegion())
                .description(photographer.getDescription())
                .profileImg(photographer.getProfileImg())
                .createdAt(photographer.getCreatedAt())
                .updatedAt(photographer.getUpdatedAt())
                .ratingAvg(String.format("%.2f", ratingAverage(photographer.getReviews())))
                //.recentReviewDtos(reviews)
                .build();
    }
    
    @Transactional
    public void deletePhotographer(String photographerId) {
        
        Photographer photographer = photographerRepository.findByAccountId(photographerId).orElseThrow(
                () -> new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));
        
        photographerRepository.deleteByAccountId(photographerId);
        
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
    
    
}
