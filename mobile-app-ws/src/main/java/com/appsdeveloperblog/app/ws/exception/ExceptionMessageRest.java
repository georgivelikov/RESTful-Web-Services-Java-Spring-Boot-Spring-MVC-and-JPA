package com.appsdeveloperblog.app.ws.exception;

import java.util.Date;

public class ExceptionMessageRest {

    private Date timestamp;
    private String message;
    private String additionalInformation;

    public ExceptionMessageRest() {
    }

    public ExceptionMessageRest(Date timestamp, String message) {
	this.timestamp = timestamp;
	this.message = message;

    }

    public ExceptionMessageRest(Date timestamp, String message, String additionalInformation) {
	this.timestamp = timestamp;
	this.message = message;
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

}
