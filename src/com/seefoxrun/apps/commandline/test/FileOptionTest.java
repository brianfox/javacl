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
import com.seefoxrun.apps.commandline.parser.options.FileOption;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Tests FileOption class for correct handling of file
 * existence checks, file creation, and basic functionality.
 * 
 * @author Brian Fox
 */
public class FileOptionTest {

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
	private String getTempFilePath() throws IOException {
		File f = File.createTempFile("unit_test", "");
		f.delete();
		return f.getPath() + ".file";
	}
	
	
	@BeforeClass
    public static void pre() {
        print(String.format("Test Class: FileOptionTest%n"));        
        print(String.format("-----------------------------%n"));        
    }
	

	@AfterClass
    public static void post() {
        print(String.format("%n%n%n"));                
    }

	
	/**
	 * Yea Ole' Test.
	 * 
	 * @throws OptionDefinitionException
	 * @throws IOException
	 */
	@Test
	public void test_basic() throws IOException, CommandLineException {
		ArrayList<CheckedOption> options;
		String filename;
		
		print(String.format("TESTING BASIC FUNCTIONALITY:%n"));
		print("Test 1A: Construction.",60);
		FileOption fo = new FileOption('o', "option", "My option", MANDATORY);
		options = new ArrayList<CheckedOption>();
		options.add(fo);
		filename = getTempFilePath();
		new Parser(options, new String[]{"-o" + filename});
		print(String.format("Result: %s%n", fo.getValue()));
		File f = fo.getValue();
		assertNotNull(f);
		assertEquals(f.getPath(), filename);
		
		print("Test 1B: Base Directory.",60);
		fo = new FileOption('o', "option", "My option", MANDATORY);
		fo.setBaseDir("/basedir/");
		options = new ArrayList<CheckedOption>();
		options.add(fo);
		filename = getTempFilePath();
		new Parser(options, new String[]{"-o" + filename});
		print(String.format("Result: %s%n", fo.getValue()));
		f = fo.getValue();
		assertNotNull(f);
		assert(f.getPath() == "/basedir/file");

		String name = System.getProperty("user.name");
		String home = System.getProperty("user.home");

		print("Test 1C: Tilde Expansion.",60);
		fo = new FileOption('o', "option", "My option", MANDATORY);
		fo.setExpandTilde(true);
		options = new ArrayList<CheckedOption>();
		options.add(fo);
		new Parser(options, new String[]{"-o ~/never_make_this_file___"});
		print(String.format("Result: %s%n", fo.getValue()));
		f = fo.getValue();
		assertNotNull(f);
		assertEquals(f.getPath(), new File(home + File.separator + "/never_make_this_file___").getPath());

		print("Test 1D: Tilde Expansion 2.",60);
		fo = new FileOption('o', "option", "My option", MANDATORY);
		fo.setExpandTilde(true);
		options = new ArrayList<CheckedOption>();
		options.add(fo);
		new Parser(options, new String[]{"-o ~" + name + "/never_make_this_file___"});
		print(String.format("Result: %s%n", fo.getValue()));
		f = fo.getValue();
		assertNotNull(f);
		assertEquals(f.getPath(), new File(home + File.separator + "/never_make_this_file___").getPath());

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
		FileOption fo;
		File f;
		String filename;
		
		
		filename = getTempFilePath();
		f = new File(filename);
		f.createNewFile();

		print("Test 2A: Must exist, Does exist.",60);
		fo = new FileOption('o', "option", "My option", MANDATORY);
		fo.setMustExist(true);
		options = new ArrayList<CheckedOption>();
		options.add(fo);
		new Parser(options, new String[]{"-o" + filename});
		print(String.format("Result: %s%n", fo.getValue()));
		f = fo.getValue();
		assertNotNull(f);
		assert(f.getPath() == filename);
		f.delete();
		
		
		print("Test 2B: Must exist, Doesn't exist.",60);
		filename = getTempFilePath();
		f = new File(filename);

		fo = new FileOption('o', "option", "My option", MANDATORY);
		fo.setMustExist(true);
		options = new ArrayList<CheckedOption>();
		options.add(fo);
		try {
			new Parser(options, new String[]{"-o" + filename});
			fail("Expected an error to be throw.");
		}
		catch (Exception e) {
			print(String.format("Result: Doesn't exist.%n", fo.getValue()));
		}
		f = fo.getValue();
		assertNull(f);

		
		print("Test 2C: Must exist, Exists, Wrong type.",60);
		fo = new FileOption('o', "option", "My option", MANDATORY);
		fo.setMustExist(true);
		options = new ArrayList<CheckedOption>();
		options.add(fo);
		
		String foname = getTempFilePath();
		f = new File(foname);
		f.mkdirs();
		assertTrue(f.exists());
		assertTrue(!f.isFile());
		
		try {
			new Parser(options, new String[]{"-o" + foname});
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
		FileOption fo;
		File f;
		String filename;
		
		print("Test 3A: Should be created, doesn't exist.",60);

		fo = new FileOption('o', "option", "My option", MANDATORY).setCreateFile(true);
		options = new ArrayList<CheckedOption>();
		options.add(fo);
		
		filename = getTempFilePath();
		new File(filename).delete();

		new Parser(options, new String[]{"-o" + filename});
		print(String.format("Result: %s%n", fo.getValue()));
		f = new File(filename);
		assertTrue(f.exists());
		assertTrue(f.isFile());
		f.delete();
		assertTrue(!f.exists());



		print("Test 3B: Should be created, already exists.",60);

		fo = new FileOption('o', "option", "My option", MANDATORY).setCreateFile(true);
		options = new ArrayList<CheckedOption>();
		options.add(fo);
		
		filename = getTempFilePath();
		new File(filename).createNewFile();

		new Parser(options, new String[]{"-o" + filename});
		print(String.format("Result: %s%n", fo.getValue()));
		f = new File(filename);
		assertTrue(f.exists());
		assertTrue(f.isFile());
		f.delete();
		assertTrue(!f.exists());

	
		print(String.format("%n%n"));

	}

}