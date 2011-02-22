package com.seefoxrun.apps.commandline.test;

import static org.junit.Assert.*;

import org.junit.* ;

import com.seefoxrun.apps.commandline.parser.Parser;
import com.seefoxrun.apps.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.apps.commandline.parser.exceptions.OptionDefinitionException;
import com.seefoxrun.apps.commandline.parser.options.CheckedOption;
import com.seefoxrun.apps.commandline.parser.options.IntegerOption;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class IntegerOptionTest {

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

	
	public void simpleTest(String title, int argument) throws CommandLineException {
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
		assertEquals((int)i,argument);
	}
	
	
	public void badTest(String title, String argument) throws OptionDefinitionException {
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
		simpleTest("Test 1A: ", 0);
		simpleTest("Test 1B: ", 1);
		simpleTest("Test 1C: ", -1);
		simpleTest("Test 1D: ", Integer.MAX_VALUE);
		simpleTest("Test 1E: ", Integer.MIN_VALUE);
		print(String.format("%n%n"));

		print(String.format("TESTING BAD VALUES:%n"));
		badTest("Test 2A:", "2.1");
		badTest("Test 2B:", "BAD");
		badTest("Test 2B:", "-");
		print(String.format("%n%n"));
	}


}