package com.seefoxrun.app.commandline.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.* ;

import com.seefoxrun.app.commandline.parser.Parser;
import com.seefoxrun.app.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.app.commandline.parser.options.CheckedOption;
import com.seefoxrun.app.commandline.parser.options.FlagOption;
import com.seefoxrun.app.commandline.parser.options.StringOption;

import java.util.ArrayList;



/**
 * Tests base class CheckedOption for proper handling of optional
 * options, optional arguments, flags, and stacked flags.
 * 
 * @author Brian Fox
 */
public class CheckedOptionTest {

	// moniker for indicating a mandatory field
	private static final boolean MANDATORY = true;
	
	
	// pretty print
	private String pad(String s, int len) {
		while (s.length() < len)
			s += "          ";
		return s.substring(0, len-1);
	}
	

	// pretty print
	private void print(String s, int len) {
		System.out.print(pad(s,len));
	}

	
	// pretty print
	private static void print(String s) {
		System.out.print(s);
	}

	
	// pretty print
	@BeforeClass
    public static void pre() {
        print(String.format("Test Class: CheckedOptionTest%n"));        
        print(String.format("-----------------------------%n"));        
    }

	
	// pretty print
	@AfterClass
    public static void post() {
        print(String.format("%n%n%n"));                
    }
	
	
	/**
	 * Yea Ole' Test.
	 * 
	 * @throws CommandLineException
	 */
	@Test
	public void test_mandatory() throws CommandLineException {
		ArrayList<CheckedOption> options;
		
		print(String.format("TESTING OPTION OMISSION:%n"));


		print("Test 1A: Optional, omitted.",60);
		options = new ArrayList<CheckedOption>();
		options.add(new StringOption('o', "option", "My option", !MANDATORY));
		new Parser(options, new String[]{});
		print(String.format("Result: %s%n",options.get(0).getValue()));
		assertNull(options.get(0).getValue());

		print("Test 1B: Optional, provided.",60);
		options = new ArrayList<CheckedOption>();
		options.add(new StringOption('o', "option", "My option", !MANDATORY));
		new Parser(options, new String[]{"-otest"});
		print(String.format("Result: %s%n",options.get(0).getValue()));
		assertNotNull(options.get(0).getValue());

		try {
			print("Test 1C: Mandatory, omitted.",60);
			options = new ArrayList<CheckedOption>();
			options.add(new StringOption('o', "option", "My option", MANDATORY));
			// testmode purposefully skips bottom turtle error detection and
			// system exiting.
			new Parser(options, new String[]{});   
			print(String.format("Result: %s%n",options.get(0).getValue()));
			fail("Mandatory option omitted.");
		} 
		catch (Exception e){
			print(String.format("Catch result: %s%n",options.get(0).getValue()));
		}

		try {
			print("Test 1D: Mandatory, provided.",60);
			options = new ArrayList<CheckedOption>();
			options.add(new StringOption('o', "option", "My option", MANDATORY));
			new Parser(options, new String[]{"-otest"});
			print(String.format("Result: %s%n", options.get(0).getValue()));
			assertNotNull(options.get(0).getValue());
		} 
		catch (Exception e){
			print(String.format("Catch result: %s%n", "Found"));
		}
		print(String.format("%n%n")); 
	}
	
	
	@Test 
	public void test_arguments() {
		ArrayList<CheckedOption> options;
		
		try {
			print(String.format("TESTING ARGUMENT OMISSION:%n"));
			print("Test 2A: Argument required, provided, short form.",60);
			options = new ArrayList<CheckedOption>();
			options.add(new StringOption('o', "option", "My option", !MANDATORY));
			new Parser(options, new String[]{"-o somestring"});
			print(String.format("Result: %s%n", options.get(0).getValue()));
		} 
		catch (Exception e){
			print(String.format("Catch result: Error.  %s%n", e));
		}

		try {
			print("Test 2B: Argument required, provided, long form.",60);
			options = new ArrayList<CheckedOption>();
			options.add(new StringOption('o', "option", "My option", !MANDATORY));
			new Parser(options, new String[]{"--option=somestring"});
			print(String.format("Result: %s%n", options.get(0).getValue()));
		} 
		catch (Exception e){
			print(String.format("Catch result: Error.  %s%n", e));
		}

		try {
			print("Test 2C: Argument required, omitted, short form.",60);
			options = new ArrayList<CheckedOption>();
			options.add(new StringOption('o', "option", "My option", MANDATORY));
			new Parser(options, new String[]{"-o"});
			print(String.format("Result: %s%n", options.get(0).getValue()));
			fail("Mandatory option omitted.");
		} 
		catch (Exception e){
			print(String.format("Catch result: Error.  %s%n", e.getMessage()));
		}


		try {
			print("Test 2D: Argument required, omitted, long form.",60);
			options = new ArrayList<CheckedOption>();
			options.add(new StringOption('o', "option", "My option", MANDATORY));
			new Parser(options, new String[]{"--option"});
			print(String.format("Result: %s%n", options.get(0).getValue()));
			fail("Mandatory option omitted.");
		} 
		catch (Exception e){
			print(String.format("Catch result: Error.  %s%n", e.getMessage()));
		}

		print(String.format("%n%n"));
	}


	/**
	 * Yea Ole' Test.
	 * 
	 * @throws CommandLineException
	 */
	@Test 
	public void test_flags() throws CommandLineException {
		ArrayList<CheckedOption> options;

		print(String.format("TESTING FLAGS:%n"));
		
		print("Test 3A. Flag omitted, short form.",60);
		options = new ArrayList<CheckedOption>();
		options.add(new FlagOption('o', "option", "My option"));
		new Parser(options, new String[]{});
		assertEquals((Boolean)options.get(0).getValue(), false);
		print(String.format("Result: %s%n",options.get(0).getValue()));

		print("Test 3B. Flag omitted, long form.",60);
		options = new ArrayList<CheckedOption>();
		options.add(new FlagOption('o', "option", "My option"));
		new Parser(options, new String[]{});
		assertEquals((Boolean)options.get(0).getValue(), false);
		print(String.format("Result: %s%n",options.get(0).getValue()));


		print("Test 3C. Flag provided, short form.",60);
		options = new ArrayList<CheckedOption>();
		options.add(new FlagOption('o', "option", "My option"));
		new Parser(options, new String[]{"-o"});
		assertEquals((Boolean)options.get(0).getValue(), true);
		print(String.format("Result: %s%n",options.get(0).getValue()));

		print("Test 3D. Flag provided, long form.",60);
		options = new ArrayList<CheckedOption>();
		options.add(new FlagOption('o', "option", "My option"));
		new Parser(options, new String[]{"--option"});
		assertEquals((Boolean)options.get(0).getValue(), true);
		print(String.format("Result: %s%n",options.get(0).getValue()));

		print("Test 3E. Flag provided, argument provided, short form.",60);
		options = new ArrayList<CheckedOption>();
		options.add(new FlagOption('o', "option", "My option"));
		try {
			new Parser(options, new String[]{"-o1"});
			print(String.format("Result: %s%n", options.get(0).getValue()));
			fail("A flag should not allow an argument to be provided.");
		} 
		catch (Exception e){
			print(String.format("Catch result: %s%n",options.get(0).getValue()));
			assertNotNull(options.get(0).getValue());
		}

		print("Test 3F. Flag provided, argument provided, long form.",60);
		options = new ArrayList<CheckedOption>();
		options.add(new FlagOption('o', "option", "My option"));
		try {
			new Parser(options, new String[]{"-o1"});
			print(String.format("Result: %s%n", options.get(0).getValue()));
			fail("A flag should not allow an argument to be provided.");
		} 
		catch (Exception e){
			print(String.format("Catch result: %s%n",options.get(0).getValue()));
			assertNotNull(options.get(0).getValue());
		}
		print(String.format("%n%n"));
	}

	
	/**
	 * Yea Ole' Test.
	 * 
	 * @throws CommandLineException
	 */
	@Test 
	public void test_stacked_flags() throws CommandLineException {
		ArrayList<CheckedOption> options;

		print(String.format("TESTING STACKED FLAGS:%n"));
		
		
		print("Test 4A. Stacked abc flags, no argument",60);
		options = new ArrayList<CheckedOption>();
		options.add(new FlagOption('a', "aoption", "My a option"));
		options.add(new FlagOption('b', "aoption", "My b option"));
		options.add(new FlagOption('c', "aoption", "My c option"));
		new Parser(options, new String[]{"-abc"});
		assertEquals((Boolean)options.get(0).getValue(), true);
		assertEquals((Boolean)options.get(1).getValue(), true);
		assertEquals((Boolean)options.get(2).getValue(), true);
		print(String.format("Result: %s%n",options.get(0).getValue()));

		
		print("Test 4B. Stacked abc flags, with argument",60);
		options = new ArrayList<CheckedOption>();
		options.add(new FlagOption('a', "aoption", "My a option"));
		options.add(new FlagOption('b', "aoption", "My b option"));
		options.add(new FlagOption('c', "aoption", "My c option"));
		try {
			new Parser(options, new String[]{"-o1"});
			print(String.format("Result: %s%n", options.get(0).getValue()));
			fail("A flag should not allow an argument to be provided.");
		} 
		catch (Exception e){
			print(String.format("Catch result: %s%n",options.get(0).getValue()));
			assertNotNull(options.get(0).getValue());
		}

		
		print("Test 4C. Stacked abc strings, no argument",60);
		options = new ArrayList<CheckedOption>();
		options.add(new StringOption('a', "aoption", "My a option", false));
		options.add(new StringOption('b', "aoption", "My b option", false));
		options.add(new StringOption('c', "aoption", "My c option", false));
		new Parser(options, new String[]{"-abc"});
		print(String.format("Result: %s %s %s%n",
				options.get(0).getValue(),
				options.get(1).getValue(),
				options.get(2).getValue()
				
		));

		print("Test 4D. Stacked abc strings, argument",60);
		options = new ArrayList<CheckedOption>();
		options.add(new StringOption('a', "aoption", "My a option", false));
		options.add(new StringOption('b', "aoption", "My b option", false));
		options.add(new StringOption('c', "aoption", "My c option", false));
		new Parser(options, new String[]{"-abcargument"});
		print(String.format("Result: %s %s %s%n",
				options.get(0).getValue(),
				options.get(1).getValue(),
				options.get(2).getValue()
				
		));



	}


}