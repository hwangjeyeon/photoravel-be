package trendravel.photoravel_be.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import trendravel.photoravel_be.commom.service.ImageService;
import trendravel.photoravel_be.db.respository.review.ReviewRepository;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;
import trendravel.photoravel_be.db.spot.Spot;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;
import trendravel.photoravel_be.domain.review.service.ReviewService;
import trendravel.photoravel_be.domain.spot.dto.response.SpotSingleReadResponseDto;
import trendravel.photoravel_be.domain.spot.service.SpotService;
import trendravel.photoravel_be.domain.spot.dto.request.SpotRequestDto;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.db.respository.spot.SpotRepository;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class SpotServiceTest {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    SpotRepository spotRepository;
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    EntityManager em;

    @MockBean
    SpotService spotService;

    @MockBean
    ImageService imageService;

    SpotRequestDto spotRequestDto;
    Location location;
    Spot spot;
    Review review1;
    Review review2;
    Review review3;
    Review review4;
    @Autowired
    private ReviewService reviewService;

    @BeforeEach
    void before(){
        spotService = new SpotService(spotRepository, locationRepository, imageService);
        spotRequestDto = new SpotRequestDto();
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
        spot.setLocation(location);
        spotRepository.save(spot);
        review1 = Review
                .builder()
                .reviewType(ReviewTypes.SPOT)
                .content("멋지네")
                .rating(1.5)
                .spotReview(spot)
                .build();
        review2 = Review
                .builder()
                .reviewType(ReviewTypes.SPOT)
                .content("키야")
                .rating(2.4)
                .spotReview(spot)
                .build();
        review3 = Review
                .builder()
                .reviewType(ReviewTypes.SPOT)
                .content("이야")
                .rating(3.42)
                .spotReview(spot)
                .build();
        review4 = Review
                .builder()
                .reviewType(ReviewTypes.SPOT)
                .content("그저 굿")
                .rating(4.5)
                .spotReview(spot)
                .build();
        review1.setSpotReview(spot);
        review2.setSpotReview(spot);
        review3.setSpotReview(spot);
        review4.setSpotReview(spot);

        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviewRepository.save(review4);

        spotRequestDto.setSpotId(1L);
        spotRequestDto.setTitle("미디어랩스건물 방문");
        spotRequestDto.setDescription("미디어랩스관입니다");
        spotRequestDto.setLatitude(46.61);
        spotRequestDto.setLongitude(35.24);
        spotRequestDto.setLocationId(locationRepository.findById(1L).get().getId());
    }

    @Order(1)
    @Test
    @DisplayName("Spot CREATE 서비스가 (이미지 미포함) 잘 작동하는지 테스트")
    void createLocationServiceTest(){
        spotService.createSpot(spotRequestDto);

        assertThat(spotRepository.findById(
                        spotRequestDto.getSpotId())
                .get().getId()).isEqualTo(1L);
        assertThat(spotRepository.findById(
                spotRequestDto.getSpotId())
                .get().getLocation().getId()).
                isEqualTo(locationRepository.findById(1L).get().getId());
        assertThat(spotRepository.findById(
                        spotRequestDto.getSpotId())
                .get().getTitle()).isEqualTo("미디어랩스건물 방문");
        assertThat(spotRepository.findById(
                        spotRequestDto.getSpotId())
                .get().getLatitude()).isEqualTo(46.61);
        assertThat(spotRepository.findById(
                        spotRequestDto.getSpotId())
                .get().getLongitude()).isEqualTo(35.24);
        assertThat(spotRepository.findById(
                        spotRequestDto.getSpotId())
                .get().getDescription()).isEqualTo("미디어랩스관입니다");

    }

    @Order(2)
    @Test
    @DisplayName("Spot UPDATE 서비스가 (이미지 미포함) 잘 작동하는지 테스트")
    void updateLocationServiceTest(){
        spotService.createSpot(spotRequestDto);
        spotRequestDto.setTitle("미디어랩스 방문 후 모습");
        spotService.updateSpot(spotRequestDto);

        assertThat(spotRepository.findById(
                        spotRequestDto.getLocationId())
                .get().getId()).isEqualTo(1L);
        assertThat(spotRepository.findById(
                        spotRequestDto.getLocationId())
                .get().getTitle()).isEqualTo("미디어랩스 방문 후 모습");
        assertThat(spotRepository.findById(
                        spotRequestDto.getLocationId())
                .get().getLatitude()).isEqualTo(46.61);
        assertThat(spotRepository.findById(
                        spotRequestDto.getLocationId())
                .get().getLongitude()).isEqualTo(35.24);
        assertThat(spotRepository.findById(
                        spotRequestDto.getLocationId())
                .get().getDescription()).isEqualTo("미디어랩스관입니다");
    }

    @Order(3)
    @Test
    @DisplayName("Spot DELETE 서비스가 잘 작동하는지 테스트")
    void deleteLocationServiceTest(){
        spotService.createSpot(spotRequestDto);
        spotService.deleteSpot(spotRequestDto.getSpotId());

        assertThat(spotRepository.findById(spotRequestDto.getSpotId())).isEmpty();
    }

    @Test
    @DisplayName("Spot SINGLE READ 서비스가 잘 동작하는지 테스트")
    void readSingleSpotServiceTest(){
        //given
        spotService.readSingleSpot(location.getId(), spot.getId());
        Spot findSpot = spotRepository.findById(spot.getId()).orElse(null);

        //when
        SpotSingleReadResponseDto spotSingleReadResponseDto
                = spotService.readSingleSpot(location.getId(), spot.getId());
        List<RecentReviewsDto> findRecentReviews =
                spotSingleReadResponseDto.getRecentReviewDtos();

        //then

        assertThat(spotSingleReadResponseDto.getSpotId())
                .isEqualTo(findSpot.getId());
        assertThat(spotSingleReadResponseDto.getLatitude())
                .isEqualTo(findSpot.getLatitude());
        assertThat(spotSingleReadResponseDto.getLongitude())
                .isEqualTo(findSpot.getLongitude());
        assertThat(spotSingleReadResponseDto.getDescription())
                .isEqualTo(findSpot.getDescription());
        assertThat(spotSingleReadResponseDto.getTitle())
                .isEqualTo(findSpot.getTitle());
        assertThat(spotSingleReadResponseDto.getRatingAvg())
                .isEqualTo(String.format("%.2f",
                        (review4.getRating() + review2.getRating()
                                + review3.getRating() + review1.getRating()) / 4));
        assertThat(spotSingleReadResponseDto.getCreatedTime())
                .isEqualTo(findSpot.getCreatedAt());
        assertThat(spotSingleReadResponseDto.getUpdatedTime())
                .isEqualTo(findSpot.getUpdatedAt());
        assertThat(spotSingleReadResponseDto.getViews()).isEqualTo(0);
        assertThat(findRecentReviews.size()).isGreaterThan(1);
        assertThat(findRecentReviews).extracting("rating")
                .containsExactlyInAnyOrder(review4.getRating(),
                        review2.getRating(), review3.getRating());
    }

}