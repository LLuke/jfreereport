/*
 * File is generated by 'Unit Tests Generator' developed under
 * 'Web Test Tools' project at http://sf.net/projects/wttools/
 * Copyright (C) 2001 "Artur Hefczyc" <kobit@users.sourceforge.net>
 * to all 'Web Test Tools' subprojects.
 *
 * No rigths to files and no responsibility for code generated
 * by this tool are belonged to author of 'unittestsgen' utility.
 *
 * $Id: SampleReport1TestCase.java,v 1.1 2002/07/08 22:10:36 taqua Exp $
 * $Author: taqua $
 * $Date: 2002/07/08 22:10:36 $
 */
package com.jrefinery.report.ext.junit.base.demo;

import com.jrefinery.report.demo.SampleReport1;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;

/**
 * File <code>SampleReport1TestCase.java</code> is automaticaly generated by
 * 'unittestsgen' application. Code generator is created for java
 * sources and for 'junit' package by "Artur Hefczyc"
 * <kobit@users.sourceforge.net><br/>
 * You should fulfil test methods with proper code for testing
 * purpose. All methods where you should put your code are below and
 * their names starts with 'test'.<br/>
 * You can run unit tests in many ways, however prefered are:
 * <ul>
 *   <li>Run tests for one class only, for example for this class you
 *       can run tests with command:
 *     <pre>
 *       java -cp "jar/thisjarfile.jar;lib/junit.jar" com.jrefinery.report.demo.SampleReport1TestCase
 *     </pre>
 *   </li>
 *   <li>Run tests for all classes in one command call. Code generator
 *       creates also <code>com.jrefinery.report.ext.junit.TestAll.class</code> which runs all
 *       available tests:
 *     <pre>
 *       java -cp "jar/thisjarfile.jar;lib/junit.jar" com.jrefinery.report.ext.junit.TestAll
 *     </pre>
 *   </li>
 *   <li>But the most prefered way is to run all tests from
 *     <em>Ant</em> just after compilation process finished.<br/>
 *     To do it. You need:
 *     <ol>
 *       <li>Ant package from
 *         <a href="http://jakarta.apache.org/">Ant</a>
 *       </li>
 *       <li>JUnit package from
 *         <a href="http://www.junit.org/">JUnit</a>
 *       </li>
 *       <li>Put some code in your <code>build.xml</code> file
 *         to tell Ant how to test your package. Sample code for
 *         Ant's <code>build.xml</code> you can find in created file:
 *         <code>sample-junit-build.xml</code>. And remember to have
 *         <code>junit.jar</code> in CLASSPATH <b>before</b> you run Ant.
 *         To generate reports by ant you must have <code>xalan.jar</code>
 *         in your <code>ANT_HOME/lib/</code> directory.
 *       </li>
 *     </ol>
 *   </li>
 * </ul>
 */
public class SampleReport1TestCase extends TestCase
{
  /**
   * Instance of tested class.
   */
  protected SampleReport1 varSampleReport1;

  /**
   * Public constructor for creating testing class.
   */
  public SampleReport1TestCase (String name)
  {
    super (name);
  } // end of SampleReport1TestCase(String name)

  /**
   * This main method is used for run tests for this class only
   * from command line.
   */
  public static void main (String[] args)
  {
    junit.textui.TestRunner.run (suite ());
  } // end of main(Stringp[] args)

  /**
   * This method is called every time before particular test execution.
   * It creates new instance of tested class and it can perform some more
   * actions which are necessary for performs tests.
   */
  protected void setUp ()
  {
    varSampleReport1 = new SampleReport1 ();
  } // end of setUp()

  /**
   * Returns all tests which should be performed for testing class.
   * By default it returns only name of testing class. Instance of this
   * is then created with its constructor.
   */
  public static Test suite ()
  {
    return new TestSuite (SampleReport1TestCase.class);
  } // end of suite()

  /**
   * for classes which doesn't contain any methods here is one additional
   * method for performing test on such classes.
   */
  public void testNoMethods ()
  {
  }

  /**
   * Method for testing how works original method:
   * com.jrefinery.report.JFreeReport createReport()
   * from tested class
   */
  public void testCreateReport ()
  {

    assertTrue ("Warning! This new test method with no real test code inside.", false);

  } // end of testCreateReport()

} // end of SampleReport1TestCase
