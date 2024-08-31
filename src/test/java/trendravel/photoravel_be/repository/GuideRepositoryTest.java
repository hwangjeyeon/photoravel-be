package trendravel.photoravel_be.repository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import trendravel.photoravel_be.config.QueryDSLConfig;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.guide.Guide;
import trendravel.photoravel_be.db.respository.guide.GuideRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDSLConfig.class)
public class GuideRepositoryTest {
    
    Guide guide;
    
    @Autowired
    GuideRepository guideRepository;
    
    
    @BeforeEach
    void before() {
        String profileImg = "이미지 url 1";
        guide = Guide.builder()
                .id(1L)
                .accountId("계정 id")
                .password("1234")
                .name("신동욱")
                .region(Region.아산)
                .description("신동욱입니다")
                .profileImg(profileImg)
                .build();
    }
    
    @Test
    @DisplayName("가이드 저장 테스트")
    void saveGuideRepository() {
        
        guideRepository.save(guide);
        Guide findGuide = guideRepository.findById(guide.getId()).get();
        
        assertThat(findGuide.getId()).isEqualTo(guide.getId());
        assertThat(findGuide.getAccountId()).isEqualTo(guide.getAccountId());
        assertThat(findGuide.getPassword()).isEqualTo(guide.getPassword());
        assertThat(findGuide.getName()).isEqualTo(guide.getName());
        assertThat(findGuide.getRegion()).isEqualTo(guide.getRegion());
        assertThat(findGuide.getDescription()).isEqualTo(guide.getDescription());
        assertThat(findGuide.getProfileImg()).isEqualTo(guide.getProfileImg());
        
    }
}

