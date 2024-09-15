package trendravel.photoravel_be.commom.image.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ImageNameRebuildUtils {

    public static final String IMAGE_EXTENSION_SEPARATOR = ".";

    public static String getImageName(String originalFileName){
        return originalFileName.substring(0,
                originalFileName.lastIndexOf(IMAGE_EXTENSION_SEPARATOR));
    }

    public static String buildImageName(String originalFileName){
        int imageExtensionIndex = originalFileName.lastIndexOf(IMAGE_EXTENSION_SEPARATOR);
        log.info("{}",imageExtensionIndex);
        String imageExtension = originalFileName.substring(imageExtensionIndex);
        String imageName = originalFileName.substring(0, imageExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());

        return imageName + "_" + now + imageExtension;
    }

}
