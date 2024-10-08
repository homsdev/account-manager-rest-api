package com.homs.account_rest_api.validator;

import com.homs.account_rest_api.annotations.ValidType;
import com.homs.account_rest_api.enums.TransactionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TypeValidator implements ConstraintValidator<ValidType, String> {

    private String type;

    @Override
    public void initialize(ValidType constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (type.equals("TRANSACTION_TYPE")) {
            return isValidTransactionType(s);
        }
        return true;
    }

    private boolean isValidTransactionType(String s) {
        for (TransactionType type : TransactionType.values()) {
            if (s.toUpperCase().equals(type.getValue())){
                return true;
            }
        }
        return false;
    }
}
