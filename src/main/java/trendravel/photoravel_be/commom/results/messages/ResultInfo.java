package trendravel.photoravel_be.commom.results.messages;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResultInfo {

    private HttpStatus code;
    private String message;
}
