package com.seefoxrun.apps.commandline.parser.exceptions;

@SuppressWarnings("serial")
public class CommandLineException extends Exception {

	public CommandLineException(String desc) {
		super(desc);
	}

	public CommandLineException() {
		super();
	}

}
