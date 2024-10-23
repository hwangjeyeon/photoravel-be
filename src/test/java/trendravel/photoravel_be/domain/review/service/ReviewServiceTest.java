package trendravel.photoravel_be.domain.review.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import trendravel.photoravel_be.commom.error.LocationErrorCode;
import trendravel.photoravel_be.commom.error.ReviewErrorCode;
import trendravel.photoravel_be.commom.error.SpotErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.commom.image.service.ImageServiceFacade;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.respository.location.LocationRepository;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.db.respository.photographer.PhotographerRepository;
import trendravel.photoravel_be.db.respository.review.ReviewRepository;
import trendravel.photoravel_be.db.respository.spot.SpotRepository;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;
import trendravel.photoravel_be.db.spot.Spot;
import trendravel.photoravel_be.domain.review.dto.request.ReviewRequestDto;
import trendravel.photoravel_be.domain.review.dto.request.ReviewUpdateImagesDto;
import trendravel.photoravel_be.domain.review.dto.response.ReviewResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class ReviewServiceTest {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    SpotRepository spotRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    PhotographerRepository photographerRepository;
    @Autowired
    MemberRepository memberRepository;

    @MockBean
    ReviewService reviewService;

    @MockBean
    ImageServiceFacade imageService;

    ReviewRequestDto locationReviewRequestDto = new ReviewRequestDto();
    ReviewRequestDto spotReviewRequestDto = new ReviewRequestDto();
    ReviewUpdateImagesDto locationReviewUpdateRequestDto = new ReviewUpdateImagesDto();
    ReviewUpdateImagesDto spotReviewUpdateRequestDto = new ReviewUpdateImagesDto();

    Location location;
    Spot spot;
    Review locationReview;
    Review spotReview;
    Review review1;
    Review review2;
    Review review3;
    Review review4;
    Long findLocationId;
    Long findSpotId;
    MemberEntity member;

    @BeforeEach
    void before(){
        reviewService = new ReviewService(reviewRepository, spotRepository, locationRepository,
                photographerRepository, memberRepository, imageService);
        member = MemberEntity.builder()
                .id(1L)
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
                .views(0)
                .point(new GeometryFactory().createPoint(
                        new Coordinate(35.24
                                , 46.61)))
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
        Long locationId = locationRepository.save(location).getId();
        findLocationId = locationId;
        Long spotId = spotRepository.save(spot).getId();
        findSpotId = spotId;
        spot.setLocation(location);
        locationReview = Review.builder()
                .id(1L)
                .reviewType(ReviewTypes.LOCATION)
                .content("우와 멋진 장소네요 ㅋㅋ")
                .rating(3.4)
                .build();
        spotReview = Review.builder()
                .id(2L)
                .reviewType(ReviewTypes.SPOT)
                .content("내 인생샷 장소임 ㄹㅇ")
                .rating(1.4)
                .build();

        review1 = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .content("멋지네")
                .rating(1.5)
                .locationReview(location)
                .build();
        review2 = Review
                .builder()
                .reviewType(ReviewTypes.LOCATION)
                .content("키야")
                .rating(2.4)
                .locationReview(location)
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
        review1.setLocationReview(location);
        review2.setLocationReview(location);
        review1.setMemberReview(member);
        review2.setMemberReview(member);
        review3.setSpotReview(spot);
        review4.setSpotReview(spot);
        review3.setMemberReview(member);
        review4.setMemberReview(member);

        locationReviewRequestDto.setReviewId(locationId);
        locationReviewRequestDto.setReviewType(locationReview.getReviewType());
        locationReviewRequestDto.setContent(locationReview.getContent());
        locationReviewRequestDto.setRating(locationReview.getRating());
        locationReviewRequestDto.setTypeId(locationId);
        locationReviewRequestDto.setUserId(member.getMemberId());

        locationReviewUpdateRequestDto.setReviewId(locationId);
        locationReviewUpdateRequestDto.setReviewType(locationReview.getReviewType());
        locationReviewUpdateRequestDto.setContent(locationReview.getContent());
        locationReviewUpdateRequestDto.setRating(locationReview.getRating());
        locationReviewUpdateRequestDto.setTypeId(locationId);

        spotReviewRequestDto.setReviewId(spotId);
        spotReviewRequestDto.setReviewType(spotReview.getReviewType());
        spotReviewRequestDto.setRating(spotReview.getRating());
        spotReviewRequestDto.setContent(spotReview.getContent());
        spotReviewRequestDto.setTypeId(spotId);
        spotReviewRequestDto.setUserId(member.getMemberId());


        spotReviewUpdateRequestDto.setReviewId(spotId);
        spotReviewUpdateRequestDto.setReviewType(spotReview.getReviewType());
        spotReviewUpdateRequestDto.setRating(spotReview.getRating());
        spotReviewUpdateRequestDto.setContent(spotReview.getContent());
        spotReviewUpdateRequestDto.setTypeId(spotId);
    }

    @Test
    @DisplayName("Location/Spot Review CREATE (이미지 미포함) Service가 잘 동작하는지 테스트")
    @Transactional
    @Order(1)
    void createReviewTest (){
        Long locationId = reviewService.createReview(locationReviewRequestDto).getReviewId();
        Long spotId = reviewService.createReview(spotReviewRequestDto).getReviewId();
        locationReviewRequestDto.setReviewId(locationId);
        spotReviewRequestDto.setReviewId(spotId);

        Review findLocationReview = reviewRepository.findById(locationId).get();
        Review findSpotReview = reviewRepository.findById(spotId).get();


        assertThat(findLocationReview.getReviewType())
                .isEqualTo(locationReviewRequestDto.getReviewType());
        assertThat(findLocationReview.getContent())
                .isEqualTo(locationReviewRequestDto.getContent());
        assertThat(findLocationReview.getRating())
                .isEqualTo(locationReviewRequestDto.getRating());
        assertThat(findLocationReview.getLocationReview().getId())
                .isEqualTo(locationReviewRequestDto.getTypeId());


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
    @Transactional
    @Order(2)
    void updateReviewTest (){
        Long locationId = reviewService.createReview(locationReviewRequestDto).getReviewId();
        Long spotId = reviewService.createReview(spotReviewRequestDto).getReviewId();
        locationReviewUpdateRequestDto.setReviewId(locationId);
        spotReviewUpdateRequestDto.setReviewId(spotId);
        locationReviewUpdateRequestDto.setRating(2.1);
        spotReviewUpdateRequestDto.setRating(1.0);

        reviewService.updateReview(locationReviewUpdateRequestDto);
        reviewService.updateReview(spotReviewUpdateRequestDto);

        Review findLocationReview = reviewRepository.findById(locationReviewUpdateRequestDto
                .getReviewId()).orElse(null);

        Review findSpotReview = reviewRepository.findById(spotReviewUpdateRequestDto
                .getReviewId()).orElse(null);

        assertThat(findLocationReview.getReviewType())
                .isEqualTo(locationReviewUpdateRequestDto.getReviewType());
        assertThat(findLocationReview.getContent())
                .isEqualTo(locationReviewUpdateRequestDto.getContent());
        assertThat(findLocationReview.getRating())
                .isEqualTo(locationReviewUpdateRequestDto.getRating());
        assertThat(findLocationReview.getLocationReview().getId())
                .isEqualTo(locationReviewUpdateRequestDto.getTypeId());

        assertThat(findSpotReview.getReviewType())
                .isEqualTo(spotReviewUpdateRequestDto.getReviewType());
        assertThat(findSpotReview.getContent())
                .isEqualTo(spotReviewUpdateRequestDto.getContent());
        assertThat(findSpotReview.getRating())
                .isEqualTo(spotReviewUpdateRequestDto.getRating());
        assertThat(findSpotReview.getSpotReview().getId())
                .isEqualTo(spotReviewUpdateRequestDto.getTypeId());
    }


    @Test
    @DisplayName("Location/Spot Review DELETE Service가 잘 동작하는지 테스트")
    @Transactional
    @Order(3)
    void deleteReviewTest (){
        Long locationId = reviewService.createReview(locationReviewRequestDto).getReviewId();
        Long spotId = reviewService.createReview(spotReviewRequestDto).getReviewId();
        locationReviewRequestDto.setReviewId(locationId);
        spotReviewRequestDto.setReviewId(spotId);
        reviewService.deleteReview(locationId);
        reviewService.deleteReview(spotId);

        Review findLocationReview = reviewRepository.findById(locationReviewRequestDto
                .getReviewId()).orElse(null);

        Review findSpotReview = reviewRepository.findById(spotReviewRequestDto
                .getReviewId()).orElse(null);

        assertThat(findLocationReview).isNull();
        assertThat(findSpotReview).isNull();

    }

    @Test
    @DisplayName("Location Review READ Service가 잘 동작하는지 테스트")
    @Transactional
    @Order(4)
    void readLocationReviewsTest(){
        Long locationId = reviewService.createReview(locationReviewRequestDto).getReviewId();
        List<ReviewResponseDto> reviews = reviewService.readAllLocationReview(findLocationId);

        assertThat(reviews.get(0).getReviewType()).isEqualTo(review1.getReviewType().toString());
        assertThat(reviews.get(0).getContent()).isEqualTo(review1.getContent());
        assertEquals(reviews.get(0).getRating(),review1.getRating());
    }

    @Test
    @DisplayName("Spot Review READ Service가 잘 동작하는지 테스트")
    @Transactional
    @Order(5)
    void readSpotReviewsTest(){
        Long locationId = reviewService.createReview(locationReviewRequestDto).getReviewId();
        reviewService.createReview(spotReviewRequestDto).getReviewId();
        List<ReviewResponseDto> reviews = reviewService.readAllSpotReview(findLocationId, findSpotId);


        assertThat(reviews.get(0).getReviewType()).isEqualTo(review3.getReviewType().toString());
        assertThat(reviews.get(0).getContent()).isEqualTo(review3.getContent());
        assertEquals(reviews.get(0).getRating(),review3.getRating());
    }


    @Test
    @DisplayName("LOCATION REVIEW CREATE의 EXCEPTION이 잘 동작하는지 테스트")
    @Transactional
    @Order(6)
    void createLocationReviewExceptionTest(){
        locationReviewRequestDto.setTypeId(10000000000000000L);
        assertThatThrownBy(() -> reviewService.createReview(locationReviewRequestDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(LocationErrorCode.LOCATION_NOT_FOUND.getErrorDescription());
    }

    @Test
    @DisplayName("SPOT REVIEW CREATE의 EXCEPTION이 잘 동작하는지 테스트")
    @Transactional
    @Order(7)
    void createSpotReviewExceptionTest(){
        spotReviewRequestDto.setTypeId(10000000000000000L);
        assertThatThrownBy(() -> reviewService.createReview(spotReviewRequestDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(SpotErrorCode.SPOT_NOT_FOUND.getErrorDescription());
    }

    @Test
    @DisplayName("LOCATION REVIEW READ의 EXCEPTION이 잘 동작하는지 테스트")
    @Transactional
    @Order(8)
    void readLocationReviewExceptionTest(){
        assertThatThrownBy(() -> reviewService.readAllLocationReview(10000000000000000L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(LocationErrorCode.LOCATION_NOT_FOUND.getErrorDescription());
    }


    @Test
    @DisplayName("LOCATION이 없을 때, SPOT REVIEW READ의 EXCEPTION이 잘 동작하는지 테스트")
    @Transactional
    @Order(9)
    void readLocationReviewWhenEmptyLocationExceptionTest(){
        assertThatThrownBy(() -> reviewService.readAllSpotReview(10000000000000000L, 1L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(LocationErrorCode.LOCATION_NOT_FOUND.getErrorDescription());
    }

    @Test
    @DisplayName("SPOT이 없을 때, SPOT REVIEW READ의 EXCEPTION이 잘 동작하는지 테스트")
    @Transactional
    @Order(10)
    void readSpotReviewWhenEmptySpotExceptionTest(){
        assertThatThrownBy(() -> reviewService.readAllSpotReview(findLocationId, 10000000000000000L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(SpotErrorCode.SPOT_NOT_FOUND.getErrorDescription());
    }

    @Test
    @DisplayName("REVIEW UPDATE의 EXCEPTION이 잘 동작하는지 테스트")
    @Transactional
    @Order(11)
    void updateReviewExceptionTest(){
        locationReviewUpdateRequestDto.setReviewId(10000000000000000L);
        assertThatThrownBy(() -> reviewService.updateReview(locationReviewUpdateRequestDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ReviewErrorCode.REVIEW_NOT_FOUND.getErrorDescription());
    }

    @Test
    @DisplayName("REVIEW DELETE의 EXCEPTION이 잘 동작하는지 테스트")
    @Transactional
    @Order(12)
    void deleteReviewExceptionTest(){
        assertThatThrownBy(() -> reviewService.deleteReview(10000000000000000L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ReviewErrorCode.REVIEW_NOT_FOUND.getErrorDescription());
    }


}