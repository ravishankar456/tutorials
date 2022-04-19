package com.ravi.fp.streams;

import com.ravi.fp.Person;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ravi.fp.Gender.FEMALE;
import static com.ravi.fp.Gender.MALE;

public class _Streams {

    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("John", MALE),
                new Person("Maria", FEMALE),
                new Person("Aisha", FEMALE),
                new Person("Alex", MALE),
                new Person("Alicia", FEMALE)
        );

        //Map - Transformation
        people.stream()
                .map(person -> person.getGender())
                .collect(Collectors.toSet())
                .forEach(System.out::println);

        //Map Int
        people.stream()
                .map(person -> person.getName())
                .mapToInt(String::length)
                .forEach(System.out::println);

        //Match
        System.out.println(people.stream().allMatch(isFemale));
        System.out.println(people.stream().anyMatch(isFemale));
        System.out.println(people.stream().noneMatch(isFemale));
    }

    static Predicate<Person> isFemale = person -> FEMALE.equals(person.getGender());
}