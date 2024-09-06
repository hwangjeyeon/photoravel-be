package trendravel.photoravel_be.commom.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ImageUploadEvent {
    private List<MultipartFile> uploadImages = new ArrayList<>();
}
