package trendravel.photoravel_be.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.guide.Guide;
import trendravel.photoravel_be.domain.guide.dto.request.GuideRequestDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GuideTest {
    
    @Test
    @DisplayName("가이드 객체 생성 테스트")
    void createGuide() {
        
        //given
        String profileImg = "이미지 url";
        
        //when
        Guide guide = Guide.builder()
                .id(1L)
                .accountId("계정 id")
                .password("1234")
                .name("신동욱")
                .region(Region.아산)
                .description("신동욱입니다")
                .profileImg(profileImg)
                .build();
        
        //then
        assertThat(guide.getId()).isEqualTo(1L);
        assertThat(guide.getAccountId()).isEqualTo("계정 id");
        assertThat(guide.getPassword()).isEqualTo("1234");
        assertThat(guide.getName()).isEqualTo("신동욱");
        assertThat(guide.getRegion()).isEqualTo(Region.아산);
        assertThat(guide.getDescription()).isEqualTo("신동욱입니다");
        assertThat(guide.getProfileImg()).isEqualTo("이미지 url");
        
    }
    
    @Test
    @DisplayName("가이드 객체 변경 테스트")
    void updateGuide() {
        
        //given
        
        String profileImg = "이미지 url 1";
        Guide guide = Guide.builder()
                .id(1L)
                .accountId("계정 id")
                .password("1234")
                .name("신동욱")
                .region(Region.아산)
                .description("신동욱입니다")
                .profileImg(profileImg)
                .build();
        
        GuideRequestDto guidebookRequestDto = new GuideRequestDto();
        guidebookRequestDto.setPassword("4321");
        guidebookRequestDto.setName("김동욱");
        guidebookRequestDto.setRegion(Region.천안);
        guidebookRequestDto.setDescription("설명 수정");
        
        List<String> profileImgList = new ArrayList<>();
        
        //when
        profileImg = "이미지 url 2";
        profileImgList.add(profileImg);
        guide.updateGuide(guidebookRequestDto, profileImgList);
        
        //then
        assertThat(guide.getPassword()).isEqualTo("4321");
        assertThat(guide.getName()).isEqualTo("김동욱");
        assertThat(guide.getRegion()).isEqualTo(Region.천안);
        assertThat(guide.getDescription()).isEqualTo("설명 수정");
        
        
        
    }
    
    
}
