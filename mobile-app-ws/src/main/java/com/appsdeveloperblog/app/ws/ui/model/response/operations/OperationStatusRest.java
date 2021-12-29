package com.appsdeveloperblog.app.ws.ui.model.response.operations;

public class OperationStatusRest {

    private String operationName;
    private String operationResult;

    public OperationStatusRest() {
    }

    public OperationStatusRest(String operationName, String operationResult) {
	this.operationName = operationName;
	this.operationResult = operationResult;
    }

    public String getOperationResult() {
	return operationResult;
    }

    public void setOperationResult(String operationResult) {
	this.operationResult = operationResult;
    }

    public String getOperationName() {
	return operationName;
    }

    public void setOperationName(String operationName) {
	this.operationName = operationName;
    }
}
