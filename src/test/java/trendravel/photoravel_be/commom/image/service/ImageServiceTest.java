package trendravel.photoravel_be.commom.image.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ImageServiceTest {

    @Autowired
    ImageService imageService;

    MockMultipartFile mockMultipartFile;
    MockMultipartFile mockMultipartFile2;

    @BeforeEach
    void before() throws IOException{
        URL resource = getClass().getClassLoader().getResource("../resources/images/test.png");
        mockMultipartFile = new MockMultipartFile("image",
                "test.png", "image/png", new FileInputStream(resource.getFile()));

        URL resource2 = getClass().getClassLoader().getResource("../resources/images/test2.png");
        mockMultipartFile2 = new MockMultipartFile("image",
                "test2.png", "image/png", new FileInputStream(resource2.getFile()));
    }


    @DisplayName("이미지가 잘 업로드 되는지 테스트")
    @Test
    @Order(1)
    @Transactional
    void imageUploadTest() {
        List<MultipartFile> list = new ArrayList<>();
        list.add(mockMultipartFile);
        List<String> urls = imageService.getImagesName(list);
        assertThat(urls.get(0).substring(urls.get(0).lastIndexOf(".")+1)).isEqualTo("png");
        assertThat(urls.get(0).substring(urls.get(0).lastIndexOf("/")+1,
                urls.get(0).lastIndexOf("_"))).isEqualTo(
                        mockMultipartFile.getOriginalFilename().substring(0,
                                mockMultipartFile.getOriginalFilename().lastIndexOf(".")));
    }

    @DisplayName("이미지가 잘 수정되는지 테스트")
    @Test
    @Order(2)
    @Transactional
    void imageUpdateTest(){
        List<MultipartFile> list = new ArrayList<>();
        list.add(mockMultipartFile);
        List<String> urls = imageService.getImagesName(list);

        list.clear();
        list.add(mockMultipartFile2);
        urls = imageService.updateImages(list, urls);


        assertThat(urls.get(0).substring(urls.get(0).lastIndexOf("/")+1,
                urls.get(0).lastIndexOf("_"))).isEqualTo(
                mockMultipartFile2.getOriginalFilename().substring(0,
                        mockMultipartFile2.getOriginalFilename().lastIndexOf(".")));
    }

}