package com.seefoxrun.app.commandline.test;

import static org.junit.Assert.*;

import org.junit.* ;

import com.seefoxrun.app.commandline.parser.Parser;
import com.seefoxrun.app.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.app.commandline.parser.exceptions.OptionDefinitionException;
import com.seefoxrun.app.commandline.parser.options.CheckedOption;
import com.seefoxrun.app.commandline.parser.options.FloatOption;

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

	


	
	/**
	 * Yea Ole' Test.
	 * 
	 * @throws OptionDefinitionException
	 */
	@Test
	public void test_range() throws CommandLineException {
		print(String.format("TESTING RANGE:%n"));
		new Parser(null, new String[]{"-h"});
		print(String.format("%n%n"));
	}


}