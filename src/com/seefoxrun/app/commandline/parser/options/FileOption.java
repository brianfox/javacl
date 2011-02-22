package com.seefoxrun.app.commandline.parser.options;

import java.io.File;
import java.io.IOException;

import com.seefoxrun.app.commandline.parser.exceptions.ArgumentException;
import com.seefoxrun.app.commandline.parser.exceptions.CommandLineException;
import com.seefoxrun.app.commandline.parser.exceptions.OptionDefinitionException;

/**
 * A CheckedOption which supports file system files.  A FileOption's
 * argument represents the path of a specific directory on the file system.
 * <br><br>  
 * Various checks are performed during command-line parsing.  The path, if 
 * it already exists, must be a file or else an ArgumentException is 
 * thrown.  If the path does not exist and this option's mustExist flag is 
 * set, an ArgumentException is thrown.  Similar checks or placed on read 
 * and write permissions.
 * <br><br>
 * FileOption does support tilde expansion for the user's home directory.
 * But it only supports the current user.  Assuming the current process
 * is being run by bill, Both ~/ and ~bill/ will expand correctly.  ~/susie
 * will throw an ArgumentException.
 * <br><br>
 * The baseDir() method allows this option to accept relative paths.  
 * Setting a base directory and using tilde expansion will throw an 
 * ArgumentException. 
 * <br><br>
 * @author Brian Fox
 */
public class FileOption extends CheckedOption {

	private boolean mustExist = false;
	private boolean canRead = false;
	private boolean canWrite = false;
	private boolean create = false;
	private boolean tildeExpansion = false;
	private String baseDir = null;
	private File val;
	
	public FileOption(Character shortOpt, String longOpt, String description, boolean mandatory) throws OptionDefinitionException {
		super(shortOpt, longOpt, description, mandatory);
	}

	/**
	 * If set true, the directory will be created if it doesn't already exist.
	 * 
	 * @return
	 */
	public FileOption setCreateFile(boolean val) {
		this.create = val;
		return this;
	}


	/**
	 * Sets the base directory for this option.  Setting the base directory 
	 * and using tilde expansion will result in an error.
	 * 
	 * @param base String representing base directory
	 * @return 
	 * @throws OptionDefinitionException 
	 */
	public FileOption setBaseDir(String base) throws OptionDefinitionException {
		this.baseDir = base;
		if (tildeExpansion && baseDir != null)
			throw new OptionDefinitionException("" +
					"tilde expansion cannot be used with a base dir"
					);
		return this;
	}
	
	
	
	/**
	 * If set true, an error will be thrown if the directory does not exist.
	 * 
	 * @return
	 */
	public FileOption setMustExist(boolean val) {
		this.mustExist = val;
		return this;
	}

	
	public FileOption setCanRead(boolean val) {
		this.canRead = val;
		return this;
	}

	
	public FileOption setCanWrite(boolean val) {
		this.canWrite = val;
		return this;
	}

	
	/**
	 * If set true, tilde expansion will be used.  Using both tilde expansion
	 * and a base directory will result in an error.
	 * 
	 * @param val
	 * @return
	 * @throws OptionDefinitionException
	 */
	public FileOption setExpandTilde(boolean val) throws OptionDefinitionException {
		this.tildeExpansion = val;
		if (tildeExpansion && baseDir != null)
			throw new OptionDefinitionException("" +
					"tilde expansion cannot be used with a base dir"
					);
		return this;
	}

	
	@Override
	public File getValue() {
		return val;
	}
	

	@Override
	public void setValue(String s) throws CommandLineException {
		s = s.trim();
		if (tildeExpansion || true) {
			String home = System.getProperty("user.home");
			String name = System.getProperty("user.name");
			if (s.startsWith("~" + File.separator))
				s = home + s.substring(1);
			if (s.startsWith("~" + name + File.separator))	
				s = home + s.substring(name.length() + 1);
		}
		
		if (baseDir != null) {
			s = (baseDir + File.separator + s);
		}
		
		super.setValue(s);
		File f = new File(s);
		if (f.exists() && !f.isFile())
			throw new ArgumentException(
					"path found, but is not a file: " + s
			);
		if (this.mustExist && !f.exists())
			throw new ArgumentException(
					"directory must exist: " + s
			);
		if (this.canRead && !f.canRead())
			throw new ArgumentException(
					"directory must be readable: " + s
			);
		if (this.canWrite && !f.canWrite())
			throw new ArgumentException(
					"directory must be writable: " + s
			);
		if (this.create && !f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				throw new CommandLineException("cannot create file: " + s);
			}
		val = f;
	}
	
	
}
