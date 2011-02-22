	package com.seefoxrun.app.commandline;

import java.util.ArrayList;

import com.seefoxrun.app.commandline.parser.Parser;
import com.seefoxrun.app.commandline.parser.options.CheckedOption;


public class CommandLineApp {
	
	Parser parser;
	
	/**
	 * Squashed default constructor.  
	 */
	@SuppressWarnings("unused")
	private CommandLineApp() {
	}
	
	
	public CommandLineApp(ArrayList<CheckedOption> options, String[] args) {
		try {
			parser = new Parser(options, args);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}

	
	/**
	 * Returns a nice usage string suitable for framing.
	 * @todo document this
	 */
	public String usage() {
		return parser.usage();
	}
}
