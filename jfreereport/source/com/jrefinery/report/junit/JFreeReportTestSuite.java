/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * --------------------
 * Bug1.java
 * --------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 27-Apr-2002 : Version 1 (DG);
 */
package com.jrefinery.report.junit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A test suite for the JFreeReport class library that can be run using JUnit (http://www.junit.org).
 */
public class JFreeReportTestSuite extends TestCase
{

  /**
   * Returns a test suite to the JUnit test runner.
   */
  public static Test suite ()
  {
    TestSuite suite = new TestSuite ("JFreeReport");
    suite.addTest (GeneratorTests.suite ());
    return suite;
  }

  /**
   * Constructs the test suite.
   */
  public JFreeReportTestSuite (String name)
  {
    super (name);
  }

}