package com.ravi.fp.optionals;

import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        Optional<String> nullValue = Optional.ofNullable(null);
        Optional<String> nullableValue = Optional.ofNullable("Hello World");
        System.out.println(nullValue.orElseGet(() -> "Default Value"));
        nullableValue.ifPresent(System.out::println);
        nullValue.ifPresentOrElse(System.out::println, () -> {
            System.out.println("Null Value");
        });
    }
}
