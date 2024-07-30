package trendravel.photoravel_be.dto.response.results;

import lombok.Data;
import lombok.NoArgsConstructor;
import trendravel.photoravel_be.dto.response.messages.ResultInfo;

@Data
@NoArgsConstructor
public class DataResultDto<T>{

    private ResultInfo result;
    private T data;

}
