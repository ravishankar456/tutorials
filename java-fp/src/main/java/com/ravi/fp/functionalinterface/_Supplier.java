package com.ravi.fp.functionalinterface;

import java.util.function.Supplier;

public class _Supplier {

    public static void main(String[] args) {
        System.out.println(dbConnSupplier.get());
    }

    static Supplier<String> dbConnSupplier = () -> "jdbc://localhost:5432/users";
}
