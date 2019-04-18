package com.guillaumevdn.customcommands.commands;

public class InvalidPatternError extends Error {

	private static final long serialVersionUID = -8712037548816835554L;

	public InvalidPatternError(String message) {
		super(message);
	}

	public InvalidPatternError(String message, Throwable cause) {
		super(message + (cause.getMessage() == null ? "" : " : " + cause.getMessage()), cause);
	}

}
