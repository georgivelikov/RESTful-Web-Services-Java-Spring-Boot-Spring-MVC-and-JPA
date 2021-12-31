package com.appsdeveloperblog.app.ws.exception;

import java.util.Date;

public class ExceptionMessageRest {

    private Date timestamp;
    private String message;
    private String sourceExceptionMessage;

    public ExceptionMessageRest() {
    }

    public ExceptionMessageRest(Date timestamp, String message) {
	this.timestamp = timestamp;
	this.message = message;

    }

    public ExceptionMessageRest(Date timestamp, String message, String sourceExceptionMessage) {
	this.timestamp = timestamp;
	this.message = message;
	this.sourceExceptionMessage = sourceExceptionMessage;
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

    public String getSourceExceptionMessage() {
	return sourceExceptionMessage;
    }

    public void setSourceExceptionMessage(String sourceExceptionMessage) {
	this.sourceExceptionMessage = sourceExceptionMessage;
    }

}
