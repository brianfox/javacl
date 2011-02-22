package com.seefoxrun.apps.commandline.parser.options;

import com.seefoxrun.apps.commandline.parser.exceptions.ArgumentException;
import com.seefoxrun.apps.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.apps.commandline.parser.exceptions.OptionDefinitionException;

public class FloatOption extends CheckedOption {

	float val;
	
	private float min = -Float.MAX_VALUE;
	private float max = Float.MAX_VALUE;
	
	
	public FloatOption(Character shortOpt, String longOpt, String description, boolean mandatory) throws OptionDefinitionException {
		super(shortOpt, longOpt, description, mandatory);
	}

	@Override
	public void setValue(String s) throws CommandLineException {
		set = true;
		
		try {
			val = Float.parseFloat(s);
			if (val < min || val > max)
				throw new ArgumentException(String.format("float value out of range: %s", s));
		}
		catch (NumberFormatException e) {
			throw new ArgumentException(String.format("could not parse float: %s", s));
		}		
	}

	public void setMin(float min) {
		this.min = min;
	}

	public void setMax(float max) {
		this.max = max;
	}

	@Override
	public Float getValue() {
		return val;		
	}

}
