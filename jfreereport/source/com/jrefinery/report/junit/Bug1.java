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

import com.jrefinery.report.G2OutputTarget;
import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.OutputTarget;
import com.jrefinery.report.ReportState;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.GroupCountFunction;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.util.ArrayList;

/**
 * This test case has been compiled in response to a bug report by Steven Feinstein:
 * <p>
 * http://www.object-refinery.com/phorum-3.3.2a/read.php?f=7&i=12&t=12
 * <p>
 * A new group is not being generated for the last row of data in a report.
 */
public class Bug1 extends TestCase
{

  JFreeReport report;
  OutputTarget target;

  /**
   * Returns the tests as a test suite.
   */
  public static Test suite ()
  {
    return new TestSuite (Bug1.class);
  }

  /**
   * Constructs a new set of tests.
   * @param The name of the tests.
   */
  public Bug1 (String name)
  {
    super (name);
  }

  /**
   * Common test setup.
   */
  protected void setUp ()
  {

    String[][] values = new String[][]{{"A", "1"}, {"A", "2"}, {"B", "3"}};
    String[] columns = new String[]{"Letter", "Number"};
    TableModel data = new DefaultTableModel (values, columns);

    this.report = new JFreeReport ();
    this.report.setName ("Test Report");
    this.report.setData (data);
    ArrayList fields = new ArrayList ();
    fields.add ("Letter");
    Group letterGroup = new Group ();
    letterGroup.setName ("Letter Group");
    letterGroup.setFields(fields);
    this.report.addGroup (letterGroup);

    GroupCountFunction function = new GroupCountFunction ();
    function.setName ("f1");
    function.setGroup("Letter Group");
    try
    {
      this.report.addFunction (function);
    }
    catch (Exception e)
    {
      this.fail ();
    }

    BufferedImage buffer = new BufferedImage (100, 100, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = (Graphics2D) buffer.getGraphics ();
    this.target = new G2OutputTarget (g2, new PageFormat ());

  }

  /**
   * Counts the number of groups.
   */
  public void testGroupCount () throws Exception
  {

    ReportState state = this.report.processReport (target, false);
    System.out.println (state.getClass().getName ());
    Function function = state.getFunctions ().get ("f1");
    Integer value = (Integer) function.getValue ();
    this.assertEquals (new Integer (2), value);

  }

}