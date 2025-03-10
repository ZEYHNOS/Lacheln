package aba3.lucid.common.annotation.valid;

import aba3.lucid.validator.BinaryChoiceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BinaryChoiceValidator.class)
@Size(min = 1, max = 1, message = "Y/N 만 올 수 있습니다.")
@NotBlank(message = "선택 여부 값이 존재하지 않습니다.")
public @interface BinaryChoiceValid {

    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
