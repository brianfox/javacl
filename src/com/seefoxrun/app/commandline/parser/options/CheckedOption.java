package com.seefoxrun.app.commandline.parser.options;

import com.seefoxrun.app.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.app.commandline.parser.exceptions.DuplicateOptionException;
import com.seefoxrun.app.commandline.parser.exceptions.OptionDefinitionException;


/**
 * The base class representing a command-line option.  A CheckedOption accepts both 
 * a hypen + single character (POSIX) and a double hypen + string (GNU) option name,
 * for example, -h and --help.  
 * <br><br>
 * shortOpt must be null or an alphanumeric.  longOpt must be null or an alphanumeric 
 * followed by one or more alphanumeric, hyphens, and underscore characters.  Either 
 * shortOpt or longOpt can be null, but not both.  
 * <br><br>
 * @param shortOpt a Character depicting the short option name
 * @param longOpt a String depicting the long option name
 * @param description a String displayed in the help output
 * @param mandatory a boolean indicating whether the option must be provided
 * @throws OptionDefinitionException occurs if shortOpt or longOpt are invalid or 
 * 	       if both are missing
 * 
 * @author Brian Fox
 */
abstract public class CheckedOption {
	
	Character shortOpt;
	String longOpt;
	String description;
	boolean mandatory;
	boolean set = false;
	
	public CheckedOption(
			Character shortOpt, 
			String longOpt, 
			String description, 
			boolean mandatory
		) throws OptionDefinitionException {
		
		this.shortOpt = shortOpt;
		this.longOpt = longOpt;
		this.description = description;
		this.mandatory = mandatory;
		
		if (longOpt != null && longOpt != null && longOpt.length() < 2)
			throw new OptionDefinitionException(
					"Long option flags must be multiple characters."
					);
	}


	
	
	/**
	 * Sets this option's value.  The String parameter comes straight off 
	 * the command line.  It is expected to be parsed for specialty 
	 * subclasses.
	 * 
	 * @param val
	 * @throws CommandLineException 
	 */
	public void setValue(String val) throws CommandLineException {
		if (set)
			throw new DuplicateOptionException();
		set = true;
	}
	
	
	
	
	/**
	 * Gets this option's value.  This is an Object in the most generic form, 
	 * but subclasses are free to (and should) narrow this down considerably.
	 * 
	 * @return Object this CheckedOption's value
	 */
	abstract public Object getValue();

	
	
	
	public boolean hasArgument() {
		return true;
	}

	
	
	/**
	 * Returns this CheckedOption's short option name.
	 * 
	 * @return String 
	 */
	public Character getShortOpt() {
		return shortOpt;
	}

	
	
	/**
	 * Return this CheckedOption's long option name.
	 * 
	 * @return String
	 */
	public String getLongOpt() {
		return longOpt;
	}

	
	
	/**
	 * Return this CheckedOption's description.
	 * 
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	
	
	
	/**
	 * Return this CheckedOption's short and long option names
	 * combined into a single String similar to "-h --help". 
	 * Omission of a short or long option name will omit
	 * their presence in the return string. 
	 * 
	 * @return String combined short and long option names
	 */
	public String getOptionDescription() {
		return 
			((shortOpt == null) ? "" : "-" + shortOpt) +
			((shortOpt != null && longOpt != null) ? " " : "") +
			((longOpt == null) ? "" : "--" + longOpt);
	}

	
	
	
	public boolean isMandatory() {
		return mandatory;
	}

	@Override
	public String toString() {
		return String.format("-%s  --%s  Desc: %s  Mandatory: %s  Value: %s", shortOpt, longOpt, description, mandatory, getValue());
	}
}
