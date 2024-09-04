package trendravel.photoravel_be.commom.exceptionhandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import trendravel.photoravel_be.commom.error.ErrorCode;
import trendravel.photoravel_be.commom.exception.ImageSystemException;
import trendravel.photoravel_be.commom.response.Api;

import java.util.*;
import java.util.stream.Collectors;

import static trendravel.photoravel_be.commom.error.ErrorCode.ENUM_TYPE_NOT_READABLE;

@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)
@Slf4j
public class GlobalExHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Api<?>> globalExHandler(
            Exception e
    ){
        log.error("", e);

        return ResponseEntity
                .status(500)
                .body(Api.ERROR(ErrorCode.SERVER_ERROR));
    }


    @ExceptionHandler(value = ImageSystemException.class)
    public ResponseEntity<Api<?>> imageExceptionHandler(
            ImageSystemException imageSystemException){
        log.error("{}, \n {}",imageSystemException.getErrorDescription(), imageSystemException.getMessage());

        return ResponseEntity
                .status(500)
                .body(
                        Api.ERROR(imageSystemException.getErrorCodeIfs().getHttpStatusCode()
                                , imageSystemException.getErrorDescription())
                );
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Api<?>> validationExceptionHandler(
            ConstraintViolationException e
    ){

        ArrayList<String> errors = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toCollection(ArrayList::new));

        return ResponseEntity
                .status(500)
                .body(Api.ERROR(500,
                        errors.stream().toList().toString()));
    }


    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Api<?>> httpMessageNotReadableExceptionHandler(
            HttpMessageNotReadableException e
    ){


        return ResponseEntity
                .status(500)
                .body(Api.ERROR(ENUM_TYPE_NOT_READABLE.getHttpStatusCode(),
                        ENUM_TYPE_NOT_READABLE.getErrorDescription()));
    }



}
