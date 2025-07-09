package es.nextdigital.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.IBANValidator;

public class IbanValidator
        implements ConstraintValidator<ValidIban, String> {

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext context) {
        if (iban == null || iban.isEmpty()) {
            return false;
        }
        return IBANValidator.getInstance().isValid(iban);
    }
}
