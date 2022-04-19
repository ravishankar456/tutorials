package com.ravi.fp.lambdas;

import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Function<String, String> uppercaseName = String::toUpperCase;
        System.out.println(uppercaseName.apply("ravi"));
    }

}
