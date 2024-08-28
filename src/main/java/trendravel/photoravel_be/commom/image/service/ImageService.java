package trendravel.photoravel_be.commom.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.error.ErrorCode;
import trendravel.photoravel_be.commom.exception.ImageSystemException;
import trendravel.photoravel_be.commom.image.util.ImageNameRebuildUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {


    private final AmazonS3 amazonS3;
    private final AmazonS3 minioClient;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;


    public List<String> uploadImages(List<MultipartFile> images){

        if(images == null){
            return null;
        }

//        return uploadImagesAndReturnUrl(images, amazonS3);
        return uploadImagesAndReturnUrl(images, minioClient);
    }


    private List<String> uploadImagesAndReturnUrl(List<MultipartFile> images, AmazonS3 storage) {
        List<String> rebuildImageName = getImagesName(images);
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            try{
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(images.get(i).getInputStream().available());
                storage.putObject(bucketName, rebuildImageName.get(i),
                        images.get(i).getInputStream(), metadata);
                urlList.add(storage.getUrl(bucketName, rebuildImageName.get(i)).toString());
            }catch(IOException e){
                throw new ImageSystemException(ErrorCode.IMAGES_UPLOAD_ERROR,e);
            }
        }
        return urlList;
    }

    public List<String> updateImages(List<MultipartFile> newImages,
                                     List<String> deleteImages){

        List<String> imageNames = sliceUrlAndGetImageNames(deleteImages);
        for (String imageName : imageNames) {
//            amazonS3.deleteObject(bucketName, imageName);
            minioClient.deleteObject(bucketName, imageName);
        }


        if(newImages == null){
            return new ArrayList<>();
        }


//        return uploadImagesAndReturnUrl(newImages, amazonS3);
        return uploadImagesAndReturnUrl(newImages, minioClient);
    }


    public void deleteAllImages(List<String> deleteImages){
        List<String> imageNames = sliceUrlAndGetImageNames(deleteImages);
        log.info("{}", imageNames.get(0));
        for (String imageName : imageNames) {
//            amazonS3.deleteObject(bucketName, imageName);
            log.info("{}", imageName);
            minioClient.deleteObject(bucketName, imageName);
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
