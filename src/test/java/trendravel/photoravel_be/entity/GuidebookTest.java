package trendravel.photoravel_be.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.guidebook.Guidebook;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookRequestDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GuidebookTest {
    
    @Test
    @DisplayName("가이드북 객체 생성 테스트")
    void createGuidebook() {
        
        //given
        List<String> image = new ArrayList<>();
        image.add("이미지 url");
        
        //when
        Guidebook guidebook = Guidebook.builder()
                .id(1L)
                .userId(1L)
                .title("아산 여행하기")
                .content("이것은 내용")
                .images(image)
                .region(Region.아산)
                .views(2)
                .build();
        
        //then
        assertThat(guidebook.getId()).isEqualTo(1L);
        assertThat(guidebook.getUserId()).isEqualTo(1L);
        assertThat(guidebook.getTitle()).isEqualTo("아산 여행하기");
        assertThat(guidebook.getContent()).isEqualTo("이것은 내용");
        assertThat(guidebook.getRegion()).isEqualTo(Region.아산);
        assertThat(guidebook.getViews()).isEqualTo(2);
        for (String guidebookImage : guidebook.getImages()) {
            assertThat(guidebookImage).isIn(image);
        }
        
    }
    
    @Test
    @DisplayName("가이드북 객체 변경 테스트")
    void updateGuidebook() {
        
        //given
        List<String> image = new ArrayList<>();
        image.add("이미지 url 1");
        Guidebook guidebook = Guidebook.builder()
                .id(1L)
                .userId(1L)
                .title("아산 여행하기")
                .content("이것은 내용")
                .images(image)
                .region(Region.아산)
                .views(2)
                .build();
        
        GuidebookRequestDto guidebookRequestDto = new GuidebookRequestDto();
        guidebookRequestDto.setTitle("수정된 제목");
        guidebookRequestDto.setContent("수정된 내용");
        guidebookRequestDto.setRegion(Region.천안);
        
        //when
        image.add("이미지 url 2");
        guidebook.updateGuidebook(guidebookRequestDto, image);
        
        //then
        assertThat(guidebook.getTitle()).isEqualTo("수정된 제목");
        assertThat(guidebook.getContent()).isEqualTo("수정된 내용");
        assertThat(guidebook.getRegion()).isEqualTo(Region.천안);
        for (String guidebookImage : guidebook.getImages()) {
            assertThat(guidebookImage).isIn(image);
        }
        
    }
    
    
}
