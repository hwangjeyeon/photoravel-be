package trendravel.photoravel_be.common.image.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Getter
@AllArgsConstructor
public class ImageUploadEvent {
    private List<MultipartFile> uploadImages;
    private List<String> rebuildImageName;
}
