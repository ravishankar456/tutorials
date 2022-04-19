package com.ravi.fp.functionalinterface;

import com.ravi.fp.Gender;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class _Predicate {

    public static void main(String[] args) {
        System.out.println(isIndianPhoneNo.test("+919611677558"));
        System.out.println(isMaleIndianPhoneNo.test("+919611677558", Gender.FEMALE));
    }

    static Predicate<String> isIndianPhoneNo = phoneNo -> phoneNo.startsWith("+91") && phoneNo.length() == 13;
    static BiPredicate<String, Gender> isMaleIndianPhoneNo = (phoneNo, gender) -> phoneNo.startsWith("+91") && phoneNo.length() == 13 && Gender.MALE == gender;
}
