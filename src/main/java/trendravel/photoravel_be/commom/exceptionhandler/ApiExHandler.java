package trendravel.photoravel_be.commom.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import trendravel.photoravel_be.commom.error.ErrorCodeIfs;
import trendravel.photoravel_be.commom.exception.ApiException;
import trendravel.photoravel_be.commom.response.Api;

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
                    Api.ERROR(errorCodeIfs.getHttpStatusCode(), apiException.getErrorDescription())
            );
    }
}
