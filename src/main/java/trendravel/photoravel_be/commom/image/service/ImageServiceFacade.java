package trendravel.photoravel_be.commom.image.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.commom.event.ImageDeleteEvent;
import trendravel.photoravel_be.commom.event.ImageUploadEvent;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceFacade {

    private final ApplicationEventPublisher eventPublisher;
    private final ImageService imageService;

    @Transactional
    public List<String> uploadImageFacade(List<MultipartFile> imageFiles) {
        List<String> rebuildImageNames = imageService.getImagesName(imageFiles);
        eventPublisher.publishEvent(new ImageUploadEvent(imageFiles, rebuildImageNames));
        return rebuildImageNames;
    }

    @Transactional
    public List<String> updateImageFacade(List<MultipartFile> newImages,
                                          List<String> deleteImages){

        if(newImages == null || newImages.isEmpty()){
            return new ArrayList<>();
        }
        List<String> rebuildImageNames = imageService.getImagesName(newImages);
        eventPublisher.publishEvent(new ImageDeleteEvent(deleteImages));
        eventPublisher.publishEvent(new ImageUploadEvent(newImages, rebuildImageNames));


        return rebuildImageNames;
    }

    @Transactional
    public void deleteAllImagesFacade(List<String> deleteImages){
        eventPublisher.publishEvent(new ImageDeleteEvent(deleteImages));
    }

}
