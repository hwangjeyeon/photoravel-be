package trendravel.photoravel_be.commom.eventHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import trendravel.photoravel_be.commom.event.ImageUploadEvent;
import trendravel.photoravel_be.commom.event.ImageDeleteEvent;
import trendravel.photoravel_be.commom.image.service.ImageService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageServiceEventHandler {

    private final ImageService imageService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleImageUploadExceptionEventListener(ImageUploadEvent imageUploadEvent){
        log.info("DB 저장 성공, S3 저장 이미지 저장");
        imageService.uploadImages(imageUploadEvent.getUploadImages(), imageUploadEvent.getRebuildImageName());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleImageDeleteExceptionEventListener(ImageDeleteEvent imageDeleteEvent){
        log.info("DB 삭제 성공, S3 이미지 삭제");
        imageService.deleteAllImages(imageDeleteEvent.getDeleteImages());
    }


}
