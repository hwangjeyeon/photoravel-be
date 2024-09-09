package trendravel.photoravel_be.commom.image.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageSizeValidator implements ConstraintValidator<ImageSizeValid, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> multipartFiles,
                           ConstraintValidatorContext constraintValidatorContext) {


        return multipartFiles != null && multipartFiles.size() <= 10;
    }
}
