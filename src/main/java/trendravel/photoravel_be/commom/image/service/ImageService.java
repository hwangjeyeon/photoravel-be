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

        return uploadImagesAndReturnUrl(images);
    }

    private List<String> uploadImagesAndReturnUrl(List<MultipartFile> images) {
        List<String> rebuildImageName = getImagesName(images);
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            try{
                InputStream inputStream = images.get(i).getInputStream();
                urlList.add(s3Template.upload(bucketName,
                        rebuildImageName.get(i), inputStream).getURL().toString());
            }catch(IOException e){
                throw new RuntimeException();
            }
        }
        return urlList;
    }

    public List<String> updateImages(List<MultipartFile> newImages,
                                     List<String> deleteImages){
        if(deleteImages.isEmpty()){
            // 예외 처리
        }

        List<String> imageNames = sliceUrlAndGetImageNames(deleteImages);
        for (String imageName : imageNames) {
            s3Template.deleteObject(bucketName, imageName);
        }

        if(newImages.isEmpty()){
            // 예외 처리
        }


        return uploadImagesAndReturnUrl(newImages);
    }


    public void deleteAllImages(List<String> deleteImages){
        if(deleteImages.isEmpty()){
            // 예외 처리
        }
        List<String> imageNames = sliceUrlAndGetImageNames(deleteImages);
        for (String imageName : imageNames) {
            s3Template.deleteObject(bucketName, imageName);
        }
    }

    private List<String> sliceUrlAndGetImageNames(List<String> imageUrls) {
        return imageUrls.stream()
                .map(p -> p.substring(p.lastIndexOf("/")+1))
                .toList();
    }


    public List<String> getImagesName(List<MultipartFile> multipartFiles){
        return multipartFiles.stream()
                .map(p-> ImageNameRebuildUtils
                        .buildImageName(p.getOriginalFilename()))
                .toList();
    }

}
