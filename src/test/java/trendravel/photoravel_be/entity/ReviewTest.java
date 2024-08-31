package trendravel.photoravel_be.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.review.dto.request.ReviewRequestDto;
import trendravel.photoravel_be.db.review.enums.ReviewTypes;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewTest {

    @Test
    @DisplayName("Review 객체가 잘 생성되는지 확인테스트")
    void createReview(){

        //given
        List<String> image = new ArrayList<>();
        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg");


        //when
        Review locationReview = Review
                .builder()
                .id(1L)
                .reviewType(ReviewTypes.LOCATION)
                .images(image)
                .rating(4.0)
                .content("이 장소 좋네요")
                .build();

        Review spotReview = Review
                .builder()
                .id(2L)
                .reviewType(ReviewTypes.SPOT)
                .images(image)
                .rating(3.0)
                .content("생각보다 별론데요?")
                .build();

        //then
        assertThat(locationReview.getId()).isEqualTo(1L);
        assertThat(locationReview.getReviewType())
                .isEqualTo(ReviewTypes.LOCATION);
        assertThat(locationReview.getRating()).isEqualTo(4.0);
        assertThat(locationReview.getContent()).isEqualTo("이 장소 좋네요");
        for (String locationImage : locationReview.getImages()) {
            assertThat(locationImage).isIn(image);
        }

        assertThat(spotReview.getId()).isEqualTo(2L);
        assertThat(spotReview.getReviewType()).isEqualTo(ReviewTypes.SPOT);
        assertThat(spotReview.getRating()).isEqualTo(3.0);
        assertThat(spotReview.getContent()).isEqualTo("생각보다 별론데요?");
        for (String spotImage : spotReview.getImages()) {
            assertThat(spotImage).isIn(image);
        }

    }


    @Test
    @DisplayName("Location 객체가 잘 변경되는지 확인테스트")
    void updateReview(){

        //given
        List<String> image = new ArrayList<>();
        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg");
        Review locationReview = Review
                .builder()
                .id(1L)
                .reviewType(ReviewTypes.LOCATION)
                .images(image)
                .rating(4.0)
                .content("이 장소 좋네요")
                .build();

        Review spotReview = Review
                .builder()
                .id(2L)
                .reviewType(ReviewTypes.SPOT)
                .images(image)
                .rating(3.0)
                .content("생각보다 별론데요?")
                .build();

        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/cat.jpg");
        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/sheep.jpg");
        ReviewRequestDto locationReviewRequestDto = new ReviewRequestDto();
        locationReviewRequestDto.setReviewId(1L);
        locationReviewRequestDto.setReviewType(ReviewTypes.LOCATION);
        locationReviewRequestDto.setContent("다시보니 별로네요");
        locationReviewRequestDto.setRating(2.0);

        ReviewRequestDto spotReviewRequestDto = new ReviewRequestDto();
        spotReviewRequestDto.setReviewId(2L);
        spotReviewRequestDto.setReviewType(ReviewTypes.SPOT);
        spotReviewRequestDto.setRating(4.0);
        spotReviewRequestDto.setContent("다시보니 좋네요");

        //when
        locationReview.updateReview(locationReviewRequestDto);
        spotReview.updateReview(spotReviewRequestDto);

        //then
        assertThat(locationReview.getId()).isEqualTo(1L);
        assertThat(locationReview.getReviewType()).isEqualTo(ReviewTypes.LOCATION);
        assertThat(locationReview.getContent()).isEqualTo("다시보니 별로네요");
        assertThat(locationReview.getRating()).isEqualTo(2.0);


        assertThat(spotReview.getId()).isEqualTo(2L);
        assertThat(spotReview.getReviewType()).isEqualTo(ReviewTypes.SPOT);
        assertThat(spotReview.getContent()).isEqualTo("다시보니 좋네요");
        assertThat(spotReview.getRating()).isEqualTo(4.0);


    }

}