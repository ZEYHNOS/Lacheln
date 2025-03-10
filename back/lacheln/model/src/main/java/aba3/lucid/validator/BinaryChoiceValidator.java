package aba3.lucid.validator;

import aba3.lucid.common.annotation.valid.BinaryChoiceValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BinaryChoiceValidator implements ConstraintValidator<BinaryChoiceValid, String> {
    
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return false;
        }

        return s.equals("Y") || s.equals("y") || s.equals("N") || s.equals("n");
    }
    
}
