package trendravel.photoravel_be.domain.review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.error.ErrorCode;
import trendravel.photoravel_be.commom.error.LocationErrorCode;
import trendravel.photoravel_be.commom.error.ReviewErrorCode;
import trendravel.photoravel_be.commom.error.SpotErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
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

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SpotRepository spotRepository;
    private final LocationRepository locationRepository;
    private final ImageService imageService;

    public ReviewResponseDto createReview(
            ReviewRequestDto reviewRequestDto, List<MultipartFile> images) {

        /**
         * 왜 orElseThrow에서 ()로 보내야지만 가능할까..? 고민해볼것.
         */
        Location location = null;
        if(reviewRequestDto.getReviewType().equals(ReviewTypes.LOCATION)){
            location = locationRepository.
                    findById(reviewRequestDto.getTypeId())
                    .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));
        }

        Spot spot = null;
        if(reviewRequestDto.getReviewType().equals(ReviewTypes.SPOT)){
            spot = spotRepository.findById(reviewRequestDto.getTypeId())
                    .orElseThrow(() -> new ApiException(SpotErrorCode.SPOT_NOT_FOUND));
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

        Location location = null;
        if(reviewRequestDto.getReviewType().equals(ReviewTypes.LOCATION)){
            location = locationRepository.
                    findById(reviewRequestDto.getTypeId())
                    .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));
        }

        Spot spot = null;
        if(reviewRequestDto.getReviewType().equals(ReviewTypes.SPOT)){
            spot = spotRepository.findById(reviewRequestDto.getTypeId())
                    .orElseThrow(() -> new ApiException(SpotErrorCode.SPOT_NOT_FOUND));
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
        List<Review> reviews = locationRepository.findById(locationId)
                .map(Location::getReview)
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));

        return reviews.stream()
                .map(p -> new ReviewResponseDto(p.getId(), p.getReviewType().toString(),
                        p.getContent(), p.getRating(), p.getImages(),
                        p.getCreatedAt(), p.getUpdatedAt()))
                .toList();
    }

    @Transactional
    public List<ReviewResponseDto> readAllSpotReview(Long locationId, Long spotId){
        List<Spot> spots = locationRepository.findById(locationId)
                .map(Location::getSpot)
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));

        Spot spot = spots.stream()
                .filter(s -> s.getId().equals(spotId)).findFirst()
                .orElseThrow(() -> new ApiException(SpotErrorCode.SPOT_NOT_FOUND));


        return spot.getReviews().stream()
                .map(p -> new ReviewResponseDto(p.getId(), p.getReviewType().toString(),
                        p.getContent(), p.getRating(), p.getImages(),
                        p.getCreatedAt(), p.getUpdatedAt()))
                .toList();
    }



    @Transactional
    public ReviewResponseDto updateReview(
            ReviewUpdateImagesDto reviewRequestDto, List<MultipartFile> images) {

        Review review = reviewRepository.findById(
                reviewRequestDto.getReviewId())
                .orElseThrow(() -> new ApiException(ReviewErrorCode.REVIEW_NOT_FOUND));

        review.updateReview(reviewRequestDto, imageService.updateImages(images,
                reviewRequestDto.getDeleteImages()));

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

    @Transactional
    public ReviewResponseDto updateReview(
            ReviewRequestDto reviewRequestDto) {

        Review review = reviewRepository.findById(
                reviewRequestDto.getReviewId())
                .orElseThrow(() -> new ApiException(ReviewErrorCode.REVIEW_NOT_FOUND));


        review.updateReview(reviewRequestDto);

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
    public void deleteReview(Long id){
        Review findReview = reviewRepository.findById(id)
                .orElseThrow(() -> new ApiException(ReviewErrorCode.REVIEW_NOT_FOUND));
        reviewRepository.deleteById(findReview.getId());
        imageService.deleteAllImages(findReview.getImages());
    }

}
