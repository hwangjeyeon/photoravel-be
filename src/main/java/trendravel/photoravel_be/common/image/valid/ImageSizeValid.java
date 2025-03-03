package trendravel.photoravel_be.common.image.valid;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = ImageSizeValidator.class)
@Target(value = {ElementType.TYPE, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ImageSizeValid {
    String message() default "한번에 요청 할 수 있는 최대 이미지 개수(10개) 초과 또는 이미지 미전송 (null)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
