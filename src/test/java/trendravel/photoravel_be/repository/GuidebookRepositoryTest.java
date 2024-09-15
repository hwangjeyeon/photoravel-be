package trendravel.photoravel_be.repository;

import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GuidebookRepositoryTest {
    
    Guidebook guidebook1;
    Guidebook guidebook2;
    
    @Autowired
    GuidebookRepository guidebookRepository;
    
    
    
    @BeforeEach
    @Order(1)
    void before() {
        List<String> image = new ArrayList<>();
        image.add("https://photravle.s3.ap-northeast-2.amazonaws.com/2_1724753324187.png");
        guidebook1 = Guidebook.builder()
                .title("아산 여행하기")
                .content("아산에는")
                .images(image)
                .region(Region.아산)
                .build();
        
        guidebook2 = Guidebook.builder()
                .title("천안 여행하기")
                .content("천안에는")
                .images(image)
                .region(Region.천안)
                .build();
    }
    
    
    @Test
    @DisplayName("가이드북 저장 테스트")
    @Order(2)
    void saveGuidebookRepository() {
        
        guidebookRepository.save(guidebook1);
        guidebookRepository.save(guidebook2);
        Guidebook findGuidebook = guidebookRepository.findById(guidebook1.getId()).get();
        
        assertThat(findGuidebook.getId()).isEqualTo(1L);
        assertThat(findGuidebook.getTitle()).isEqualTo(guidebook1.getTitle());
        assertThat(findGuidebook.getContent()).isEqualTo(guidebook1.getContent());
        assertThat(findGuidebook.getImages()).containsExactlyElementsOf(guidebook1.getImages());
        assertThat(findGuidebook.getRegion()).isEqualTo(guidebook1.getRegion());
        assertThat(findGuidebook.getViews()).isEqualTo(guidebook1.getViews());
        
        Guidebook findGuidebook2 = guidebookRepository.findById(guidebook2.getId()).get();
        assertThat(findGuidebook2.getId()).isEqualTo(2L);
    }
    
    @Test
    @DisplayName("가이드북 리스트 반환 테스트")
    void getListGuidebookRepository() {
        
        guidebookRepository.save(guidebook1);
        guidebookRepository.save(guidebook2);
        
        List<Guidebook> list = guidebookRepository.findAll();
        
        assertThat(list.size()).isEqualTo(2);
        
    }
    
    
}
