package trendravel.photoravel_be.dto.response.results;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import trendravel.photoravel_be.dto.response.messages.ResultInfo;


@Data
@NoArgsConstructor
public class OnlyResultDto{

    private ResultInfo result;

}
