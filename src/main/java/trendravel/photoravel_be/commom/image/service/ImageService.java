package trendravel.photoravel_be.commom.image.service;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.image.util.ImageNameRebuildUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {



    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;


    public List<String> uploadImages(List<MultipartFile> images){

        if(images.isEmpty()){
            // 예외 처리
        }

        List<String> rebuildImageName = getImagesName(images);
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            try{
                InputStream inputStream = images.get(i).getInputStream();
                urlList.add(s3Template.upload(bucketName, rebuildImageName.get(i), inputStream).getURL().toString());
            }catch(IOException e){
                throw new RuntimeException();
            }
        }

        return urlList;
    }





    public List<String> getImagesName(List<MultipartFile> multipartFiles){
        return multipartFiles.stream()
                .map(p-> ImageNameRebuildUtils
                        .buildImageName(p.getOriginalFilename()))
                .toList();
    }

}
