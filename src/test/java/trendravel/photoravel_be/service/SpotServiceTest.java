package trendravel.photoravel_be.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import trendravel.photoravel_be.commom.error.LocationErrorCode;
import trendravel.photoravel_be.commom.error.SpotErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.commom.image.service.ImageService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
                .point(new GeometryFactory().createPoint(
                        new Coordinate(35.24
                                , 46.61)))
                .views(0)
                .build();
        location.getPoint().setSRID(4326);
        spot = Spot
                .builder()
                .description("미디어랩스관입니다")
                .longitude(35.24)
                .latitude(46.61)
                .title("미디어랩스건물 방문")
                .views(0)
                .location(location)
                .build();
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



        spotRequestDto.setTitle("미디어랩스건물 방문");
        spotRequestDto.setDescription("미디어랩스관입니다");
        spotRequestDto.setLatitude(46.61);
        spotRequestDto.setLongitude(35.24);
    }

    @Order(1)
    @Test
    @DisplayName("Spot CREATE 서비스가 (이미지 미포함) 잘 작동하는지 테스트")
    @Transactional
    void createLocationServiceTest(){
        Long id = locationRepository.save(location).getId();
        spotRequestDto.setLocationId(id);
        Long spotId = spotService.createSpot(spotRequestDto).getSpotId();

        assertThat(spotRepository.findById(
                       spotId)
                .get().getTitle()).isEqualTo(spotRequestDto.getTitle());
        assertThat(spotRepository.findById(
                        spotId)
                .get().getLatitude()).isEqualTo(spotRequestDto.getLatitude());
        assertThat(spotRepository.findById(
                        spotId)
                .get().getLongitude()).isEqualTo(spotRequestDto.getLongitude());
        assertThat(spotRepository.findById(
                        spotId)
                .get().getDescription()).isEqualTo(spotRequestDto.getDescription());

    }

    @Order(2)
    @Test
    @DisplayName("Spot UPDATE 서비스가 (이미지 미포함) 잘 작동하는지 테스트")
    @Transactional
    void updateLocationServiceTest(){
        Long id = locationRepository.save(location).getId();
        spotRequestDto.setLocationId(id);
        Long spotId = spotService.createSpot(spotRequestDto).getSpotId();
        spotRequestDto.setSpotId(spotId);
        spotRequestDto.setTitle("미디어랩스 방문 후 모습");
        spotService.updateSpot(spotRequestDto);

        assertThat(spotRepository.findById(
                        spotId)
                .get().getTitle()).isEqualTo(spotRequestDto.getTitle());
        assertThat(spotRepository.findById(
                        spotId)
                .get().getLatitude()).isEqualTo(spotRequestDto.getLatitude());
        assertThat(spotRepository.findById(
                        spotId)
                .get().getLongitude()).isEqualTo(spotRequestDto.getLongitude());
        assertThat(spotRepository.findById(
                        spotId)
                .get().getDescription()).isEqualTo(spotRequestDto.getDescription());
    }

    @Order(3)
    @Test
    @DisplayName("Spot DELETE 서비스가 잘 작동하는지 테스트")
    @Transactional
    void deleteLocationServiceTest(){
        Long id = locationRepository.save(location).getId();
        spotRequestDto.setLocationId(id);
        Long spotId = spotService.createSpot(spotRequestDto).getSpotId();

        spotService.deleteSpot(spotId);

        assertThat(spotRepository.findById(spotId)).isEmpty();
    }

    @Order(4)
    @Test
    @DisplayName("Spot SINGLE READ 서비스가 잘 동작하는지 테스트")
    @Transactional
    void readSingleSpotServiceTest(){
        //given
        Long id = locationRepository.save(location).getId();
        spotRequestDto.setLocationId(id);
        Long spotId = spotService.createSpot(spotRequestDto).getSpotId();

        Spot findSpot = spotRepository.findById(spotId).get();

        review1.setSpotReview(findSpot);
        review2.setSpotReview(findSpot);
        review3.setSpotReview(findSpot);
        review4.setSpotReview(findSpot);

        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviewRepository.save(review4);
        //when
        SpotSingleReadResponseDto spotSingleReadResponseDto
                = spotService.readSingleSpot(id, spotId);
        List<RecentReviewsDto> findRecentReviews =
                spotSingleReadResponseDto.getRecentReviewDtos();


        //then

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
        assertThat(spotSingleReadResponseDto.getViews()).isEqualTo(0);
        assertThat(findRecentReviews.size()).isGreaterThan(1);
        assertThat(findRecentReviews).extracting("rating")
                .containsExactlyInAnyOrder(review4.getRating(),
                        review2.getRating(), review3.getRating());
    }

    @Order(5)
    @DisplayName("Location이 없을 때, SPOT CREATE의 EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    void createSpotExceptionTest(){
        spotRequestDto.setLocationId(4L);
        assertThatThrownBy(() -> spotService.createSpot(spotRequestDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(LocationErrorCode.LOCATION_NOT_FOUND.getErrorDescription());
    }

    @Order(6)
    @DisplayName("Location이 없을 때, SPOT READ EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    void readSpotExceptionWhenEmptyLocationTest(){
        spotRequestDto.setLocationId(4L);
        assertThatThrownBy(() -> spotService.readSingleSpot(5L,1L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(LocationErrorCode.LOCATION_NOT_FOUND.getErrorDescription());
    }

    @Order(7)
    @DisplayName("Location이 있지만 SPOT이 없을 때, SPOT READ EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    void readSpotExceptionWhenEmptySpotTest(){
        spotRequestDto.setLocationId(4L);
        assertThatThrownBy(() -> spotService.readSingleSpot(1L,2L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(LocationErrorCode.LOCATION_NOT_FOUND.getErrorDescription());
    }

    @Order(8)
    @DisplayName("Spot이 없을 때, SPOT UPDATE EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    void updateSpotExceptionTest(){
        spotRequestDto.setSpotId(4L);
        assertThatThrownBy(() -> spotService.updateSpot(spotRequestDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(SpotErrorCode.SPOT_NOT_FOUND.getErrorDescription());
    }

    @Order(9)
    @DisplayName("SPOT DELETE EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    void deleteSpotExceptionTest(){
        assertThatThrownBy(() -> spotService.deleteSpot(100L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(SpotErrorCode.SPOT_NOT_FOUND.getErrorDescription());
    }


}