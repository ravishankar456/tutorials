package com.ravi.fp.combinatorpattern;

import com.ravi.fp.combinatorpattern.CustomerValidator.ValidationResult;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Customer alice = new Customer("Alice", "alice@gmail.com", "+917867868767868", LocalDate.of(2000, 2, 24));
        //Combinator Pattern
        ValidationResult result = CustomerValidator.isEmailValid()
                .and(CustomerValidator.isPhoneNoValid())
                .and(CustomerValidator.isAdult())
                .apply(alice);
        System.out.println(result);
    }
}
