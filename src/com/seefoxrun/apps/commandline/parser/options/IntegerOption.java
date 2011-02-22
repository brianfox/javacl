package com.seefoxrun.apps.commandline.parser.options;

import com.seefoxrun.apps.commandline.parser.exceptions.ArgumentException;
import com.seefoxrun.apps.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.apps.commandline.parser.exceptions.OptionDefinitionException;


public class IntegerOption extends CheckedOption {

	int val;
	int min = Integer.MIN_VALUE;
	int max = Integer.MAX_VALUE;
	
	public IntegerOption(Character shortOpt, String longOpt, String description, boolean mandatory) throws OptionDefinitionException {
		super(shortOpt, longOpt, description, mandatory);
	}

	@Override
	public void setValue(String s) throws CommandLineException {
		set = true;
		try {
			val = Integer.parseInt(s);
		}
		catch (NumberFormatException e) {
			throw new ArgumentException(String.format("Could not parse integer: %s", s));
		}	
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public Integer getValue() {
		return val;		
	}
}
