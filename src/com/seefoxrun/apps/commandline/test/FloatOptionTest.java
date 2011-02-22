package com.seefoxrun.apps.commandline.test;

import static org.junit.Assert.*;

import org.junit.* ;

import com.seefoxrun.apps.commandline.parser.Parser;
import com.seefoxrun.apps.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.apps.commandline.parser.exceptions.OptionDefinitionException;
import com.seefoxrun.apps.commandline.parser.options.CheckedOption;
import com.seefoxrun.apps.commandline.parser.options.FloatOption;

import java.util.ArrayList;


/**
 * Tests FloatOption class for correct handling of float
 * command-line arguments.  Min and max values are tested
 * as well.
 * 
 * @author Brian Fox
 */
public class FloatOptionTest {

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

	
	// template test method: we expect the parser to work
	private void goodTest(String title, String argument) throws CommandLineException {
		simpleTest(title, argument, null, null);
	}
	
	
	// template test method: we expect the parser to work
	// supports min and max
	private void simpleTest(String title, String argument, Float min, Float max) throws CommandLineException {
		ArrayList<CheckedOption> options;
		FloatOption opt;

		print(title + " Argument: " + argument, 60);
		opt = new FloatOption('o', "option", "My option", MANDATORY);
		if (min != null)
			opt.setMin(min);
		if (max != null)
			opt.setMax(max);
		options = new ArrayList<CheckedOption>();
		options.add(opt);
		new Parser(options, new String[]{"-o" + argument});
		print(String.format("Result: %s%n", opt.getValue()));
		Float i = opt.getValue();
		assertNotNull(i);
		try {
			float val = Float.parseFloat(argument);
			assertTrue(i.floatValue() == val);
		} 
		catch(NumberFormatException e) {
			fail("Test failed.");
		}
	}
	
	

	// template test method: we expect an exception here
	private void badTest(
			String title, 
			String argument
	) throws OptionDefinitionException {

		badTest(title, argument, null, null);
	}

	
	// template test method: we expect an exception here
	// supports min and max
	private void badTest(
			String title, 
			String argument, 
			Float min, 
			Float max
	) throws OptionDefinitionException {

		ArrayList<CheckedOption> options;
		FloatOption opt;

		print(title + " Argument: " + argument, 60);
		opt = new FloatOption('o', "option", "My option", MANDATORY);
		if (min != null)
			opt.setMin(min);
		if (max != null)
			opt.setMax(max);
		
		options = new ArrayList<CheckedOption>();
		options.add(opt);
		try {
			new Parser(options, new String[]{"-o" + argument});
			fail("Expected an error to be thrown");
		}
		catch (Exception e){
			print(String.format("Result: Error: %s%n", e.getMessage()));
		}
	}

	
	/**
	 * Yea Ole' Test.
	 * 
	 * @throws OptionDefinitionException
	 * @throws IOException
	 */
	@Test
	public void test_basic() throws CommandLineException {
		print(String.format("TESTING BASIC FUNCTIONALITY:%n"));
		goodTest("Test 1A: ", "0");
		goodTest("Test 1B: ", "1.0");
		goodTest("Test 1C: ", "-1.0");
		goodTest("Test 1D: ", new Float(Float.MAX_VALUE).toString());
		goodTest("Test 1E: ", new Float(Float.MIN_VALUE).toString());
		print(String.format("%n%n"));

		print(String.format("TESTING BAD VALUES:%n"));
		badTest("Test 2A:", "BAD");
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
		simpleTest("Test 1A: ", "0", -1F, 1F);
		badTest("Test 1A: ", "2", -1F, 1F);
		print(String.format("%n%n"));
	}


}