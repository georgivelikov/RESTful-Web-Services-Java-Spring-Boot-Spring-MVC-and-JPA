package com.appsdeveloperblog.app.ws.exception;

public class Utils {

    // Returns true if value is null, empty string (length == 0) or only white
    // spaces
    public static boolean isNullOrBlank(String value) {
	return value == null || value.isBlank();
    }

    // Returns true if value is null or empty string (length == 0)
    public static boolean isNullOrEmpty(String value) {
	return value == null || value.isEmpty();
    }
}
