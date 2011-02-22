package com.seefoxrun.apps.commandline.parser.exceptions;


@SuppressWarnings("serial")
public class DuplicateOptionException extends CommandLineException {

	public DuplicateOptionException() {
		super();
	}

	public DuplicateOptionException(String s) {
		super(s);
	}

}
