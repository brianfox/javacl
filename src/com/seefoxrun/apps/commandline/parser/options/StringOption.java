package com.seefoxrun.apps.commandline.parser.options;

import com.seefoxrun.apps.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.apps.commandline.parser.exceptions.OptionDefinitionException;

public class StringOption extends CheckedOption {
	
	String val;
	
	public StringOption(
			Character shortOpt, 
			String longOpt, 
			String description, 
			boolean mandatory
			) throws OptionDefinitionException {

		super(shortOpt, longOpt, description, mandatory);
	}

	public StringOption(
			Character shortOpt, 
			String longOpt, 
			String description
			) throws OptionDefinitionException {
		
		super(shortOpt, longOpt, description, true);
	}

	@Override
	public void setValue(String s) throws CommandLineException {
		set = true;
		val = s;
	}
	
	@Override
	public String getValue() {
		return val;
	}
}
