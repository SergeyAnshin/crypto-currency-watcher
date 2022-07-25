package com.serg.ans.cryptocurrencywatcher.validator;

import com.serg.ans.cryptocurrencywatcher.validator.constraint.ExistingSymbol;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ExistingSymbolValidator implements ConstraintValidator<ExistingSymbol, String> {
    @Value("${existingSymbols}")
    private List<String> existingSymbols;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return existingSymbols.contains(value);
    }
}
