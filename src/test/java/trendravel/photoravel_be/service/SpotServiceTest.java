package trendravel.photoravel_be.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import trendravel.photoravel_be.common.exception.error.LocationErrorCode;
import trendravel.photoravel_be.common.exception.error.SpotErrorCode;
import trendravel.photoravel_be.common.exception.ApiException;
import trendravel.photoravel_be.common.image.service.ImageServiceFacade;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.db.respository.review.ReviewRepository;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;
import trendravel.photoravel_be.db.spot.Spot;
import trendravel.photoravel_be.domain.review.dto.response.RecentReviewsDto;
import trendravel.photoravel_be.domain.review.service.ReviewService;
import trendravel.photoravel_be.domain.spot.dto.request.SpotUpdatedImagesDto;
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
@ActiveProfiles("test")
class SpotServiceTest {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    SpotRepository spotRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @MockBean
    SpotService spotService;

    @MockBean
    ImageServiceFacade imageService;

    SpotRequestDto spotRequestDto;
    SpotUpdatedImagesDto spotUpdatedImagesDto;
    Location location;
    Spot spot;
    Review review1;
    Review review2;
    Review review3;
    Review review4;
    MemberEntity member;

    @Autowired
    private ReviewService reviewService;

    @BeforeEach
    void before(){
        spotService = new SpotService(spotRepository, locationRepository
                , memberRepository, imageService);
        spotRequestDto = new SpotRequestDto();
        spotUpdatedImagesDto = new SpotUpdatedImagesDto();
        member = MemberEntity.builder()
                .email("asfd")
                .memberId("hwangjeyeon")
                .nickname("hwangs")
                .name("황제연")
                .password("1234")
                .profileImg("1123asd.png")
                .build();
        memberRepository.save(member);

        location = Location
                .builder()
                .name("순천향대학교")
                .latitude(35.24)
                .longitude(46.61)
                .address("아산시 신창면 순천향로46")
                .description("순천향대학교입니다.")
                .point(new GeometryFactory().createPoint(
                        new Coordinate(46.61
                                , 35.24)))
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
        spot.setMemberSpot(member);
        review1 = Review
                .builder()
                .reviewType(ReviewTypes.SPOT)
                .content("멋지네")
                .rating(1.5)
                .member(member)
                .build();
        review2 = Review
                .builder()
                .reviewType(ReviewTypes.SPOT)
                .content("키야")
                .rating(2.4)
                .member(member)
                .build();
        review3 = Review
                .builder()
                .reviewType(ReviewTypes.SPOT)
                .content("이야")
                .rating(3.42)
                .member(member)
                .build();
        review4 = Review
                .builder()
                .reviewType(ReviewTypes.SPOT)
                .content("그저 굿")
                .rating(4.5)
                .member(member)
                .build();
        review1.setMemberReview(member);
        review2.setMemberReview(member);
        review3.setMemberReview(member);
        review4.setMemberReview(member);

        spotRequestDto.setTitle("미디어랩스건물 방문");
        spotRequestDto.setDescription("미디어랩스관입니다");
        spotRequestDto.setLatitude(46.61);
        spotRequestDto.setLongitude(35.24);
        spotRequestDto.setUserId(member.getMemberId());


        spotUpdatedImagesDto.setTitle("미디어랩스건물 방문");
        spotUpdatedImagesDto.setDescription("미디어랩스관입니다");
        spotUpdatedImagesDto.setLatitude(46.61);
        spotUpdatedImagesDto.setLongitude(35.24);
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
        spotUpdatedImagesDto.setSpotId(spotId);
        spotUpdatedImagesDto.setTitle("미디어랩스 방문 후 모습");
        spotService.updateSpot(spotUpdatedImagesDto);

        assertThat(spotRepository.findById(
                        spotId)
                .get().getTitle()).isEqualTo(spotUpdatedImagesDto.getTitle());
        assertThat(spotRepository.findById(
                        spotId)
                .get().getLatitude()).isEqualTo(spotUpdatedImagesDto.getLatitude());
        assertThat(spotRepository.findById(
                        spotId)
                .get().getLongitude()).isEqualTo(spotUpdatedImagesDto.getLongitude());
        assertThat(spotRepository.findById(
                        spotId)
                .get().getDescription()).isEqualTo(spotUpdatedImagesDto.getDescription());
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
                .isEqualTo(Double.parseDouble(String.format("%.2f",
                        (review4.getRating() + review2.getRating()
                                + review3.getRating() + review1.getRating()) / 4)));
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
        spotRequestDto.setLocationId(10000000000000000L);
        assertThatThrownBy(() -> spotService.createSpot(spotRequestDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(LocationErrorCode.LOCATION_NOT_FOUND.getErrorDescription());
    }

    @Order(6)
    @DisplayName("Location이 없을 때, SPOT READ EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    void readSpotExceptionWhenEmptyLocationTest(){
        spotRequestDto.setLocationId(10000000000000000L);
        assertThatThrownBy(() -> spotService.readSingleSpot(10000000000000000L,1L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(LocationErrorCode.LOCATION_NOT_FOUND.getErrorDescription());
    }

    @Order(7)
    @DisplayName("Location이 있지만 SPOT이 없을 때, SPOT READ EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    void readSpotExceptionWhenEmptySpotTest(){
        spotRequestDto.setLocationId(100000000000000L);
        Long id = locationRepository.save(location).getId();
        assertThatThrownBy(() -> spotService.readSingleSpot(id,10000000000000000L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(SpotErrorCode.SPOT_NOT_FOUND.getErrorDescription());
    }

    @Order(8)
    @DisplayName("Spot이 없을 때, SPOT UPDATE EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    void updateSpotExceptionTest(){
        spotUpdatedImagesDto.setSpotId(4L);
        assertThatThrownBy(() -> spotService.updateSpot(spotUpdatedImagesDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(SpotErrorCode.SPOT_NOT_FOUND.getErrorDescription());
    }

    @Order(9)
    @DisplayName("SPOT DELETE EXCEPTION이 잘 동작하는지 테스트")
    @Test
    @Transactional
    void deleteSpotExceptionTest(){
        assertThatThrownBy(() -> spotService.deleteSpot(10000000000000000L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(SpotErrorCode.SPOT_NOT_FOUND.getErrorDescription());
    }

    @DisplayName("SPOT의 리뷰 수가 100개 이상일 때, 99개로 변경하여 잘 보내는지 테스트")
    @Test
    @Transactional
    @Order(10)
    void locationReviewCountsMoreThanOneHundredTest(){
        Long locationId = locationRepository.save(location).getId();
        for (int i = 0; i < 1000; i++) {
            review1.setSpotReview(spot);
            reviewRepository.save(review1);
        }
        spot.setLocation(location);
        Long id = spotRepository.save(spot).getId();

        assertThat(spotService.readSingleSpot(locationId, id).getReviewCounts())
                .isEqualTo(99);
    }

    @DisplayName("SPOT의 리뷰 수가 100개 이하일 때, 그 개수를 그대로 READ하는지 테스트")
    @Test
    @Transactional
    @Order(11)
    void locationReviewCountsLessThanOneHundredTest(){
        Long locationId = locationRepository.save(location).getId();
        spot.setLocation(location);
        Long id = spotRepository.save(spot).getId();

        assertThat(spotService.readSingleSpot(locationId, id).getReviewCounts())
                .isEqualTo(location.getReview().size());
    }


}