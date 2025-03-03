package trendravel.photoravel_be.common.image.event;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageDeleteEvent {
    private List<String> deleteImages;
}
