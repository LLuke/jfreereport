/*
 * File is generated by 'Unit Tests Generator' developed under
 * 'Web Test Tools' project at http://sf.net/projects/wttools/
 * Copyright (C) 2001 "Artur Hefczyc" <kobit@users.sourceforge.net>
 * to all 'Web Test Tools' subprojects.
 *
 * No rigths to files and no responsibility for code generated
 * by this tool are belonged to author of 'unittestsgen' utility.
 *
 * $Id:$
 * $Author:$
 * $Date:$
 */
package com.jrefinery.report.ext.junit.base.targets;

import junit.framework.*;
import junit.extensions.*;
import java.awt.Toolkit;
import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;

import com.jrefinery.report.targets.G2OutputTarget;

/**
 * File <code>G2OutputTargetTestCase.java</code> is automaticaly generated by
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
 *       java -cp "jar/thisjarfile.jar;lib/junit.jar" com.jrefinery.report.targets.G2OutputTargetTestCase
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
public class G2OutputTargetTestCase extends TestCase
{
  /**
   * Instance of tested class.
   */
  protected G2OutputTarget varG2OutputTarget;

  /**
   * Public constructor for creating testing class.
   */
  public G2OutputTargetTestCase(String name) {
    super(name);
  } // end of G2OutputTargetTestCase(String name)
  /**
   * This main method is used for run tests for this class only
   * from command line.
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  } // end of main(Stringp[] args)
  /**
   * This method is called every time before particular test execution.
   * It creates new instance of tested class and it can perform some more
   * actions which are necessary for performs tests.
   */
  protected void setUp() {
    // Initialize your variable(s) here
    // for example:
    // varG2OutputTarget = new G2OutputTarget(...);
    // But note that there is no default constructor in G2OutputTarget
  } // end of setUp()
  /**
   * Returns all tests which should be performed for testing class.
   * By default it returns only name of testing class. Instance of this
   * is then created with its constructor.
   */
  public static Test suite() {
    return new TestSuite(G2OutputTargetTestCase.class);
  } // end of suite()
  /**
   * for classes which doesn't contain any methods here is one additional
   * method for performing test on such classes.
   */
  public void testNoMethods() {
  }

  /**
   * Method for testing how works original method:
   * void beginPage()
   * from tested class
   */
  public void testBeginPage() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testBeginPage()

  /**
   * Method for testing how works original method:
   * void close()
   * from tested class
   */
  public void testClose() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testClose()

  /**
   * Method for testing how works original method:
   * java.awt.Graphics2D createEmptyGraphics()
   * from tested class
   */
  public void testCreateEmptyGraphics() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testCreateEmptyGraphics()

  /**
   * Method for testing how works original method:
   * void drawImage(com.jrefinery.report.ImageReference)
   * from tested class
   */
  public void testDrawImage1503576525() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testDrawImage1503576525(com.jrefinery.report.ImageReference)

  /**
   * Method for testing how works original method:
   * void drawShape(java.awt.Shape)
   * from tested class
   */
  public void testDrawShape2073736251() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testDrawShape2073736251(java.awt.Shape)

  /**
   * Method for testing how works original method:
   * void drawString(java.lang.String, int)
   * from tested class
   */
  public void testDrawString1195259493104431() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testDrawString1195259493104431(java.lang.String, int)

  /**
   * Method for testing how works original method:
   * void endPage()
   * from tested class
   */
  public void testEndPage() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testEndPage()

  /**
   * Method for testing how works original method:
   * void fillShape(java.awt.Shape)
   * from tested class
   */
  public void testFillShape2073736251() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testFillShape2073736251(java.awt.Shape)

  /**
   * Method for testing how works original method:
   * java.awt.Font getFont()
   * from tested class
   */
  public void testGetFont() {

/* No default constructor, code commented out...
    varG2OutputTarget.setFont((java.awt.Font)null);
    assertEquals((java.awt.Font)null, varG2OutputTarget.getFont());
*/
    assertTrue("Warning! This new test method with no real test code inside.", false);
/* No default constructor, code commented out...
    varG2OutputTarget.setFont((java.awt.Font)null);
    assertEquals((java.awt.Font)null, varG2OutputTarget.getFont());
*/

  } // end of testGetFont()

  /**
   * Method for testing how works original method:
   * float getFontHeight()
   * from tested class
   */
  public void testGetFontHeight() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testGetFontHeight()

  /**
   * Method for testing how works original method:
   * java.awt.Graphics2D getGraphics2D()
   * from tested class
   */
  public void testGetGraphics2D() {

/* No default constructor, code commented out...
    varG2OutputTarget.setGraphics2D((java.awt.Graphics2D)null);
    assertEquals((java.awt.Graphics2D)null, varG2OutputTarget.getGraphics2D());
*/
    assertTrue("Warning! This new test method with no real test code inside.", false);
/* No default constructor, code commented out...
    varG2OutputTarget.setGraphics2D((java.awt.Graphics2D)null);
    assertEquals((java.awt.Graphics2D)null, varG2OutputTarget.getGraphics2D());
*/

  } // end of testGetGraphics2D()

  /**
   * Method for testing how works original method:
   * java.awt.Paint getPaint()
   * from tested class
   */
  public void testGetPaint() {

/* No default constructor, code commented out...
    varG2OutputTarget.setPaint((java.awt.Paint)null);
    assertEquals((java.awt.Paint)null, varG2OutputTarget.getPaint());
*/
    assertTrue("Warning! This new test method with no real test code inside.", false);
/* No default constructor, code commented out...
    varG2OutputTarget.setPaint((java.awt.Paint)null);
    assertEquals((java.awt.Paint)null, varG2OutputTarget.getPaint());
*/

  } // end of testGetPaint()

  /**
   * Method for testing how works original method:
   * float getStringBounds(java.lang.String, int, int)
   * from tested class
   */
  public void testGetStringBounds1195259493104431104431() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testGetStringBounds1195259493104431104431(java.lang.String, int, int)

  /**
   * Method for testing how works original method:
   * java.awt.Stroke getStroke()
   * from tested class
   */
  public void testGetStroke() {

/* No default constructor, code commented out...
    try {
      varG2OutputTarget.setStroke((java.awt.Stroke)null);
    } catch (com.jrefinery.report.targets.OutputTargetException exc) {
      assertTrue("Call to varG2OutputTarget.setStroke((java.awt.Stroke)null); threw exception: com.jrefinery.report.targets.OutputTargetException", false);
    }
    assertEquals((java.awt.Stroke)null, varG2OutputTarget.getStroke());
*/
    assertTrue("Warning! This new test method with no real test code inside.", false);
/* No default constructor, code commented out...
    try {
      varG2OutputTarget.setStroke((java.awt.Stroke)null);
    } catch (com.jrefinery.report.targets.OutputTargetException exc) {
      assertTrue("Call to varG2OutputTarget.setStroke((java.awt.Stroke)null); threw exception: com.jrefinery.report.targets.OutputTargetException", false);
    }
    assertEquals((java.awt.Stroke)null, varG2OutputTarget.getStroke());
*/

  } // end of testGetStroke()

  /**
   * Method for testing how works original method:
   * void open(java.lang.String, java.lang.String)
   * from tested class
   */
  public void testOpen11952594931195259493() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testOpen11952594931195259493(java.lang.String, java.lang.String)

  /**
   * Method for testing how works original method:
   * void restoreState(java.lang.Object)
   * from tested class
   */
  public void testRestoreState1063877011() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testRestoreState1063877011(java.lang.Object)

  /**
   * Method for testing how works original method:
   * java.lang.Object saveState()
   * from tested class
   */
  public void testSaveState() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testSaveState()

  /**
   * Method for testing how works original method:
   * void setClippingArea(java.awt.geom.Rectangle2D)
   * from tested class
   */
  public void testSetClippingArea227781269() {

    assertTrue("Warning! This new test method with no real test code inside.", false);

  } // end of testSetClippingArea227781269(java.awt.geom.Rectangle2D)

  /**
   * Method for testing how works original method:
   * void setFont(java.awt.Font)
   * from tested class
   */
  public void testSetFont1041103787() {

/* No default constructor, code commented out...
    varG2OutputTarget.setFont((java.awt.Font)null);
    assertEquals((java.awt.Font)null, varG2OutputTarget.getFont());
*/
    assertTrue("Warning! This new test method with no real test code inside.", false);
/* No default constructor, code commented out...
    varG2OutputTarget.setFont((java.awt.Font)null);
    assertEquals((java.awt.Font)null, varG2OutputTarget.getFont());
*/

  } // end of testSetFont1041103787(java.awt.Font)

  /**
   * Method for testing how works original method:
   * void setGraphics2D(java.awt.Graphics2D)
   * from tested class
   */
  public void testSetGraphics2D548156537() {

/* No default constructor, code commented out...
    varG2OutputTarget.setGraphics2D((java.awt.Graphics2D)null);
    assertEquals((java.awt.Graphics2D)null, varG2OutputTarget.getGraphics2D());
*/
    assertTrue("Warning! This new test method with no real test code inside.", false);
/* No default constructor, code commented out...
    varG2OutputTarget.setGraphics2D((java.awt.Graphics2D)null);
    assertEquals((java.awt.Graphics2D)null, varG2OutputTarget.getGraphics2D());
*/

  } // end of testSetGraphics2D548156537(java.awt.Graphics2D)

  /**
   * Method for testing how works original method:
   * void setPaint(java.awt.Paint)
   * from tested class
   */
  public void testSetPaint2076707710() {

/* No default constructor, code commented out...
    varG2OutputTarget.setPaint((java.awt.Paint)null);
    assertEquals((java.awt.Paint)null, varG2OutputTarget.getPaint());
*/
    assertTrue("Warning! This new test method with no real test code inside.", false);
/* No default constructor, code commented out...
    varG2OutputTarget.setPaint((java.awt.Paint)null);
    assertEquals((java.awt.Paint)null, varG2OutputTarget.getPaint());
*/

  } // end of testSetPaint2076707710(java.awt.Paint)

  /**
   * Method for testing how works original method:
   * void setStroke(java.awt.Stroke)
   * from tested class
   */
  public void testSetStroke150273684() {

/* No default constructor, code commented out...
    try {
      varG2OutputTarget.setStroke((java.awt.Stroke)null);
    } catch (com.jrefinery.report.targets.OutputTargetException exc) {
      assertTrue("Call to varG2OutputTarget.setStroke((java.awt.Stroke)null); threw exception: com.jrefinery.report.targets.OutputTargetException", false);
    }
    assertEquals((java.awt.Stroke)null, varG2OutputTarget.getStroke());
*/
    assertTrue("Warning! This new test method with no real test code inside.", false);
/* No default constructor, code commented out...
    try {
      varG2OutputTarget.setStroke((java.awt.Stroke)null);
    } catch (com.jrefinery.report.targets.OutputTargetException exc) {
      assertTrue("Call to varG2OutputTarget.setStroke((java.awt.Stroke)null); threw exception: com.jrefinery.report.targets.OutputTargetException", false);
    }
    assertEquals((java.awt.Stroke)null, varG2OutputTarget.getStroke());
*/

  } // end of testSetStroke150273684(java.awt.Stroke)

} // end of G2OutputTargetTestCase
