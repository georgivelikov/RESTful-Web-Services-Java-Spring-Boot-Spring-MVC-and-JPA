package com.appsdeveloperblog.app.ws.shared.utils;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final int ITERATIONS = 10000;
    private final int KEY_LENGTH = 256;

    public String generateUserId(int length) {
	return generateRandomString(length);
    }

    // Returns true if value is null, empty string (length == 0) or only white
    // spaces
    public static boolean isNullOrBlank(String value) {
	return value == null || value.isBlank();
    }

    // Returns true if value is null or empty string (length == 0)
    public static boolean isNullOrEmpty(String value) {
	return value == null || value.isEmpty();
    }

    private String generateRandomString(int length) {
	StringBuilder returnValue = new StringBuilder();

	for (int i = 0; i < length; i++) {
	    returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
	}

	return returnValue.toString();
    }
}
