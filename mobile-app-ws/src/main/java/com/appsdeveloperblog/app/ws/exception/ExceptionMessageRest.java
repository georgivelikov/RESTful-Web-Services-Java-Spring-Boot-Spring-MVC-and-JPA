package com.appsdeveloperblog.app.ws.exception;

import java.util.Date;

public class ExceptionMessageRest {

    private Date timestamp;
    private String message;
    private String additionalInformation;
    private String path;
    private String method;

    public ExceptionMessageRest() {
    }

    public ExceptionMessageRest(Date timestamp, String path, String method, String message) {
	this.timestamp = timestamp;
	this.path = path;
	this.setMethod(method);
	this.message = message;

    }

    public ExceptionMessageRest(Date timestamp, String path, String method, String message,
	    String additionalInformation) {
	this(timestamp, path, method, message);
	this.additionalInformation = additionalInformation;
    }

    public Date getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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
