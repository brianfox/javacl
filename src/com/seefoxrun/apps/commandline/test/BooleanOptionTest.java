package com.seefoxrun.apps.commandline.test;

import static org.junit.Assert.*;

import org.junit.* ;

import com.seefoxrun.apps.commandline.CommandLineApp;
import com.seefoxrun.apps.commandline.parser.Parser;
import com.seefoxrun.apps.commandline.parser.exceptions.OptionDefinitionException;
import com.seefoxrun.apps.commandline.parser.options.BooleanOption;
import com.seefoxrun.apps.commandline.parser.options.CheckedOption;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BooleanOptionTest {

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
        print(String.format("Test Class: BooleanOptionTest%n"));        
        print(String.format("-----------------------------%n"));        
    }
	

	@AfterClass
    public static void post() {
        print(String.format("%n%n%n"));                
    }

	
	public void simpleTest(String title, String argument, boolean expected) throws OptionDefinitionException {
		ArrayList<CheckedOption> options;
		BooleanOption bo;
		Boolean b;

		print(title + ", " + argument, 60);
		bo = new BooleanOption('o', "option", "My option", MANDATORY);
		options = new ArrayList<CheckedOption>();
		options.add(bo);
		new CommandLineApp(options, new String[]{"-o" + argument});
		print(String.format("Result: %s%n", bo.getValue()));
		b = bo.getValue();
		assertNotNull(b);
		if (expected == true) 
			assertTrue(b);
		else
			assertFalse(b);
	}
	
	
	public void badTest(String title, String argument, boolean expected) throws OptionDefinitionException {
		ArrayList<CheckedOption> options;
		BooleanOption bo;

		print(title + ", " + argument, 60);
		bo = new BooleanOption('o', "option", "My option", MANDATORY);
		options = new ArrayList<CheckedOption>();
		options.add(bo);
		try {
			new Parser(options, new String[]{"-o" + argument});
			fail("Expected an error to be throw.");
		}
		catch (Exception e) {
			print(String.format("Result: Error thrown: %s%n", e.getMessage()));
		}
	}

	
	@Test
	public void test_basic() throws OptionDefinitionException, IOException {
		print(String.format("TESTING NEGATIVE FUNCTIONALITY:%n"));
		simpleTest("Test 1A: Negative", "0", false);
		simpleTest("Test 1B: Negative", "n", false);
		simpleTest("Test 1C: Negative", "no", false);
		simpleTest("Test 1D: Negative", "f", false);
		simpleTest("Test 1E: Negative", "false", false);
		print(String.format("%n%n"));

		print(String.format("TESTING POSITIVE FUNCTIONALITY:%n"));
		simpleTest("Test 2A: Negative", "1", true);
		simpleTest("Test 2B: Negative", "y", true);
		simpleTest("Test 2C: Negative", "yes", true);
		simpleTest("Test 2D: Negative", "t", true);
		simpleTest("Test 2E: Negative", "true", true);
		print(String.format("%n%n"));

		print(String.format("TESTING CASE SENSITIVITY:%n"));
		simpleTest("Test 3A: Negative", "Y", true);
		simpleTest("Test 3B: Negative", "yeS", true);
		simpleTest("Test 3C: Negative", "T", true);
		simpleTest("Test 3D: Negative", "trUe", true);
		print(String.format("%n%n"));

		print(String.format("TESTING BAD VALUES:%n"));
		badTest("Test 4A: Negative", "2", true);
		badTest("Test 4B: Negative", "BAD", true);
		print(String.format("%n%n"));
	}


}