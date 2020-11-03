package pierobon.paiva.lucilio.bluefood.infrastructure.web.validator;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import pierobon.paiva.lucilio.bluefood.util.FileType;

public class UploadValidator implements ConstraintValidator<UploadConstraint, MultipartFile> {
	
	private List<FileType> acceptedFileTypes;

	@Override
	public void initialize(UploadConstraint constraintAnnotation) {
		acceptedFileTypes = Arrays.asList(constraintAnnotation.acceptedTypes());
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		
		if (value == null) {
			return true;
		}
		
		for (FileType fileType : acceptedFileTypes) {
			if (fileType.sameOf(value.getContentType())) {
				return true;
			}
		}
		
		return false;
	}
	
	

}
