package trendravel.photoravel_be.commom.event;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ImageDeleteEvent {
    private List<String> deleteImages = new ArrayList<>();
}
