package trendravel.photoravel_be.commom.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trendravel.photoravel_be.commom.error.ErrorCodeIfs;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Api<T> {

    private Result result;
    private T data;

    // 20x
    public static <T> Api<T> OK(T result){
        Api<T> response = new Api<>();
        response.result = Result.OK();
        response.data = result;
        return response;
    }

    public static <T> Api<T> CREATED(T result){
        Api<T> response = new Api<>();
        response.result = Result.CREATED();
        response.data = result;
        return response;
    }

    public static <T> Api<T> READ(T result){
        Api<T> response = new Api<>();
        response.result = Result.READ();
        response.data = result;
        return response;
    }

    public static <T> Api<T> UPDATED(T result){
        Api<T> response = new Api<>();
        response.result = Result.UPDATED();
        response.data = result;
        return response;
    }

    public static <T> Api<T> DELETE(T result){
        Api<T> response = new Api<>();
        response.result = Result.DELETED();
        response.data = result;
        return response;
    }

    public static Api<Object> ERROR(Result result) {
        Api<Object> response = new Api<>();
//        response.resultCode 는 exHandeler에서 설정해줌
        response.result = result;
        return response;
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs) {
        Api<Object> response = new Api<>();
        response.result = Result.ERROR(errorCodeIfs);
        return response;
    }


    public static Api<Object> ERROR(Integer errorCode, String errorDescription) {
        Api<Object> response = new Api<>();
        response.result = Result.ERROR(errorCode, errorDescription);
        return response;
    }


    public static Api<Object> NOT_VALID(Integer errorCode, String errorDescription) {
        Api<Object> response = new Api<>();
        response.result = Result.ERROR(errorCode, errorDescription);
        return response;
    }



}
