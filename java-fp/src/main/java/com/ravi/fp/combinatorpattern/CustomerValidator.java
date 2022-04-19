package com.ravi.fp.combinatorpattern;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.Function;

import static com.ravi.fp.combinatorpattern.CustomerValidator.ValidationResult;
import static com.ravi.fp.combinatorpattern.CustomerValidator.ValidationResult.INVALID_EMAIL;
import static com.ravi.fp.combinatorpattern.CustomerValidator.ValidationResult.INVALID_PHONE_NUMBER;
import static com.ravi.fp.combinatorpattern.CustomerValidator.ValidationResult.NOT_ADULT;
import static com.ravi.fp.combinatorpattern.CustomerValidator.ValidationResult.SUCCESS;

@FunctionalInterface
public interface CustomerValidator extends Function<Customer, ValidationResult> {

    enum ValidationResult {
        SUCCESS,
        INVALID_PHONE_NUMBER,
        INVALID_EMAIL,
        NOT_ADULT
    }

    static CustomerValidator isEmailValid() {
        return customer -> customer.getEmail().contains("@") ? SUCCESS : INVALID_EMAIL;
    }

    static CustomerValidator isPhoneNoValid() {
        return customer -> customer.getPhoneNumber().startsWith("+91") ? SUCCESS : INVALID_PHONE_NUMBER;
    }

    static CustomerValidator isAdult() {
        return customer -> Period.between(customer.getDob(), LocalDate.now()).getYears() > 18 ? SUCCESS : NOT_ADULT;
    }

    default CustomerValidator and(CustomerValidator other) {
        return customer -> {
            ValidationResult result = this.apply(customer);
            return result.equals(SUCCESS) ? other.apply(customer) : result;
        };
    }
}
