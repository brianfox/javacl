package com.seefoxrun.apps.commandline.parser.options;

import com.seefoxrun.apps.commandline.parser.exceptions.ArgumentException;
import com.seefoxrun.apps.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.apps.commandline.parser.exceptions.OptionDefinitionException;

public class FlagOption extends CheckedOption {

	public FlagOption(Character shortOpt, String longOpt, String description) throws OptionDefinitionException {
		super(shortOpt, longOpt, description, false);
	}


	@Override
	public void setValue(String s) throws CommandLineException {
		if (s != null)
			throw new ArgumentException("does not accept argument: " + s);
		set = true;
	}

	@Override
	public Boolean getValue() {
		return set;
	}

	@Override
	public boolean hasArgument() {
		return false;
	}

}
