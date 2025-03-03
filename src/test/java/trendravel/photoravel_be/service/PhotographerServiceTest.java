package trendravel.photoravel_be.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.common.exception.error.PhotographerErrorCode;
import trendravel.photoravel_be.common.exception.ApiException;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.db.respository.photographer.PhotographerRepository;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerRequestDto;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerUpdateDto;
import trendravel.photoravel_be.domain.photographer.dto.response.PhotographerListResponseDto;
import trendravel.photoravel_be.domain.photographer.dto.response.PhotographerSingleResponseDto;
import trendravel.photoravel_be.domain.photographer.service.PhotographerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;



/**
 * 이미지 테스트 코드는 작성 하지 말것.
 * 1. CI/CD 배포 시, Mock파일 사용 관련해서 에러 발생
 * 2. 실제 s3 버킷을 사용할 수 밖에 없어 비용 낭비 발생
 *
 */
//@SpringBootTest
//@testMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
//@ActiveProfiles("test")
public class PhotographerServiceTest {
    
    @Autowired
    PhotographerService photographerService;
    
    @Autowired
    PhotographerRepository photographerRepository;
    
    PhotographerRequestDto photo1, photo2;
    PhotographerUpdateDto photographerUpdateDto;
    
    
    MockMultipartFile mockMultipartFile;


    @BeforeEach
    void before() throws IOException {
        
        //   resource/images/test.png
//        URL resource = getClass().getClassLoader().getResource("images/test.png");
//        mockMultipartFile = new MockMultipartFile("image",
//                "test.png", "image/png", new FileInputStream(resource.getFile()));
        
        
        photo1 = new PhotographerRequestDto();
        photo1.setAccountId("아이디");
        photo1.setPassword("비밀번호");
        photo1.setDescription("설명");
        photo1.setName("신동욱");
        photo1.setRegion(Region.아산);
        photo1.setCareerYear(1);
        
        photo2 = new PhotographerRequestDto();
        photo2.setAccountId("아이디22");
        photo2.setPassword("비밀번호22");
        photo2.setDescription("설명22");
        photo2.setName("김동욱");
        photo2.setRegion(Region.천안);
        photo2.setCareerYear(2);
        
        photographerUpdateDto = new PhotographerUpdateDto();
        photographerUpdateDto.setAccountId("아이디"); //수정용 아니고 검색용
        photographerUpdateDto.setName("이름이걸로수정");
        photographerUpdateDto.setDescription("설명이걸로수정");
        photographerUpdateDto.setPassword("비밀번호이걸로수정");
        photographerUpdateDto.setRegion(Region.천안);
        
        
    }
    
//    //@test
    @Order(1)
    @DisplayName("사진작가 CREATE 테스트")
    void create() {
        
        List<MultipartFile> list = new ArrayList<>();
        list.add(mockMultipartFile);
        
        //when
        photographerService.createPhotographer(photo1, list);
        
        //then
        Photographer find = photographerRepository.findByAccountId("아이디").get();
        assertThat(find.getAccountId()).isEqualTo(photo1.getAccountId());
        assertThat(find.getPassword()).isEqualTo(photo1.getPassword());
        assertThat(find.getDescription()).isEqualTo(photo1.getDescription());
        assertThat(find.getRegion()).isEqualTo(photo1.getRegion());
    }

    /**
     * 테스트 빌드 시, 실제 버킷에 데이터를 넣는 문제 발생. 따라서 해당 테스트는 삭제 필요.
     */
//    //@test
    @Order(2)
    @DisplayName("사진작가 목록 READ 테스트")
    void getList() {
        
        //given
        List<MultipartFile> list = new ArrayList<>();
        list.add(mockMultipartFile);
        photographerService.createPhotographer(photo1, list);
        photographerService.createPhotographer(photo2, list);
        
        //when
        List<PhotographerListResponseDto> all = photographerService.getPhotographerList("career");
        List<PhotographerListResponseDto> region = photographerService.getPhotographerList("천안");
        
        //then
        assertThat(all.size()).isEqualTo(2);
        assertThat(region.size()).isEqualTo(1);
        
    }
    
    //@test
    @Order(3)
    @DisplayName("사진작가 단일 READ 테스트")
    void getOne() {
        
        //given
        List<MultipartFile> list = new ArrayList<>();
        list.add(mockMultipartFile);
        photographerService.createPhotographer(photo1, list);
        
        //when
        PhotographerSingleResponseDto find = photographerService.getPhotographer(photo1.getAccountId());
        
        //then
        assertThat(find.getAccountId()).isEqualTo(photo1.getAccountId());
        assertThat(find.getDescription()).isEqualTo(photo1.getDescription());
        assertThat(find.getRegion()).isEqualTo(photo1.getRegion());
    }
    
    //@test
    @Order(4)
    @DisplayName("사진작가 UPDATE 테스트")
    void update() {
        
        //given
        List<MultipartFile> list = new ArrayList<>();
        list.add(mockMultipartFile);
        photographerService.createPhotographer(photo1, list);
        
        //when
        PhotographerSingleResponseDto find = photographerService.updatePhotographer(photographerUpdateDto);
        
        //then
        assertThat(find.getAccountId()).isEqualTo(photographerUpdateDto.getAccountId());
        assertThat(find.getName()).isEqualTo(photographerUpdateDto.getName());
        assertThat(find.getDescription()).isEqualTo(photographerUpdateDto.getDescription());
        assertThat(find.getRegion()).isEqualTo(photographerUpdateDto.getRegion());
    }
    
    //@test
    @Order(5)
    @DisplayName("사진작가 DELETE 테스트")
    void delete() {
        
        //given
        List<MultipartFile> list = new ArrayList<>();
        list.add(mockMultipartFile);
        photographerService.createPhotographer(photo1, list);
        
        //when
        photographerService.deletePhotographer(photo1.getAccountId());
        
        //then
        assertThat(photographerRepository.findByAccountId(photo1.getAccountId())).isEmpty();
    }
    
    //@test
    @Order(6)
    @DisplayName("사진작가 READ 예외 테스트")
    void readEx() {
        
        assertThatThrownBy(() -> photographerService.getPhotographer("ㅇㅇ"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND.getErrorDescription());
    }
    
    //@test
    @Order(7)
    @DisplayName("사진작가 UPDATE 예외 테스트")
    void updateEx() {
        
        assertThatThrownBy(() -> photographerService.updatePhotographer(photographerUpdateDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND.getErrorDescription());
    }
    
    //@test
    @Order(8)
    @DisplayName("사진작가 DELETE 예외 테스트")
    void deleteEx() {
        
        assertThatThrownBy(() -> photographerService.deletePhotographer("ezez"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND.getErrorDescription());
    }
    
}
