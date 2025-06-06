package aba3.lucid.common.annotation;

import aba3.lucid.common.validator.ValidatorList;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidatorList.class)
public @interface ValidList {

    String message() default "리스트가 비어있습니다.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
