package trendravel.photoravel_be.domain.location.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.MediaType;


@Schema(description = "장소 CREATE/UPDATE(이미지 미포함) 요청 DTO",
        contentEncoding = MediaType.APPLICATION_JSON_VALUE)
@Data
public class LocationRequestDto {

    @Schema(description = "장소ID")
    private Long locationId;
    @Schema(description = "위도")
    @NotNull(message = "필수 입력사항입니다.")
    private double latitude;
    @Schema(description = "경도")
    @NotNull(message = "필수 입력사항입니다.")
    private double longitude;
    @Schema(description = "주소")
    @NotBlank(message = "공백/null 입력은 미허용됩니다.")
    @Length(max = 50, message = "최대 길이는 50글자입니다.")
    private String address;
    @Schema(description = "설명")
    @NotBlank(message = "공백/null 입력은 미허용됩니다.")
    private String description;
    @Schema(description = "이름")
    @NotBlank(message = "공백/null 입력은 미허용됩니다.")
    @Length(max = 50, message = "최대 길이는 50글자입니다.")
    private String name;
    @Schema(description = "유저명")
    private String userId;

}
