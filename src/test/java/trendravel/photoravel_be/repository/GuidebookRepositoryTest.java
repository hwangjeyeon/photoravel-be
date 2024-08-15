package trendravel.photoravel_be.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import trendravel.photoravel_be.config.QueryDSLConfig;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.guidebook.Guidebook;
import trendravel.photoravel_be.db.respository.guidebook.GuidebookRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDSLConfig.class)
public class GuidebookRepositoryTest {
    
    Guidebook guidebook;
    
    @Autowired
    GuidebookRepository guidebookRepository;
    
    
    
    @BeforeEach
    void before() {
        List<String> image = new ArrayList<>();
        image.add("이미지 url");
        guidebook = Guidebook.builder()
                .id(1L)
                .userId(1L)
                .title("아산 여행하기")
                .content("이것은 내용")
                .images(image)
                .region(Region.아산)
                .views(2)
                .build();
    }
    
    
    @Test
    @DisplayName("가이드북 저장 테스트")
    void saveGuidebookRepository() {
        
        guidebookRepository.save(guidebook);
        Guidebook findGuidebook = guidebookRepository.findById(guidebook.getId()).get();
        
        assertThat(findGuidebook.getId()).isEqualTo(guidebook.getId());
        assertThat(findGuidebook.getUserId()).isEqualTo(guidebook.getUserId());
        assertThat(findGuidebook.getTitle()).isEqualTo(guidebook.getTitle());
        assertThat(findGuidebook.getContent()).isEqualTo(guidebook.getContent());
        assertThat(findGuidebook.getImages()).containsExactlyElementsOf(guidebook.getImages());
        assertThat(findGuidebook.getRegion()).isEqualTo(guidebook.getRegion());
        assertThat(findGuidebook.getViews()).isEqualTo(guidebook.getViews());
        
    }
    
}
