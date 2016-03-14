package me.kotisis.stocks.model;

public class ValidationException extends Exception {

	private static final long serialVersionUID = -528215006597899565L;

	public ValidationException(String message) {
		super("Invalid value for " + message);
	}

}
