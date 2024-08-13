package trendravel.photoravel_be.domain.review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.image.service.ImageService;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.spot.Spot;
import trendravel.photoravel_be.domain.review.dto.request.ReviewRequestDto;
import trendravel.photoravel_be.domain.review.dto.request.ReviewUpdateImagesDto;
import trendravel.photoravel_be.domain.review.dto.response.ReviewResponseDto;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.db.respository.review.ReviewRepository;
import trendravel.photoravel_be.db.respository.spot.SpotRepository;

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

        Location location = locationRepository.
                findById(reviewRequestDto.getTypeId())
                .orElse(null);

        Spot spot = spotRepository.findById(reviewRequestDto.getTypeId())
                .orElse(null);

        if(location == null && spot == null){
            // 예외처리 로직 추가 필요
        }

        Review review = reviewRepository.save(Review.builder()
                .reviewType(reviewRequestDto.getReviewType())
                .images(imageService.uploadImages(images))
                .content(reviewRequestDto.getContent())
                .rating(reviewRequestDto.getRating())
                .locationReview(ReviewTypes.LOCATION ==
                        reviewRequestDto.getReviewType()
                        ? location : null)
                .spotReview(ReviewTypes.SPOT ==
                        reviewRequestDto.getReviewType()
                        ? spot : null)
                .build());
        review.setLocationReview(location);
        review.setSpotReview(spot);

        return ReviewResponseDto
                .builder()
                .ReviewId(review.getId())
                .images(review.getImages())
                .rating(review.getRating())
                .content(review.getContent())
                .reviewType(review.getReviewType().toString())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public ReviewResponseDto createReview(
            ReviewRequestDto reviewRequestDto) {

        Location location = locationRepository.
                findById(reviewRequestDto.getTypeId())
                .orElse(null);

        Spot spot = spotRepository.findById(reviewRequestDto.getTypeId())
                .orElse(null);

        if(location == null && spot == null){
            // 예외처리 로직 추가 필요
        }


        Review review = reviewRepository.save(Review.builder()
                .reviewType(reviewRequestDto.getReviewType())
                .content(reviewRequestDto.getContent())
                .rating(reviewRequestDto.getRating())
                .locationReview(ReviewTypes.LOCATION ==
                        reviewRequestDto.getReviewType()
                        ? location : null)
                .spotReview(ReviewTypes.SPOT ==
                        reviewRequestDto.getReviewType()
                        ? spot : null)
                .build());


        /**
         * 리팩토링 필요.
         */
        if(review.getLocationReview() == null){
            review.setSpotReview(spot);
        }else if(review.getSpotReview() == null){
            review.setLocationReview(location);
        }




        return ReviewResponseDto
                .builder()
                .ReviewId(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .reviewType(review.getReviewType().toString())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    @Transactional
    public List<ReviewResponseDto> readAllLocationReview(Long locationId){
        List<Review> reviews = locationRepository.findById(locationId).get().getReview();


        return reviews.stream()
                .map(p -> new ReviewResponseDto(p.getId(), p.getReviewType().toString(),
                        p.getContent(), p.getRating(), p.getImages(),
                        p.getCreatedAt(), p.getUpdatedAt()))
                .toList();
    }

    @Transactional
    public List<ReviewResponseDto> readAllSpotReview(Long locationId, Long spotId){
        List<Spot> spots = locationRepository.findById(locationId).get().getSpot();

        if(spots.isEmpty()){
            // 예외 처리 로직
        }

        Spot spot = spots.stream()
                .filter(s -> s.getId().equals(spotId)).findFirst().get();

        return spot.getReviews().stream()
                .map(p -> new ReviewResponseDto(p.getId(), p.getReviewType().toString(),
                        p.getContent(), p.getRating(), p.getImages(),
                        p.getCreatedAt(), p.getUpdatedAt()))
                .toList();
    }



    @Transactional
    public ReviewResponseDto updateReview(
            ReviewUpdateImagesDto reviewRequestDto, List<MultipartFile> images) {

        Optional<Review> review = reviewRepository.findById(
                reviewRequestDto.getReviewId());


        if(review.isEmpty()){
            // 추후 Exception Controller 만들어 처리할 계획
        }
        review.get().updateReview(reviewRequestDto, imageService.updateImages(images,
                reviewRequestDto.getDeleteImages()));

        return ReviewResponseDto
                .builder()
                .ReviewId(review.get().getId())
                .images(review.get().getImages())
                .rating(review.get().getRating())
                .content(review.get().getContent())
                .reviewType(review.get().getReviewType().toString())
                .createdAt(review.get().getCreatedAt())
                .updatedAt(review.get().getUpdatedAt())
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
                .createdAt(review.get().getCreatedAt())
                .updatedAt(review.get().getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteReview(Long id){
        imageService.deleteAllImages(reviewRepository.findById(id).get().getImages());
        reviewRepository.deleteById(id);
    }

}
