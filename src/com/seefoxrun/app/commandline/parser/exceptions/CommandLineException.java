package com.seefoxrun.app.commandline.parser.exceptions;

@SuppressWarnings("serial")
public class CommandLineException extends Exception {

	public CommandLineException(String desc) {
		super(desc);
	}

	public CommandLineException() {
		super();
	}

}
