package aba3.lucid.common.validator;

import aba3.lucid.common.annotation.ValidList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ValidatorList implements ConstraintValidator<ValidList, List<?>> {

    @Override
    public boolean isValid(List<?> value, ConstraintValidatorContext context) {
        return value != null && !value.isEmpty();
    }
}
