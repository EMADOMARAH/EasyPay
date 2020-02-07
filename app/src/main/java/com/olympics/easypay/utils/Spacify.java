package com.olympics.easypay.utils;

public class Spacify {
    public static String take(String input) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (i % 4 == 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(input.charAt(i));
        }
        return stringBuilder.toString().trim();
    }
}
