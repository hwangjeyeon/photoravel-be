package trendravel.photoravel_be.commom.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import trendravel.photoravel_be.commom.error.ErrorCode;
import trendravel.photoravel_be.commom.error.ErrorCodeIfs;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {

    private Integer resultCode;
    private String resultMessage;

    public static Result OK() {
        return Result.builder()
                .resultCode(ErrorCode.OK.getHttpStatusCode())
                .resultMessage(ErrorCode.OK.getErrorDescription())
                .build();
    }

    public static Result CREATED() {
        return Result.builder()
                .resultCode(ErrorCode.CREATED.getHttpStatusCode())
                .resultMessage(ErrorCode.CREATED.getErrorDescription())
                .build();
    }

    public static Result READ() {
        return Result.builder()
                .resultCode(ErrorCode.READ.getHttpStatusCode())
                .resultMessage(ErrorCode.READ.getErrorDescription())
                .build();
    }

    public static Result UPDATED() {
        return Result.builder()
                .resultCode(ErrorCode.UPDATED.getHttpStatusCode())
                .resultMessage(ErrorCode.UPDATED.getErrorDescription())
                .build();
    }

    public static Result DELETED() {
        return Result.builder()
                .resultCode(ErrorCode.DELETED.getHttpStatusCode())
                .resultMessage(ErrorCode.DELETED.getErrorDescription())
                .build();
    }

    public static Result ERROR(ErrorCodeIfs errorCodeIfs){
        return Result.builder()
                .resultCode(errorCodeIfs.getHttpStatusCode())
                .resultMessage(errorCodeIfs.getErrorDescription())
                .build()
                ;
    }


    // validation용 ERROR
    public static Result ERROR(
            Integer errorCode,
            String errorDescription
    ){
        return Result.builder()
                .resultCode(errorCode)
                .resultMessage(errorDescription)
                .build()
                ;
    }



}
