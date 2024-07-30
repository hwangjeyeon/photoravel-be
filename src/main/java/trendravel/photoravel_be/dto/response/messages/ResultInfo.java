package trendravel.photoravel_be.dto.response.messages;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResultInfo {

    private HttpStatus code;
    private String message;
}
