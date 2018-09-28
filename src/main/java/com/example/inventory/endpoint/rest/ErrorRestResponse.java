package com.example.inventory.endpoint.rest;

public class ErrorRestResponse {
	int responseCode;
	String errorMsg;
	
	public ErrorRestResponse(int responseCode, String errorMsg) {
		this.responseCode = responseCode;
		this.errorMsg = errorMsg;
	}
}
