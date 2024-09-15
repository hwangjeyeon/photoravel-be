package trendravel.photoravel_be.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trendravel.photoravel_be.db.location.Location;
import trendravel.photoravel_be.domain.location.dto.request.LocationRequestDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



class LocationTest {

    @Test
    @DisplayName("Location 객체가 잘 생성되는지 확인테스트")
    void createLocation(){

        //given
        List<String> image = new ArrayList<>();
        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg");


        //when
        Location location = Location
                .builder()
                .id(1L)
                .name("순천향대학교")
                .latitude(35.24)
                .longitude(46.61)
                .address("아산시 신창면 순천향로46")
                .description("순천향대학교입니다.")
                .images(image)
                .views(0)
                .build();

        //then
        assertThat(location.getId()).isEqualTo(1L);
        assertThat(location.getLatitude()).isEqualTo(35.24);
        assertThat(location.getLongitude()).isEqualTo(46.61);
        assertThat(location.getName()).isEqualTo("순천향대학교");
        assertThat(location.getAddress()).isEqualTo("아산시 신창면 순천향로46");
        assertThat(location.getDescription()).isEqualTo("순천향대학교입니다.");
        for (String locationImage : location.getImages()) {
            assertThat(locationImage).isIn(image);
        }
        assertThat(location.getViews()).isEqualTo(0);

    }


    @Test
    @DisplayName("Location 객체가 잘 변경되는지 확인테스트")
    void updateLocation(){

        //given
        List<String> image = new ArrayList<>();
        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/puppy.jpg");
        Location location = Location
                .builder()
                .id(1L)
                .name("순천향대학교")
                .latitude(35.24)
                .longitude(46.61)
                .address("아산시 신창면 순천향로46")
                .description("순천향대학교입니다.")
                .images(image)
                .views(0)
                .build();

        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/cat.jpg");
        image.add("https://s3.ap-northeast-2.amazonaws.com/mybucket/sheep.jpg");
        LocationRequestDto locationRequestDto = new LocationRequestDto();
        locationRequestDto.setLatitude(24.12);
        locationRequestDto.setLongitude(32.53);
        locationRequestDto.setAddress("김포시 풍무동 양도로49");
        locationRequestDto.setDescription("우리집 근처입니다");
        locationRequestDto.setName("우리집");
        locationRequestDto.setUserId("hwangjeyeon");

        //when
        location.updateLocation(locationRequestDto);

        //then
        assertThat(location.getId()).isEqualTo(1L);
        assertThat(location.getLatitude()).isEqualTo(24.12);
        assertThat(location.getLongitude()).isEqualTo(32.53);
        assertThat(location.getName()).isEqualTo("우리집");
        assertThat(location.getAddress()).isEqualTo("김포시 풍무동 양도로49");
        assertThat(location.getDescription()).isEqualTo("우리집 근처입니다");
        assertThat(location.getViews()).isEqualTo(0);

    }

}