package trendravel.photoravel_be.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import trendravel.photoravel_be.commom.error.GuidebookErrorCode;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.guidebook.Guidebook;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.respository.guidebook.GuidebookRepository;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookRequestDto;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookUpdateDto;
import trendravel.photoravel_be.domain.guidebook.dto.response.GuidebookListResponseDto;
import trendravel.photoravel_be.domain.guidebook.dto.response.GuidebookResponseDto;
import trendravel.photoravel_be.domain.guidebook.service.GuidebookService;


import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class GuidebookServiceTest {
    
    @Autowired
    GuidebookService guidebookService;
    
    @Autowired
    GuidebookRepository guidebookRepository;
    
    @Autowired
    MemberRepository memberRepository;
    
    GuidebookRequestDto guidebookRequestDto1;
    GuidebookRequestDto guidebookRequestDto2;
    GuidebookUpdateDto guidebookUpdateDto;
    
    MemberEntity member;
    @BeforeEach
    void before() {
        
        guidebookRequestDto1 = new GuidebookRequestDto();
        guidebookRequestDto1.setUserId("mem");
        guidebookRequestDto1.setTitle("아산 여행하기");
        guidebookRequestDto1.setContent("아산에는");
        guidebookRequestDto1.setRegion(Region.아산);
        
        guidebookRequestDto2 = new GuidebookRequestDto();
        guidebookRequestDto2.setUserId("mem");
        guidebookRequestDto2.setTitle("천안 여행하기");
        guidebookRequestDto2.setContent("천안에는");
        guidebookRequestDto2.setRegion(Region.천안);
        
        guidebookUpdateDto = new GuidebookUpdateDto();
        guidebookUpdateDto.setTitle("제목 수정이요");
        guidebookUpdateDto.setContent("내용 수정이요");
        guidebookUpdateDto.setRegion(Region.천안);
        
        
        member = MemberEntity.builder()
                .email("aaa")
                .memberId("mem")
                .nickname("donguk")
                .name("신동욱")
                .password("1234")
                .profileImg("test.png")
                .build();
        
        memberRepository.save(member);
        
    }

    @Test
    @Order(1)
    @DisplayName("가이드북 CREATE 테스트")
    void create() {
        
        //when
        Long id = guidebookService.createGuidebook(guidebookRequestDto1).getId();
        
        //then
        Guidebook findGuidebook = guidebookRepository.findById(id).get();
        assertThat(findGuidebook.getTitle()).isEqualTo(guidebookRequestDto1.getTitle());
        assertThat(findGuidebook.getContent()).isEqualTo(guidebookRequestDto1.getContent());
        assertThat(findGuidebook.getRegion()).isEqualTo(guidebookRequestDto1.getRegion());
        
    }
    
    @Test
    @Order(2)
    @DisplayName("가이드북 목록 READ 테스트")
    void readList() {
        
        //given
        guidebookService.createGuidebook(guidebookRequestDto1);
        guidebookService.createGuidebook(guidebookRequestDto2);
        
        //when
        List<GuidebookListResponseDto> list1 = guidebookService.getGuidebookList("newest");
        List<GuidebookListResponseDto> list2 = guidebookService.getGuidebookList("천안");
        
        //then
        assertThat(list1.size()).isEqualTo(2);
        assertThat(list2.size()).isEqualTo(1);
    }
    
    @Test
    @Order(3)
    @DisplayName("가이드북 단일 READ 테스트")
    void readOne() {
        
        //given
        Long id = guidebookService.createGuidebook(guidebookRequestDto1).getId();
        
        //when
        GuidebookResponseDto findGuidebook = guidebookService.getGuidebook(id);
        
        //then
        assertThat(findGuidebook.getUserId()).isEqualTo(guidebookRequestDto1.getUserId());
        assertThat(findGuidebook.getTitle()).isEqualTo(guidebookRequestDto1.getTitle());
        assertThat(findGuidebook.getContent()).isEqualTo(guidebookRequestDto1.getContent());
        assertThat(findGuidebook.getRegion()).isEqualTo(guidebookRequestDto1.getRegion());
    }
    
    @Test
    @Order(4)
    @DisplayName("가이드북 UPDATE 테스트")
    void update() {
        
        //given
        Long id = guidebookService.createGuidebook(guidebookRequestDto1).getId();
        guidebookUpdateDto.setId(id);
        
        //when
        GuidebookResponseDto guidebookResponseDto = guidebookService.updateGuidebook(guidebookUpdateDto);
        
        //then
        assertThat(guidebookResponseDto.getTitle()).isEqualTo(guidebookUpdateDto.getTitle());
        assertThat(guidebookResponseDto.getContent()).isEqualTo(guidebookUpdateDto.getContent());
        assertThat(guidebookResponseDto.getRegion()).isEqualTo(guidebookUpdateDto.getRegion());
    }
    
    @Test
    @Order(5)
    @DisplayName("가이드북 DELETE 테스트")
    void delete() {
        
        //given
        Long id = guidebookService.createGuidebook(guidebookRequestDto1).getId();
        
        //when
        guidebookService.deleteGuidebook(id);
        
        //then
        assertThat(guidebookRepository.findById(id)).isEmpty();
    }
    
    @Test
    @Order(6)
    @DisplayName("가이드북 단일 READ Exception 테스트")
    void readEx() {
        
        assertThatThrownBy(() -> guidebookService.getGuidebook(999L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(GuidebookErrorCode.GUIDEBOOK_NOT_FOUND.getErrorDescription());
    }
    
    @Test
    @Order(8)
    @DisplayName("가이드북 UPDATE Exception 테스트")
    void updateEx() {
        guidebookUpdateDto.setId(999L);
        assertThatThrownBy(() -> guidebookService.updateGuidebook(guidebookUpdateDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(GuidebookErrorCode.GUIDEBOOK_NOT_FOUND.getErrorDescription());
    }
    
    @Test
    @Order(9)
    @DisplayName("가이드북 DELETE Exception 테스트")
    void deleteEx() {
        
        assertThatThrownBy(() -> guidebookService.getGuidebook(999L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(GuidebookErrorCode.GUIDEBOOK_NOT_FOUND.getErrorDescription());
    }
    
}
