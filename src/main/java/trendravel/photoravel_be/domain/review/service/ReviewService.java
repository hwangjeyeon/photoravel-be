package trendravel.photoravel_be.domain.review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.domain.review.dto.request.ReviewRequestDto;
import trendravel.photoravel_be.domain.review.dto.response.ReviewResponseDto;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;
import trendravel.photoravel_be.db.respository.LocationRepository;
import trendravel.photoravel_be.db.respository.ReviewRepository;
import trendravel.photoravel_be.db.respository.SpotRepository;
import trendravel.photoravel_be.commom.service.ImageService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SpotRepository spotRepository;
    private final LocationRepository locationRepository;
    private final ImageService imageService;

    public ReviewResponseDto createReview(
            ReviewRequestDto reviewRequestDto, List<MultipartFile> images) {


        Review review = reviewRepository.save(Review.builder()
                .reviewType(reviewRequestDto.getReviewType())
                .images(imageService.uploadImages(images))
                .content(reviewRequestDto.getContent())
                .rating(reviewRequestDto.getRating())
                .locationReview(ReviewTypes.LOCATION ==
                        reviewRequestDto.getReviewType()
                        ? locationRepository.
                        findById(reviewRequestDto.getTypeId())
                        .orElse(null) : null)
                .spotReview(ReviewTypes.SPOT ==
                        reviewRequestDto.getReviewType()
                        ? spotRepository.findById(reviewRequestDto.getTypeId())
                        .orElse(null) : null)
                .build());


        return ReviewResponseDto
                .builder()
                .ReviewId(review.getId())
                .images(review.getImages())
                .rating(review.getRating())
                .content(review.getContent())
                .reviewType(review.getReviewType().toString())
                .createdTime(review.getCreatedAt())
                .updatedTime(review.getUpdatedAt())
                .build();
    }

    public ReviewResponseDto createReview(
            ReviewRequestDto reviewRequestDto) {


        Review review = reviewRepository.save(Review.builder()
                .reviewType(reviewRequestDto.getReviewType())
                .content(reviewRequestDto.getContent())
                .rating(reviewRequestDto.getRating())
                .locationReview(ReviewTypes.LOCATION ==
                        reviewRequestDto.getReviewType()
                        ? locationRepository.
                        findById(reviewRequestDto.getTypeId())
                        .orElse(null) : null)
                .spotReview(ReviewTypes.SPOT ==
                        reviewRequestDto.getReviewType()
                        ? spotRepository.findById(reviewRequestDto.getTypeId())
                        .orElse(null) : null)
                .build());


        return ReviewResponseDto
                .builder()
                .ReviewId(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .reviewType(review.getReviewType().toString())
                .createdTime(review.getCreatedAt())
                .updatedTime(review.getUpdatedAt())
                .build();
    }



    @Transactional
    public ReviewResponseDto updateReview(
            ReviewRequestDto reviewRequestDto, List<MultipartFile> images) {

        Optional<Review> review = reviewRepository.findById(
                reviewRequestDto.getReviewId());


        if(review.isEmpty()){
            // 추후 Exception Controller 만들어 처리할 계획
        }
        review.get().updateReview(reviewRequestDto, imageService.uploadImages(images));

        return ReviewResponseDto
                .builder()
                .ReviewId(review.get().getId())
                .images(review.get().getImages())
                .rating(review.get().getRating())
                .content(review.get().getContent())
                .reviewType(review.get().getReviewType().toString())
                .createdTime(review.get().getCreatedAt())
                .updatedTime(review.get().getUpdatedAt())
                .build();
    }

    @Transactional
    public ReviewResponseDto updateReview(
            ReviewRequestDto reviewRequestDto) {

        Optional<Review> review = reviewRepository.findById(
                reviewRequestDto.getReviewId());


        if(review.isEmpty()){
            // 추후 Exception Controller 만들어 처리할 계획
        }
        review.get().updateReview(reviewRequestDto);

        return ReviewResponseDto
                .builder()
                .ReviewId(review.get().getId())
                .rating(review.get().getRating())
                .content(review.get().getContent())
                .reviewType(review.get().getReviewType().toString())
                .createdTime(review.get().getCreatedAt())
                .updatedTime(review.get().getUpdatedAt())
                .build();
    }

    public void deleteReview(Long id){
        reviewRepository.deleteById(id);
    }

}
