package com.spring.ApiSystem.shared.security;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PasswordGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "@#$%&*!?";
    private static final String ALL = LOWER + UPPER + DIGITS + SYMBOLS;

    private PasswordGenerator() {}

    public static String generate(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length must be at least 8");
        }

        List<Character> chars = new ArrayList<>();
        chars.add(randomChar(LOWER));
        chars.add(randomChar(UPPER));
        chars.add(randomChar(DIGITS));
        chars.add(randomChar(SYMBOLS));

        for (int i = chars.size(); i < length; i++) {
            chars.add(randomChar(ALL));
        }

        Collections.shuffle(chars, SECURE_RANDOM);

        StringBuilder sb = new StringBuilder(length);
        for (char c : chars) sb.append(c);

        return sb.toString();
    }

    private static char randomChar(String source) {
        return source.charAt(SECURE_RANDOM.nextInt(source.length()));
    }
}
