package com.jrefinery.report.junit;

import junit.framework.*;
import com.jrefinery.data.junit.*;
import com.jrefinery.date.junit.*;

/**
 * A test suite for the JFreeReport class library that can be run using JUnit (http://www.junit.org).
 */
public class JFreeReportTestSuite extends TestCase {

    /**
     * Returns a test suite to the JUnit test runner.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("JFreeReport");
        suite.addTest(GeneratorTests.suite());
        return suite;
    }

    /**
     * Constructs the test suite.
     */
    public JFreeReportTestSuite(String name) {
        super(name);
    }

}