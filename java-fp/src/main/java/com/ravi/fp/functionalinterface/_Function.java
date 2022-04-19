package com.ravi.fp.functionalinterface;

import java.util.function.BiFunction;
import java.util.function.Function;

public class _Function {

    public static void main(String[] args) {
        System.out.println(incrementByOne.andThen(multiplyByTen).apply(4));
        System.out.println(incrementByOneAndMultiply.apply(4, 10));
    }

    static Function<Integer, Integer> incrementByOne = number -> number + 1;
    static Function<Integer, Integer> multiplyByTen = number -> number * 10;

    static BiFunction<Integer, Integer, Integer> incrementByOneAndMultiply = (number, multiplier) -> (number + 1) * multiplier;
}
