package com.appsdeveloperblog.app.ws.shared.utils;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm z";
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final Random RANDOM = new SecureRandom();

    public String generatePublicId(int length) {
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

    public static String getDateString(Date date) {
	return DATE_FORMATTER.format(date);
    }

    private String generateRandomString(int length) {
	StringBuilder returnValue = new StringBuilder();

	for (int i = 0; i < length; i++) {
	    returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
	}

	return returnValue.toString();
    }
}
