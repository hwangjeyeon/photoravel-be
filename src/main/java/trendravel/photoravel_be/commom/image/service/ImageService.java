package trendravel.photoravel_be.commom.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import trendravel.photoravel_be.commom.error.ErrorCode;
import trendravel.photoravel_be.commom.exception.ImageSystemException;
import trendravel.photoravel_be.commom.image.util.ImageNameRebuildUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService{


    private final S3Client amazonS3;
    private final S3Client minioClient;



    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public void uploadImages(List<MultipartFile> images, List<String> rebuildImageName){
        if(images == null){
            return;
        }
        uploadImageToStorage(images, rebuildImageName, minioClient);
//      uploadImageToStorage(images, amazonS3);
    }


    private void uploadImageToStorage (List<MultipartFile> images, List<String> rebuildImageNames, S3Client storage) {

        for (int i = 0; i < images.size(); i++) {
            try{
                PutObjectRequest objectRequest = PutObjectRequest
                        .builder()
                        .bucket(bucketName)
                        .key(rebuildImageNames.get(i))
                        .build();
                storage.putObject(objectRequest,
                        RequestBody.fromByteBuffer(ByteBuffer.wrap(images.get(i).getBytes())));
            }catch(IOException e){
                throw new ImageSystemException(ErrorCode.IMAGES_UPLOAD_ERROR,e);
            }
        }
    }



    /**
     * db에 들어있는 경우만 지우기!
     */

    public void deleteAllImages(List<String> deleteImages){
        deleteImagesInStorage(deleteImages, minioClient);
//        deleteImagesInStorage(deleteImages, amazonS3);
    }

    private void deleteImagesInStorage(List<String> deleteImages, S3Client storage) {
        log.info("{}", deleteImages.get(0));
        for (String imageName : deleteImages) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(imageName)
                    .build();
            storage.deleteObject(deleteObjectRequest);
        }
    }

    private List<String> sliceUrlAndGetImageNames(List<String> imageUrls) {
        return imageUrls.stream()
                .map(p -> p.substring(p.lastIndexOf("/")+1))
                .toList();
    }


    List<String> getImagesName(List<MultipartFile> multipartFiles){
        return multipartFiles.stream()
                .map(p-> ImageNameRebuildUtils
                        .buildImageName(p.getOriginalFilename()))
                .toList();
    }

}
