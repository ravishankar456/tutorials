package com.ravi.fp.callbacks;

import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        hello("John", "Montana", firstName -> handleMissingLastName(firstName));
        hello("John", null, firstName -> handleMissingLastName(firstName));
    }

    private static void handleMissingLastName(String firstName) {
        System.out.println("No last name provided for : " + firstName);
    }

    static void hello(String firstName, String lastName, Consumer<String> callbackMethod) {
        System.out.println(firstName);
        if (lastName != null) {
            System.out.println(lastName);
        } else {
            callbackMethod.accept(firstName);
        }
    }
}
