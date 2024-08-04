package trendravel.photoravel_be.commom.results;

import lombok.Data;
import lombok.NoArgsConstructor;
import trendravel.photoravel_be.commom.results.messages.ResultInfo;

@Data
@NoArgsConstructor
public class DataResultDto<T>{

    private ResultInfo result;
    private T data;

}
