package com.jrefinery.report.junit;

import junit.framework.*;
import com.jrefinery.data.junit.*;

/**
 * Some tests for the reporting engine.
 * <P>
 * These tests can be run using JUnit (http://www.junit.org).
 */
public class GeneratorTests extends TestCase {

    /**
     * Returns a test suite to the JUnit test runner.
     */
    public static Test suite() {

        TestSuite suite = new TestSuite("com.jrefinery.report");
        suite.addTestSuite(Bug1.class);
        return suite;

    }

    /**
     * Constructs the test suite.
     */
    public GeneratorTests(String name) {
        super(name);
    }

}