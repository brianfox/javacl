package com.seefoxrun.app.commandline.test;

import static org.junit.Assert.*;

import org.junit.* ;

import com.seefoxrun.app.commandline.parser.Parser;
import com.seefoxrun.app.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.app.commandline.parser.exceptions.OptionDefinitionException;
import com.seefoxrun.app.commandline.parser.options.CheckedOption;
import com.seefoxrun.app.commandline.parser.options.IntegerOption;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Tests IntegerOption class for correct handling of integer
 * command-line arguments.  Min and max values are tested
 * as well.
 * 
 * @author Brian Fox
 */
public class GNU_4_7_1_Test {

	
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
	



	@Test
	public void test_basic() throws IOException, CommandLineException {
		print(String.format("TESTING BASIC FUNCTIONALITY:%n"));
		new Parser(null, new String[]{"-h"});
		print(String.format("%n%n"));
	}

	
}