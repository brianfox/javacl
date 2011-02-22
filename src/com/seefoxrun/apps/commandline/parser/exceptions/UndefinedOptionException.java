package com.seefoxrun.apps.commandline.parser.exceptions;


@SuppressWarnings("serial")
public class UndefinedOptionException extends CommandLineException {

	public UndefinedOptionException() {
		super();
	}

	public UndefinedOptionException(String s) {
		super(s);
	}

}
