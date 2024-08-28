package trendravel.photoravel_be.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerRequestDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PhotographerTest {
    
    @Test
    @DisplayName("사진작가 객체 생성 테스트")
    void createPhotographer() {
        
        //given
        String profileImg = "이미지 url";
        
        //when
        Photographer photographer = Photographer.builder()
                .id(1L)
                .accountId("계정 id")
                .password("1234")
                .name("신동욱")
                .region(Region.아산)
                .description("신동욱입니다")
                .profileImg(profileImg)
                .build();
        
        //then
        assertThat(photographer.getId()).isEqualTo(1L);
        assertThat(photographer.getAccountId()).isEqualTo("계정 id");
        assertThat(photographer.getPassword()).isEqualTo("1234");
        assertThat(photographer.getName()).isEqualTo("신동욱");
        assertThat(photographer.getRegion()).isEqualTo(Region.아산);
        assertThat(photographer.getDescription()).isEqualTo("신동욱입니다");
        assertThat(photographer.getProfileImg()).isEqualTo("이미지 url");
        
    }
    
    @Test
    @DisplayName("사진작가 객체 변경 테스트")
    void updatePhotographer() {
        
        //given
        
        String profileImg = "이미지 url 1";
        Photographer photographer = Photographer.builder()
                .id(1L)
                .accountId("계정 id")
                .password("1234")
                .name("신동욱")
                .region(Region.아산)
                .description("신동욱입니다")
                .profileImg(profileImg)
                .build();
        
        PhotographerRequestDto photographerRequestDto = new PhotographerRequestDto();
        photographerRequestDto.setPassword("4321");
        photographerRequestDto.setName("김동욱");
        photographerRequestDto.setRegion(Region.천안);
        photographerRequestDto.setDescription("설명 수정");
        
        List<String> profileImgList = new ArrayList<>();
        
        //when
        profileImg = "이미지 url 2";
        profileImgList.add(profileImg);
        photographer.updatePhotographer(photographerRequestDto, profileImgList);
        
        //then
        assertThat(photographer.getPassword()).isEqualTo("4321");
        assertThat(photographer.getName()).isEqualTo("김동욱");
        assertThat(photographer.getRegion()).isEqualTo(Region.천안);
        assertThat(photographer.getDescription()).isEqualTo("설명 수정");
        
        
        
    }
    
    
}
