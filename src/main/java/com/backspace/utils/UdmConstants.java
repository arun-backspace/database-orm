package com.backspace.utils;

public enum UdmConstants {

	MONGO("mongo"),
	MARIADB("mariadb"),
	POSTGRESQL("postgresql"),
	BACKSPACE("backspace"),
	TENANT("tenant");

	String message;

	UdmConstants(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
