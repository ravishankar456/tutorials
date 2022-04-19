package com.ravi.fp.functionalinterface;

import com.ravi.fp.Customer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class _Consumer {

    public static void main(String[] args) {
        greet.accept(new Customer("John", "768767988989"));
        greetPII.accept(new Customer("John", "768767988989"), false);
    }

    static Consumer<Customer> greet = customer ->
            System.out.println("Hello " + customer.getName() + "! Thanks for registering phone number " + customer.getPhoneNumber());

    static BiConsumer<Customer, Boolean> greetPII = (customer, showPhoneNumber) ->
            System.out.println("Hello " + customer.getName() + "! Thanks for registering phone number " + (showPhoneNumber ? customer.getPhoneNumber() : "xxxxxxxxxx"));
}
