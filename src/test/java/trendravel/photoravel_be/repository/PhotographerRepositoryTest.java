package trendravel.photoravel_be.repository;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import trendravel.photoravel_be.config.QueryDSLConfig;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.db.respository.photographer.PhotographerRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(QueryDSLConfig.class)
@Transactional
@ActiveProfiles("test")
public class PhotographerRepositoryTest {
    
    Photographer photographer;
    
    @Autowired
    PhotographerRepository photographerRepository;
    
    
    @BeforeEach
    void before() {
        String profileImg = "이미지 url 1";
        photographer = Photographer.builder()
                .id(1L)
                .accountId("계정 id")
                .password("1234")
                .name("신동욱")
                .region(Region.아산)
                .description("신동욱입니다")
                .profileImg(profileImg)
                .careerYear(1)
                .matchingCount(0)
                .build();
    }
    
    @Test
    @DisplayName("사진작가 저장 테스트")
    void savePhotographerRepository() {
        
        Long id = photographerRepository.save(photographer).getId();
        Photographer findPhotographer = photographerRepository.findById(id).get();
        
        assertThat(findPhotographer.getId()).isEqualTo(id);
        assertThat(findPhotographer.getAccountId()).isEqualTo(photographer.getAccountId());
        assertThat(findPhotographer.getPassword()).isEqualTo(photographer.getPassword());
        assertThat(findPhotographer.getName()).isEqualTo(photographer.getName());
        assertThat(findPhotographer.getRegion()).isEqualTo(photographer.getRegion());
        assertThat(findPhotographer.getDescription()).isEqualTo(photographer.getDescription());
        assertThat(findPhotographer.getProfileImg()).isEqualTo(photographer.getProfileImg());
        
    }
}

