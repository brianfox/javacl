package com.seefoxrun.app.commandline.parser.options;

import com.seefoxrun.app.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.app.commandline.parser.exceptions.OptionDefinitionException;


/**
 * A CheckedOption which supports boolean arguments.  The argument can
 * be true or false, yes or no, or 0 or 1, or some reasonable abbreviation.
 * The argument is case-insensitive.  Explicitly, all of the following
 * values are accepted:<br>  
 * Positive: "yes", "y", "true", "t", "1" <br>
 * Negative: "no", "n", "false", "f", "0" <br>
 * <br><br>
 * Example command-line use:<br>
 * myapp --xflag=yes -y=n --zflag=true
 * 
 * @author Brian Fox
 */
public class BooleanOption extends CheckedOption {

	boolean value;
	
	
	public BooleanOption(
			Character shortOpt, 
			String longOpt, 
			String description, 
			boolean mandatory
	) 
	throws OptionDefinitionException {
		super(shortOpt, longOpt, description, mandatory);
	}

	
	@Override
	public void setValue(String s) throws CommandLineException {
		super.setValue(s);
		s = s.toLowerCase();
		if (s.compareTo("false") == 0
			|| s.compareTo("f") == 0
			|| s.compareTo("no") == 0
			|| s.compareTo("n") == 0
			|| s.compareTo("0") == 0)
			this.value = false;
		else if (s.compareTo("true") == 0
				|| s.compareTo("t") == 0
				|| s.compareTo("yes") == 0
				|| s.compareTo("y") == 0
				|| s.compareTo("1") == 0)
				this.value = true;
		else
			throw new CommandLineException(
					"Invalid boolean option specified."
			);
	}

	
	@Override
	public Boolean getValue() {
		return value;
	}

}
