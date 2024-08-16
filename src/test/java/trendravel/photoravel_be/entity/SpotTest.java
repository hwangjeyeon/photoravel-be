package trendravel.photoravel_be.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trendravel.photoravel_be.db.spot.Spot;
import trendravel.photoravel_be.domain.spot.dto.request.SpotRequestDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SpotTest {

    @Test
    @DisplayName("Spot 객체가 잘 생성되는지 확인테스트")
    void createSpot(){

        //given
        List<String> image = new ArrayList<>();
        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg");


        //when
        Spot spot = Spot
                .builder()
                .id(1L)
                .description("미디어랩스관입니다")
                .longitude(35.24)
                .latitude(46.61)
                .title("미디어랩스건물 방문")
                .images(image)
                .views(0)
                .build();

        //then
        assertThat(spot.getId()).isEqualTo(1L);
        assertThat(spot.getTitle()).isEqualTo("미디어랩스건물 방문");
        assertThat(spot.getDescription()).isEqualTo("미디어랩스관입니다");
        assertThat(spot.getLatitude()).isEqualTo(46.61);
        assertThat(spot.getLongitude()).isEqualTo(35.24);
        assertThat(spot.getViews()).isEqualTo(0);
    }

    @Test
    @DisplayName("Spot 객체가 잘 변경되는지 확인테스트")
    void updateSpot(){

        //given
        List<String> image = new ArrayList<>();
        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg");
        Spot spot = Spot
                .builder()
                .id(1L)
                .description("미디어랩스관입니다")
                .longitude(35.24)
                .latitude(46.61)
                .title("미디어랩스건물 방문")
                .images(image)
                .views(0)
                .build();

        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/cat.jpg");
        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/sheep.jpg");

        SpotRequestDto spotRequestDto = new SpotRequestDto();
        spotRequestDto.setLatitude(24.12);
        spotRequestDto.setLongitude(32.53);
        spotRequestDto.setDescription("우리집 근처 고기집입니다");
        spotRequestDto.setTitle("우리집 근처 고기집");
        spotRequestDto.setUserId("Messi");

        //when
        spot.updateSpot(spotRequestDto);

        //then
        assertThat(spot.getId()).isEqualTo(1L);
        assertThat(spot.getLatitude()).isEqualTo(24.12);
        assertThat(spot.getLongitude()).isEqualTo(32.53);
        assertThat(spot.getTitle()).isEqualTo("우리집 근처 고기집");
        assertThat(spot.getDescription()).isEqualTo("우리집 근처 고기집입니다");
        assertThat(spot.getViews()).isEqualTo(0);


    }

}