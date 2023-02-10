package com.example.integrate.spring.react.exception;

public class ProductApiException extends RuntimeException {

	private static final long serialVersionUID = 119874212393098L;

	public ProductApiException(String msg) {
		super(msg);
	}

}
