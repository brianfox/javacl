package com.seefoxrun.apps.commandline.parser.exceptions;


@SuppressWarnings("serial")
public class RequiredOptionException extends CommandLineException {

	public RequiredOptionException() {
		super();
	}

	public RequiredOptionException(String s) {
		super(s);
	}

}
