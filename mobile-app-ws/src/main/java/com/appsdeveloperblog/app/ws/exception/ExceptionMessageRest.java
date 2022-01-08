package com.appsdeveloperblog.app.ws.exception;

public class ExceptionMessageRest {

    private String timestamp;
    private String message;
    private String additionalInformation;
    private String path;
    private String method;

    public ExceptionMessageRest() {
    }

    public ExceptionMessageRest(String timestamp, String path, String method, String message) {
	this.timestamp = timestamp;
	this.path = path;
	this.setMethod(method);
	this.message = message;

    }

    public ExceptionMessageRest(String timestamp, String path, String method, String message,
	    String additionalInformation) {
	this(timestamp, path, method, message);
	this.additionalInformation = additionalInformation;
    }

    public String getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getAdditionalInformation() {
	return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
	this.additionalInformation = additionalInformation;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public String getMethod() {
	return method;
    }

    public void setMethod(String method) {
	this.method = method;
    }

}
