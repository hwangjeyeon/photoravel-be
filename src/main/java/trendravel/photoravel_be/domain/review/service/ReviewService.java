package trendravel.photoravel_be.domain.review.service;


import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.common.exception.ApiException;
import trendravel.photoravel_be.common.exception.error.*;
import trendravel.photoravel_be.common.image.service.ImageServiceFacade;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.db.respository.photographer.PhotographerRepository;
import trendravel.photoravel_be.db.spot.Spot;
import trendravel.photoravel_be.domain.review.dto.request.ReviewRequestDto;
import trendravel.photoravel_be.domain.review.dto.request.ReviewUpdateImagesDto;
import trendravel.photoravel_be.domain.review.dto.response.ReviewResponseDto;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.db.respository.review.ReviewRepository;
import trendravel.photoravel_be.db.respository.spot.SpotRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SpotRepository spotRepository;
    private final LocationRepository locationRepository;
    private final PhotographerRepository photographerRepository;
    private final MemberRepository memberRepository;
    private final ImageServiceFacade imageServiceFacade;

    @Transactional
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
        
        Photographer photographer = null;
        if(reviewRequestDto.getReviewType().equals(ReviewTypes.PHOTOGRAPHER)){
            photographer = photographerRepository.findById(reviewRequestDto.getTypeId())
                    .orElseThrow(() -> new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));
        }

        MemberEntity member = memberRepository.findByMemberId(reviewRequestDto.getUserId())
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));
        

        Review review = Review.builder()
                .reviewType(reviewRequestDto.getReviewType())
                .content(reviewRequestDto.getContent())
                .rating(reviewRequestDto.getRating())
                .locationReview(ReviewTypes.LOCATION ==
                        reviewRequestDto.getReviewType()
                        ? location : null)
                .spotReview(ReviewTypes.SPOT ==
                        reviewRequestDto.getReviewType()
                        ? spot : null)
                .photographerReview(ReviewTypes.PHOTOGRAPHER ==
                        reviewRequestDto.getReviewType()
                        ? photographer : null)
                .images(imageServiceFacade.uploadImageFacade(images))
                .member(member)
                .build();

        if (ReviewTypes.LOCATION == reviewRequestDto.getReviewType()) {
            review.setLocationReview(location);
        } else if (ReviewTypes.SPOT == reviewRequestDto.getReviewType()) {
            review.setSpotReview(spot);
        } else if (ReviewTypes.PHOTOGRAPHER == reviewRequestDto.getReviewType()) {
            review.setPhotographerReview(photographer);
        }
        reviewRepository.save(review);
        
        return ReviewResponseDto
                .builder()
                .ReviewId(review.getId())
                .images(review.getImages())
                .rating(review.getRating())
                .content(review.getContent())
                .reviewType(review.getReviewType().toString())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .userName(member.getNickname())
                .build();
    }

    @Transactional
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
        
        Photographer photographer = null;
        if(reviewRequestDto.getReviewType().equals(ReviewTypes.PHOTOGRAPHER)){
            photographer = photographerRepository.findById(reviewRequestDto.getTypeId())
                    .orElseThrow(() -> new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));
        }

        MemberEntity member = memberRepository.findByMemberId(reviewRequestDto.getUserId())
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));
        
        Review review = Review.builder()
                .reviewType(reviewRequestDto.getReviewType())
                .content(reviewRequestDto.getContent())
                .rating(reviewRequestDto.getRating())
                .locationReview(ReviewTypes.LOCATION ==
                        reviewRequestDto.getReviewType()
                        ? location : null)
                .spotReview(ReviewTypes.SPOT ==
                        reviewRequestDto.getReviewType()
                        ? spot : null)
                .photographerReview(ReviewTypes.PHOTOGRAPHER ==
                        reviewRequestDto.getReviewType()
                        ? photographer : null)
                .member(member)
                .build();


        /**
         * 리팩토링 필요.
         */
        if (ReviewTypes.LOCATION == reviewRequestDto.getReviewType()) {
            review.setLocationReview(location);
        } else if (ReviewTypes.SPOT == reviewRequestDto.getReviewType()) {
            review.setSpotReview(spot);
        } else if (ReviewTypes.PHOTOGRAPHER == reviewRequestDto.getReviewType()) {
            review.setPhotographerReview(photographer);
        }

        reviewRepository.save(review);

        return ReviewResponseDto
                .builder()
                .ReviewId(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .reviewType(review.getReviewType().toString())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .userName(member.getNickname())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> readAllLocationReview(Long locationId){
        List<Review> reviews = locationRepository.findById(locationId)
                .map(Location::getReview)
                .orElseThrow(() -> new ApiException(LocationErrorCode.LOCATION_NOT_FOUND));

        return reviews.stream()
                .map(p -> new ReviewResponseDto(p.getId(), p.getReviewType().toString(),
                        p.getContent(), p.getRating(), p.getImages(),
                        p.getMember().getNickname(),
                        p.getCreatedAt(), p.getUpdatedAt()))
                .toList();
    }

    @Transactional(readOnly = true)
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
                        p.getMember().getNickname(),
                        p.getCreatedAt(), p.getUpdatedAt()))
                .toList();
    }
    @Transactional
    public List<ReviewResponseDto> readAllPhotographerReview(String photographerId) {
        List<Review> reviews = photographerRepository.findByAccountId(photographerId).map(Photographer::getReviews)
                .orElseThrow(() -> new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));
        
        return reviews.stream()
                .map(p -> new ReviewResponseDto(p.getId(), p.getReviewType().toString(),
                        p.getContent(), p.getRating(), p.getImages(),
                        p.getMember().getNickname(),
                        p.getCreatedAt(), p.getUpdatedAt()))
                .toList();
    }

    @Transactional
    public ReviewResponseDto updateReview(
            ReviewUpdateImagesDto reviewRequestDto, List<MultipartFile> images) {

        Review review = reviewRepository.findById(
                reviewRequestDto.getReviewId())
                .orElseThrow(() -> new ApiException(ReviewErrorCode.REVIEW_NOT_FOUND));

        review.updateReview(reviewRequestDto, imageServiceFacade.updateImageFacade(images,
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
                .userName(review.getMember().getNickname())
                .build();
    }

    @Transactional
    public ReviewResponseDto updateReview(
            ReviewUpdateImagesDto reviewRequestDto) {

        Review review = reviewRepository.findById(
                reviewRequestDto.getReviewId())
                .orElseThrow(() -> new ApiException(ReviewErrorCode.REVIEW_NOT_FOUND));


        review.updateReview(reviewRequestDto);
        imageServiceFacade.updateImageFacade(new ArrayList<>(), reviewRequestDto.getDeleteImages());

        return ReviewResponseDto
                .builder()
                .ReviewId(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .reviewType(review.getReviewType().toString())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .userName(review.getMember().getNickname())
                .images(review.getImages())
                .build();
    }

    @Transactional
    public void deleteReview(Long id){
        Review findReview = reviewRepository.findById(id)
                .orElseThrow(() -> new ApiException(ReviewErrorCode.REVIEW_NOT_FOUND));
        imageServiceFacade.deleteAllImagesFacade(findReview.getImages());
        reviewRepository.deleteById(findReview.getId());
    }
    
    
}
