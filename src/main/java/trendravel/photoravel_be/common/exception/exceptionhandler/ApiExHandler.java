package trendravel.photoravel_be.common.exception.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import trendravel.photoravel_be.common.exception.ApiException;
import trendravel.photoravel_be.common.exception.error.ErrorCodeIfs;
import trendravel.photoravel_be.util.response.Api;

import static trendravel.photoravel_be.common.exception.error.ErrorCode.*;

@RestControllerAdvice
@Slf4j
@Order(value = Integer.MIN_VALUE)
public class ApiExHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<Api<Object>> apiExceptionHandler(
            ApiException apiException
    ){

        log.error("{}", apiException.getErrorDescription());

        ErrorCodeIfs errorCodeIfs = apiException.getErrorCodeIfs();
        return ResponseEntity
            .status(errorCodeIfs.getHttpStatusCode())
            .body(
                    Api.ERROR(errorCodeIfs.getHttpStatusCode(),
                            apiException.getErrorDescription())
            );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Api<Object>> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException e
    ){

        log.info("{}",e.getBindingResult().
                getFieldError()
                .getDefaultMessage());


        return ResponseEntity
                .status(e.getStatusCode())
                .body(
                        Api.NOT_VALID(INPUT_FORMAT_ERROR.getHttpStatusCode(),
                                e.getBindingResult().
                                        getFieldError()
                                        .getDefaultMessage()
                        )
                );
    }

    @ExceptionHandler(value = HandlerMethodValidationException.class)
    public ResponseEntity<Api<?>> handlerMethodValidationExceptionHandler(
            HandlerMethodValidationException e
    ){

        log.info("{}",e.getAllValidationResults().get(0).getResolvableErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList());

        return ResponseEntity
                .status(INPUT_FORMAT_ERROR.getHttpStatusCode())
                .body(Api.ERROR(INPUT_FORMAT_ERROR.getHttpStatusCode(),
                        e.getAllValidationResults().get(0).getResolvableErrors()
                                .stream()
                                .map(MessageSourceResolvable::getDefaultMessage)
                                .toList()
                                .toString()
                ));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Api<?>> httpMessageNotReadableExceptionHandler(
            HttpMessageNotReadableException e
    ){

        return ResponseEntity
                .status(HTTP_INPUT_NOT_READABLE.getHttpStatusCode())
                .body(Api.ERROR(HTTP_INPUT_NOT_READABLE.getHttpStatusCode(),
                        HTTP_INPUT_NOT_READABLE.getErrorDescription()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Api<?>> maxUploadSizeExceededExceptionHandler(){
        return ResponseEntity
                .status(IMAGE_SIZE_EXCEED_ERROR.getHttpStatusCode())
                .body(Api.ERROR(IMAGE_SIZE_EXCEED_ERROR.getHttpStatusCode(),
                        IMAGE_SIZE_EXCEED_ERROR.getErrorDescription()
                ));
    }

}
