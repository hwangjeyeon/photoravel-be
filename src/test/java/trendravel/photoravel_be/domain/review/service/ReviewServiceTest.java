package trendravel.photoravel_be.domain.review.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import trendravel.photoravel_be.commom.service.ImageService;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.db.respository.review.ReviewRepository;
import trendravel.photoravel_be.db.respository.spot.SpotRepository;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;
import trendravel.photoravel_be.db.spot.Spot;
import trendravel.photoravel_be.domain.review.dto.request.ReviewRequestDto;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class ReviewServiceTest {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    SpotRepository spotRepository;
    @Autowired
    ReviewRepository reviewRepository;

    @MockBean
    ReviewService reviewService;

    @MockBean
    ImageService imageService;

    ReviewRequestDto locationReviewRequestDto = new ReviewRequestDto();
    ReviewRequestDto spotReviewRequestDto = new ReviewRequestDto();
    Location location;
    Spot spot;
    Review locationReview;
    Review spotReview;

    @BeforeEach
    void before(){
        reviewService = new ReviewService(reviewRepository, spotRepository, locationRepository, imageService);
        location = Location
                .builder()
                .name("순천향대학교")
                .latitude(35.24)
                .longitude(46.61)
                .address("아산시 신창면 순천향로46")
                .description("순천향대학교입니다.")
                .views(0)
                .build();
        locationRepository.save(location);
        spot = Spot
                .builder()
                .description("미디어랩스관입니다")
                .longitude(35.24)
                .latitude(46.61)
                .title("미디어랩스건물 방문")
                .views(0)
                .location(location)
                .build();
        spotRepository.save(spot);
        locationReview = Review.builder()
                .id(1L)
                .reviewType(ReviewTypes.LOCATION)
                .content("우와 멋진 장소네요 ㅋㅋ")
                .rating(3.4)
                .locationReview(locationRepository.findById(1L).get())
                .build();
        spotReview = Review.builder()
                .id(2L)
                .reviewType(ReviewTypes.SPOT)
                .content("내 인생샷 장소임 ㄹㅇ")
                .rating(1.4)
                .spotReview(spotRepository.findById(1L).get())
                .build();
        reviewRepository.save(locationReview);
        reviewRepository.save(spotReview);

        locationReviewRequestDto.setReviewId(reviewRepository.findById(1L).get().getId());
        locationReviewRequestDto.setReviewType(locationReview.getReviewType());
        locationReviewRequestDto.setContent(locationReview.getContent());
        locationReviewRequestDto.setRating(locationReview.getRating());
        locationReviewRequestDto.setTypeId(locationReview.getLocationReview().getId());

        spotReviewRequestDto.setReviewId(reviewRepository.findById(2L).get().getId());
        spotReviewRequestDto.setReviewType(spotReview.getReviewType());
        spotReviewRequestDto.setRating(spotReview.getRating());
        spotReviewRequestDto.setContent(spotReview.getContent());
        spotReviewRequestDto.setTypeId(spotReview.getSpotReview().getId());
    }

    @Test
    @DisplayName("Location/Spot Review CREATE (이미지 미포함) Service가 잘 동작하는지 테스트")
    void createReviewTest (){
        reviewService.createReview(locationReviewRequestDto);
        reviewService.createReview(spotReviewRequestDto);

        Review findLocationReview = reviewRepository.findById(locationReviewRequestDto
                .getReviewId()).orElse(null);

        Review findSpotReview = reviewRepository.findById(spotReviewRequestDto
                .getReviewId()).orElse(null);

        assertThat(findLocationReview.getId())
                .isEqualTo(locationReviewRequestDto.getReviewId());
        assertThat(findLocationReview.getReviewType())
                .isEqualTo(locationReviewRequestDto.getReviewType());
        assertThat(findLocationReview.getContent())
                .isEqualTo(locationReviewRequestDto.getContent());
        assertThat(findLocationReview.getRating())
                .isEqualTo(locationReviewRequestDto.getRating());
        assertThat(findLocationReview.getLocationReview().getId())
                .isEqualTo(locationReviewRequestDto.getTypeId());

        assertThat(findSpotReview.getId())
                .isEqualTo(spotReviewRequestDto.getReviewId());
        assertThat(findSpotReview.getReviewType())
                .isEqualTo(spotReviewRequestDto.getReviewType());
        assertThat(findSpotReview.getContent())
                .isEqualTo(spotReviewRequestDto.getContent());
        assertThat(findSpotReview.getRating())
                .isEqualTo(spotReviewRequestDto.getRating());
        assertThat(findSpotReview.getSpotReview().getId())
                .isEqualTo(spotReviewRequestDto.getTypeId());
    }

    @Test
    @DisplayName("Location/Spot Review UPDATE (이미지 미포함) Service가 잘 동작하는지 테스트")
    void updateReviewTest (){
        reviewService.createReview(locationReviewRequestDto);
        reviewService.createReview(spotReviewRequestDto);
        locationReviewRequestDto.setRating(2.1);
        spotReviewRequestDto.setRating(1.0);
        reviewService.updateReview(locationReviewRequestDto);
        reviewService.updateReview(spotReviewRequestDto);

        Review findLocationReview = reviewRepository.findById(locationReviewRequestDto
                .getReviewId()).orElse(null);

        Review findSpotReview = reviewRepository.findById(spotReviewRequestDto
                .getReviewId()).orElse(null);

        assertThat(findLocationReview.getId())
                .isEqualTo(locationReviewRequestDto.getReviewId());
        assertThat(findLocationReview.getReviewType())
                .isEqualTo(locationReviewRequestDto.getReviewType());
        assertThat(findLocationReview.getContent())
                .isEqualTo(locationReviewRequestDto.getContent());
        assertThat(findLocationReview.getRating())
                .isEqualTo(locationReviewRequestDto.getRating());
        assertThat(findLocationReview.getLocationReview().getId())
                .isEqualTo(locationReviewRequestDto.getTypeId());

        assertThat(findSpotReview.getId())
                .isEqualTo(spotReviewRequestDto.getReviewId());
        assertThat(findSpotReview.getReviewType())
                .isEqualTo(spotReviewRequestDto.getReviewType());
        assertThat(findSpotReview.getContent())
                .isEqualTo(spotReviewRequestDto.getContent());
        assertThat(findSpotReview.getRating())
                .isEqualTo(spotReviewRequestDto.getRating());
        assertThat(findSpotReview.getSpotReview().getId())
                .isEqualTo(spotReviewRequestDto.getTypeId());
    }


    @Test
    @DisplayName("Location/Spot Review DELETE Service가 잘 동작하는지 테스트")
    void deleteReviewTest (){
        reviewService.createReview(locationReviewRequestDto);
        reviewService.createReview(spotReviewRequestDto);
        reviewService.deleteReview(1L);
        reviewService.deleteReview(2L);

        Review findLocationReview = reviewRepository.findById(locationReviewRequestDto
                .getReviewId()).orElse(null);

        Review findSpotReview = reviewRepository.findById(spotReviewRequestDto
                .getReviewId()).orElse(null);

        assertThat(findLocationReview).isNull();
        assertThat(findSpotReview).isNull();

    }





}