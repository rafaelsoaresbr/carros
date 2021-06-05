package com.example.carros.api.exception;

import java.io.Serializable;

public class ExceptionError implements Serializable {

	private static final long serialVersionUID = 1L;
	private String error;

	ExceptionError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}
}
