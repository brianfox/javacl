package com.seefoxrun.apps.commandline.test;

import static org.junit.Assert.*;

import org.junit.* ;

import com.seefoxrun.apps.commandline.parser.Parser;
import com.seefoxrun.apps.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.apps.commandline.parser.exceptions.OptionDefinitionException;
import com.seefoxrun.apps.commandline.parser.options.CheckedOption;
import com.seefoxrun.apps.commandline.parser.options.FloatOption;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FloatOptionTest {

	private static final boolean MANDATORY = true;
	
	
	public String pad(String s, int len) {
		while (s.length() < len)
			s += "          ";
		return s.substring(0, len-1);
	}
	
	public void print(String s, int len) {
		System.out.print(pad(s,len));
	}

	public static void print(String s) {
		System.out.print(s);
	}

	
	public String getTempFilePath() throws IOException {
		File f = File.createTempFile("unit_test", "");
		f.delete();
		return f.getPath() + ".file";
	}
	
	
	@BeforeClass
    public static void pre() {
        print(String.format("Test Class: IntegerOptionTest%n"));        
        print(String.format("-----------------------------%n"));        
    }
	

	@AfterClass
    public static void post() {
        print(String.format("%n%n%n"));                
    }

	
	public void goodTest(String title, String argument) throws CommandLineException {
		simpleTest(title, argument, null, null);
	}
	
	public void simpleTest(String title, String argument, Float min, Float max) throws CommandLineException {
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
	
	

	public void badTest(String title, String argument) throws OptionDefinitionException {
		badTest(title, argument, null, null);
	}

	public void badTest(String title, String argument, Float min, Float max) throws OptionDefinitionException {
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

	
	//@Test
	public void test_basic() throws IOException, CommandLineException {
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

	@Test
	public void test_range() throws IOException, CommandLineException {
		print(String.format("TESTING RANGE:%n"));
		simpleTest("Test 1A: ", "0", -1F, 1F);
		badTest("Test 1A: ", "2", -1F, 1F);
		print(String.format("%n%n"));
	}


}