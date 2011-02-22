package com.seefoxrun.app.commandline.test;

import static org.junit.Assert.*;

import org.junit.* ;

import com.seefoxrun.app.commandline.CommandLineApp;
import com.seefoxrun.app.commandline.parser.Parser;
import com.seefoxrun.app.commandline.parser.exceptions.OptionDefinitionException;
import com.seefoxrun.app.commandline.parser.options.BooleanOption;
import com.seefoxrun.app.commandline.parser.options.CheckedOption;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Tests BooleanOption for correct handling of permitted argument
 * values (y, yes, true, t, no, n, false, f, 0, and 1).  Also 
 * checks invalid arguments. 
 * 
 * @author Brian Fox
 */
public class BooleanOptionTest {

	
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
	public static void print(String s) {
		System.out.print(s);
	}

	
	// convenience method for creating a temp file path
	public String getTempFilePath() throws IOException {
		File f = File.createTempFile("unit_test", "");
		f.delete();
		return f.getPath() + ".file";
	}
	
	
	// pretty print
	@BeforeClass
    public static void pre() {
        print(String.format("Test Class: BooleanOptionTest%n"));        
        print(String.format("-----------------------------%n"));        
    }
	
	
	// pretty print
	@AfterClass
    public static void post() {
        print(String.format("%n%n%n"));                
    }


	// template test method: we expect good results here 
	public void simpleTest(
			String title, 
			String argument, 
			boolean expected
	) throws OptionDefinitionException {

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
	
	
	// template test method: we expect an exception here
	public void badTest(
			String title, 
			String argument, 
			boolean expected
	) throws OptionDefinitionException {

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
			print(String.format(
					"Result: Error thrown: %s%n", e.getMessage()
			));
		}
	}

	
	/**
	 * Yea Ole' Test.
	 * 
	 * @throws OptionDefinitionException
	 * @throws IOException
	 */
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
		simpleTest("Test 2A: Positive", "1", true);
		simpleTest("Test 2B: Positive", "y", true);
		simpleTest("Test 2C: Positive", "yes", true);
		simpleTest("Test 2D: Positive", "t", true);
		simpleTest("Test 2E: Positive", "true", true);
		print(String.format("%n%n"));

		print(String.format("TESTING CASE SENSITIVITY:%n"));
		simpleTest("Test 3A: Positive", "Y", true);
		simpleTest("Test 3B: Positive", "yeS", true);
		simpleTest("Test 3C: Positive", "T", true);
		simpleTest("Test 3D: Positive", "trUe", true);
		print(String.format("%n%n"));

		print(String.format("TESTING BAD VALUES:%n"));
		badTest("Test 4A: Bad", "2", true);
		badTest("Test 4B: Bad", "BAD", true);
		print(String.format("%n%n"));
	}


}