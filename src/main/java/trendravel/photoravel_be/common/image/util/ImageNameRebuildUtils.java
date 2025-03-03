package trendravel.photoravel_be.common.image.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ImageNameRebuildUtils {

    private static final String IMAGE_EXTENSION_SEPARATOR = ".";
    private static final String FOLDER_NAME = "photoravle-cloudfront/";
    private static final String CLOUD_FRONT = "https://photravle-images.shop/";
    private static final String FOLDER_SEPARATOR = "/";

    public static String buildImageName(String originalFileName){
        int imageExtensionIndex = originalFileName.lastIndexOf(IMAGE_EXTENSION_SEPARATOR);
        log.info("{}",imageExtensionIndex);
        String imageExtension = originalFileName.substring(imageExtensionIndex);
        String imageName = originalFileName.substring(0, imageExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());

        return FOLDER_NAME + imageName + "_" + now + imageExtension;
    }

    public static String buildSaveImageName(String imageName){
        String imageOriginalName = imageName.substring(imageName.lastIndexOf(FOLDER_SEPARATOR)+1);

        return CLOUD_FRONT + imageOriginalName;
    }

    public static String findDeleteImageName(String imageName){
        String imageOriginalName = imageName.substring(imageName.lastIndexOf(FOLDER_SEPARATOR)+1);
        log.info("{}",imageOriginalName);
        return FOLDER_NAME + imageOriginalName;
    }

}
