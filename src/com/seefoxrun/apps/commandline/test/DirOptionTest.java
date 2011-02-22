package com.seefoxrun.apps.commandline.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.* ;

import com.seefoxrun.apps.commandline.parser.Parser;
import com.seefoxrun.apps.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.apps.commandline.parser.exceptions.OptionDefinitionException;
import com.seefoxrun.apps.commandline.parser.options.CheckedOption;
import com.seefoxrun.apps.commandline.parser.options.DirOption;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Tests DirOption class for correct handling of directory
 * existence checks, dir creation, and basic functionality.
 * 
 * @author brianfox
 */
public class DirOptionTest {

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

	
	// gets a temporary directory
	private String getTempDirPath() throws IOException {
		File f = File.createTempFile("unit_test", "");
		f.delete();
		return f.getPath() + ".dir";
	}

	
	// pretty print
	@BeforeClass
    public static void pre() {
        print(String.format("Test Class: DirOptionTest%n"));        
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
	 * @throws IOException
	 */	@Test
	public void test_basic() throws CommandLineException {
		ArrayList<CheckedOption> options;
		print(String.format("TESTING BASIC FUNCTIONALITY:%n"));
		print("Test 1A: Construction.",60);
		DirOption dir = new DirOption('o', "option", "My option", MANDATORY);
		options = new ArrayList<CheckedOption>();
		options.add(dir);
		new Parser(options, new String[]{"-o/tmp/"});
		print(String.format("Result: %s%n", dir.getValue()));
		File f = dir.getValue();
		assertNotNull(f);
		assert(f.getPath() == "/tmp");
		
		print("Test 1B: Base Directory.",60);
		dir = new DirOption('o', "option", "My option", MANDATORY);
		dir.setBaseDir("/basedir/");
		options = new ArrayList<CheckedOption>();
		options.add(dir);
		new Parser(options, new String[]{"-o/relativedir/"});
		print(String.format("Result: %s%n", dir.getValue()));
		f = dir.getValue();
		assertNotNull(f);
		assert(f.getPath() == "/basedir/relativedir");

		String name = System.getProperty("user.name");
		String home = System.getProperty("user.home");

		print("Test 1C: Tilde Expansion.",60);
		dir = new DirOption('o', "option", "My option", MANDATORY);
		dir.setExpandTilde(true);
		options = new ArrayList<CheckedOption>();
		options.add(dir);
		new Parser(options, new String[]{"-o ~/help"});
		print(String.format("Result: %s%n", dir.getValue()));
		f = dir.getValue();
		assertNotNull(f);
		assertEquals(f.getPath(), new File(home + File.separator + "/help").getPath());

		print("Test 1D: Tilde Expansion 2.",60);
		dir = new DirOption('o', "option", "My option", MANDATORY);
		dir.setExpandTilde(true);
		options = new ArrayList<CheckedOption>();
		options.add(dir);
		new Parser(options, new String[]{"-o ~" + name + "/help2"});
		print(String.format("Result: %s%n", dir.getValue()));
		f = dir.getValue();
		assertNotNull(f);
		assertEquals(f.getPath(), new File(home + File.separator + "/help2").getPath());

		print(String.format("%n%n"));
	}


	/**
	 * Yea Ole' Test.
	 * 
	 * @throws OptionDefinitionException
	 * @throws IOException
	 */
	@Test
	public void test_mustExist() throws IOException, CommandLineException {
		print(String.format("TESTING EXISTANCE CHECKS:%n"));

		ArrayList<CheckedOption> options;
		DirOption dir;
		File f;
		
		print("Test 2A: Must exist, Does exist.",60);
		dir = new DirOption('o', "option", "My option", MANDATORY);
		dir.setMustExist(true);
		options = new ArrayList<CheckedOption>();
		options.add(dir);
		new Parser(options, new String[]{"-o/tmp/"});
		print(String.format("Result: %s%n", dir.getValue()));
		f = dir.getValue();
		assertNotNull(f);
		assert(f.getPath() == "/tmp");

		
		print("Test 2B: Must exist, Doesn't exist.",60);
		dir = new DirOption('o', "option", "My option", MANDATORY);
		dir.setMustExist(true);
		options = new ArrayList<CheckedOption>();
		options.add(dir);
		try {
			new Parser(options, new String[]{"-o/tmp_fake/"});
			fail("Expected an error to be throw.");
		}
		catch (Exception e) {

		}
		print(String.format("Result: %s%n", dir.getValue()));
		f = dir.getValue();
		assertNull(f);

		
		print("Test 2C: Must exist, Exists, Wrong type.",60);
		dir = new DirOption('o', "option", "My option", MANDATORY);
		dir.setMustExist(true);
		options = new ArrayList<CheckedOption>();
		options.add(dir);
		
		String dirname = getTempDirPath();
		f = new File(dirname);
		f.createNewFile();
		assertTrue(f.exists());
		assertTrue(f.isFile());
		
		try {
			new Parser(options, new String[]{"-o" + dirname});
			fail("Expected an error to be throw.");
		}
		catch (Exception e) {
			print(String.format("Result: %s%n", e.getMessage()));
		}
		f.delete();
		print(String.format("%n%n"));
	}


	/**
	 * Yea Ole' Test.
	 * 
	 * @throws OptionDefinitionException
	 * @throws IOException
	 */
	@Test
	public void test_create() throws IOException, CommandLineException {
		print(String.format("TESTING CREATION:%n"));

		ArrayList<CheckedOption> options;
		DirOption dir;
		File f;
		
		print("Test 3A: Should be created, doesn't exist.",60);

		dir = new DirOption('o', "option", "My option", MANDATORY).setCreateDir(true);
		options = new ArrayList<CheckedOption>();
		options.add(dir);
		
		String dirname = getTempDirPath();
		new File(dirname).delete();

		new Parser(options, new String[]{"-o" + dirname});
		print(String.format("Result: %s%n", dir.getValue()));
		f = new File(dirname);
		assertTrue(f.exists());
		assertTrue(f.isDirectory());
		f.delete();
		assertTrue(!f.exists());



		print("Test 3B: Should be created, already exists.",60);

		dir = new DirOption('o', "option", "My option", MANDATORY).setCreateDir(true);
		options = new ArrayList<CheckedOption>();
		options.add(dir);
		
		dirname = getTempDirPath();
		new File(dirname).mkdirs();

		new Parser(options, new String[]{"-o" + dirname});
		print(String.format("Result: %s%n", dir.getValue()));
		f = new File(dirname);
		assertTrue(f.exists());
		assertTrue(f.isDirectory());
		f.delete();
		assertTrue(!f.exists());
	}

}