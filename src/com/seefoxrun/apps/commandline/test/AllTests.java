package com.seefoxrun.apps.commandline.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses(
		{
			  CheckedOptionTest.class,
			  DirOptionTest.class,
			  FileOptionTest.class,
			  BooleanOptionTest.class,
			  IntegerOptionTest.class,
			  FloatOptionTest.class
		}
	)
public class AllTests {
    
}