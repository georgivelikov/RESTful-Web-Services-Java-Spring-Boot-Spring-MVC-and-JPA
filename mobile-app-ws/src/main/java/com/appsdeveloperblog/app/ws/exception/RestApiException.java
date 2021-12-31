package com.appsdeveloperblog.app.ws.exception;

public class RestApiException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -4327194167750066550L;

    private String sourceExceptionMessage;

    public RestApiException(String message) {
	super(message);
    }

    public RestApiException(String message, String sourceExceptionMessage) {
	super(message);
	this.sourceExceptionMessage = sourceExceptionMessage;
    }

    public String getSourceExceptionMessage() {
	return this.sourceExceptionMessage;
    }
}
