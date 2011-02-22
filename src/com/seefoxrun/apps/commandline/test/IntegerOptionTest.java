package com.seefoxrun.apps.commandline.test;

import static org.junit.Assert.*;

import org.junit.* ;

import com.seefoxrun.apps.commandline.parser.Parser;
import com.seefoxrun.apps.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.apps.commandline.parser.exceptions.OptionDefinitionException;
import com.seefoxrun.apps.commandline.parser.options.CheckedOption;
import com.seefoxrun.apps.commandline.parser.options.IntegerOption;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Tests IntegerOption class for correct handling of integer
 * command-line arguments.  Min and max values are tested
 * as well.
 * 
 * @author Brian Fox
 */
public class IntegerOptionTest {

	
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
        print(String.format("Test Class: IntegerOptionTest%n"));        
        print(String.format("-----------------------------%n"));        
    }
	

	// pretty print
	@AfterClass
    public static void post() {
        print(String.format("%n%n%n"));                
    }

	
	// template test method: we expect an exception here
	private void goodTest(
			String title, 
			String argument
	) throws CommandLineException {

		goodTest(title, argument, null, null);
	}

	
	// template test method: we expect an exception here
	// supports min and max
	private void goodTest(
			String title, 
			String argument, 
			Integer min, 
			Integer max
	) throws CommandLineException {

		ArrayList<CheckedOption> options;
		IntegerOption io;

		print(title + " Argument: " + argument, 60);
		io = new IntegerOption('o', "option", "My option", MANDATORY);
		options = new ArrayList<CheckedOption>();
		options.add(io);
		new Parser(options, new String[]{"-o" + new Integer(argument).toString()});
		print(String.format("Result: %s%n", io.getValue()));
		Integer i = io.getValue();
		assertNotNull(i);
		assertEquals((int)i,Integer.parseInt(argument));
	}
	

	// template test method: we expect an exception here
	private void badTest(
			String title, 
			String argument
	) throws OptionDefinitionException {

		badTest(title, argument, null, null);
	}


	// template test method: we expect this to work
	public void badTest(
			String title, 
			String argument,
			Integer min, 
			Integer max
	) throws OptionDefinitionException {

		ArrayList<CheckedOption> options;
		IntegerOption io;

		print(title + " Argument: " + argument, 60);
		io = new IntegerOption('o', "option", "My option", MANDATORY);
		options = new ArrayList<CheckedOption>();
		options.add(io);
		try {
			new Parser(options, new String[]{"-o" + argument});
		}
		catch (Exception e){
			print(String.format("Result: Error: %s%n", e.getMessage()));
		}
	}

	
	@Test
	public void test_basic() throws IOException, CommandLineException {
		print(String.format("TESTING BASIC FUNCTIONALITY:%n"));
		goodTest("Test 1A: ", "0");
		goodTest("Test 1B: ", "1");
		goodTest("Test 1C: ", "-1");
		goodTest("Test 1D: ", "" + Integer.MAX_VALUE);
		goodTest("Test 1E: ", "" + Integer.MIN_VALUE);
		print(String.format("%n%n"));

		print(String.format("TESTING BAD VALUES:%n"));
		badTest("Test 2A:", "2.1");
		badTest("Test 2B:", "BAD");
		badTest("Test 2B:", "-");
		print(String.format("%n%n"));
	}

	
	/**
	 * Yea Ole' Test.
	 * 
	 * @throws OptionDefinitionException
	 */
	@Test
	public void test_range() throws CommandLineException {
		print(String.format("TESTING RANGE:%n"));
		goodTest("Test 1A: ", "0", -1, 1);
		badTest("Test 1A: ", "2", -1, 1);
		print(String.format("%n%n"));
	}

}