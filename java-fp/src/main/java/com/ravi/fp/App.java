package com.ravi.fp;

import java.util.List;
import java.util.function.Predicate;

import static com.ravi.fp.Gender.FEMALE;
import static com.ravi.fp.Gender.MALE;

public class App {

    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("John", MALE),
                new Person("Maria", FEMALE),
                new Person("Aisha", FEMALE),
                new Person("Alex", MALE),
                new Person("Alicia", FEMALE)
        );
        printFemaleCount(people);
    }

    private static void printFemaleCount(List<Person> people) {
        people.stream()
                .filter(femalePredicate)
                .forEach(System.out::println);
    }

    private static Predicate<Person> femalePredicate =
            person -> person.getGender().equals(FEMALE);
}
