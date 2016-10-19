package com.sporthenon.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Sporthenon Test Suite");
		//$JUnit-BEGIN$
		suite.addTestSuite(EntityTest.class);
		suite.addTestSuite(FunctionTest.class);
		//$JUnit-END$
		return suite;
	}

}
