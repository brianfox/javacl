package com.seefoxrun.apps.commandline.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.seefoxrun.apps.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.apps.commandline.parser.exceptions.RequiredOptionException;
import com.seefoxrun.apps.commandline.parser.exceptions.UndefinedOptionException;
import com.seefoxrun.apps.commandline.parser.options.CheckedOption;


public class Parser {
	
	// listedOptions is provided as a constructor argument.  
	// mappedOptions is a lookup table of the same listedOptions.
	
	ArrayList<CheckedOption> listedOptions;
	HashMap<String,CheckedOption> mappedOptions;  
	String commandLine;
	boolean testMode = false;
	
	/**
	 * Squashed default constructor.  
	 */
	@SuppressWarnings("unused")
	private Parser() {
	}
	
	
	public Parser(ArrayList<CheckedOption> options, String[] args) throws CommandLineException {
		listedOptions = options;
		commandLine = cat(args);
		mappedOptions = mapOptions(listedOptions);
		parseCommandLine();
	}

	
	/**
	 * Returns a nice usage string suitable for framing.
	 * @todo document this
	 */
	public String usage() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Usage: " + "program " + " [OPTIONS]... FILES%n"));
		sb.append(String.format("Some description%n"));
		sb.append(String.format("%n"));
		sb.append(String.format("%s%n", optUsage()));
		return sb.toString();
	}

	
	/**
	 * Parses the command line arguments in accordance to POSIX and
	 * GNU guidelines.
	 */
	public void parseCommandLine() throws CommandLineException {
		try {
			String[] rough = commandLine.split("^--|\\s+--");
			// rough = { 
			//            long1 [shortgroup1], 
			//            long2 [shortgroup2], 
			//            ... 
			//            longN [shortgroupN] 
			//        }
			
			for (String s : rough) {
				String[] details = s.split("^-|\\s+-");
				// details = { 
				//              long1 | stackedshort0,  
				//              stackedshort1,
				//              stackedshort2,
				//              ...
				//              stackedshortN
				//            }
				
				int start = 0;
				if (details[0].trim().length() != 0) { 
					start++;
					// We found a long format option
					SimpleOpt o = parseLongOption(details[0]);
					setArgument(o.name, o.arg);
				}
				
				for (int i=start; i < details.length; i++) {
					// details[i] = {
					//					flag1,
					//					flag2,
					//					...
					//					flagN[arg]
					//               }
					ArrayList<SimpleOpt> stack = breakStack(details[i]);
					if (stack == null || stack.size() == 0) 
						;  // loose argument  @todo
					else 
						for (SimpleOpt o : stack) 
							setArgument(o.name, o.arg);
				}
			}
			
			for (CheckedOption o : listedOptions) {
				if (o.isMandatory() && o.getValue() == null) {
					throw new RequiredOptionException(
							"missing required option: " 
							+ o.getOptionDescription()
							);
				}
			}
		} 
		catch (UndefinedOptionException e) {
			throw new CommandLineException("unknown option: " + e.getMessage());
		}
	}
	
	
	/**
	 * Breaks up "stacked" command-line options.  A stacked option is defined as a 
	 * single dash followed by a group of single character options and finally a
	 * argument.
	 * 
	 * EXAMPLE:  
	 * ls -alt
	 * ls -a -l -t
	 * 
	 * @param s a String suspected to be a stacked option
	 * @return an ArrayList of SimpleOpt listing all options.  The last may possibly
	 * have an argument.
	 * @throws UndefinedOptionException 
	 * 
	 */
	private ArrayList<SimpleOpt> breakStack(String s) throws UndefinedOptionException {

		ArrayList<SimpleOpt> list = new ArrayList<SimpleOpt>();
		ArrayList<Character> found = new ArrayList<Character>();
		
		String arg = null;
		
		for (int j=0; j < s.length(); j++) {
			String optname = s.charAt(j) + "";
			CheckedOption o = findOption(optname);
			if (found.contains(s.charAt(j)) || o == null) {
				arg = s.substring(j);
				break;
			}
			list.add(new SimpleOpt(optname, null));
			found.add(s.charAt(j));
		}
		if (list.size() == 0 && arg != null)
			throw new UndefinedOptionException("undefined option: " + s);
		if (arg != null)
			list.get(list.size()-1).arg = arg;
		return list;
	}
	

	/**
	 * Assigns a value to a CheckedOption.
	 * 
	 * @param name the name of the checkedOption.  This can be a short 
	 * option single-character String or a long option String.
	 * @param arg the argument value.
	 * @throws CommandLineException if the CheckedOption is not defined.
	 */
	private void setArgument(String name, String arg) throws CommandLineException {
		CheckedOption o = mappedOptions.get(name);
		if (o == null) 
			throw new UndefinedOptionException("--" + name);
		o.setValue(arg);
	}


	/**
	 * Maps each CheckedOption to both it's single-character short option
	 * name and to it's multi-character long option name.
	 * 
	 * @param options the CheckedOptions arranged in an ArrayList. 
	 * @return the CheckedOptions arranged in a HashMap.
	 */
	private HashMap<String,CheckedOption> mapOptions(ArrayList<CheckedOption> options) {
		HashMap<String,CheckedOption> ret = new HashMap<String,CheckedOption>();
		for (CheckedOption o : options) {
			if (o.getLongOpt() != null)
				ret.put(o.getLongOpt(), o);
			if (o.getShortOpt() != null)
				ret.put(o.getShortOpt().toString(), o);
		}
		return ret;
	}
	
	
	/**
	 * Concatenates an array of Strings with a single space between each String.
	 * 
	 * @param strings Strings to concatenate
	 * @return concatenated String
	 */
	private String cat(String[] strings) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			sb.append(s + " ");
		}
		return sb.toString().trim();
	}
	
	
	/**
	 * Parses the simple a GNU long option in the format --NAME=ARG
	 *  
	 * @param s String to parse
	 * @return SimpleOpt representing the options name and argument 
	 */
	private SimpleOpt parseLongOption(String s) {
		int idx = s.indexOf('=');
		String name = (idx < 0) ? s : s.substring(0, idx);
		String arg = (idx < 0) ? null : s.substring(idx+1);
		SimpleOpt o = new SimpleOpt(name, arg);
		return o;
	}

	
	/**
	 * Finds an option in the ArrayList provided in the constructor.
	 *  
	 * @param s option name
	 * @return a CheckedOption matching the name or null if no match.
	 */
	private CheckedOption findOption(String s) {
		for (CheckedOption c : listedOptions) 
			if (
					c.getShortOpt() != null 
					&& c.getShortOpt().toString().compareTo(s) == 0
				)
				return c;
		return null;
	}
	
	
	private String optUsage() {
		StringBuilder sb = new StringBuilder();
		for (CheckedOption o : listedOptions) {
			Character sh = o.getShortOpt();
			String lo = o.getLongOpt();
			String next = "";
			if (sh == null) 
				next = String.format("--%s", lo);
			else if (lo == null)
				next = String.format("-%s", sh);
			else
				next = String.format("-%s  --%s", sh, lo);
			sb.append(pad(next, 0, 70, true));
			sb.append(pad(o.getDescription(), 7, 70, true));
			sb.append(String.format("%n"));
		}
		return sb.toString();
	}
	
	private String pad(String s, int left, int right, boolean keepCR) {
		StringBuilder sb = new StringBuilder();
		String format = "%" + left + "s" + "  %s%n";
		sb.append(String.format(format, "", s));
		return sb.toString();
	}
	
	/**
	 * Simple struct like class.
	 */
	private class SimpleOpt {
		String name;
		String arg;
		
		SimpleOpt(String name, String arg) {
			this.name = name;
			this.arg = arg;
		}
	}
	
}
