/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * GroupCountBug.java
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
package org.jfree.report.ext.junit.bugs;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.function.GroupCountFunction;
import org.jfree.report.states.ReportState;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.base.ReportStateList;
import org.jfree.report.modules.output.pageable.graphics.G2OutputTarget;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case has been compiled in response to a bug report by Steven Feinstein:
 * <p>
 * http://www.object-refinery.com/phorum-3.3.2a/read.php?f=7&i=12&t=12
 * <p>
 * A new group is not being generated for the last row of data in a report.
 */
public class GroupCountBug extends TestCase
{

  private JFreeReport report;
  private OutputTarget target;

  /**
   * Returns the tests as a test suite.
   */
  public static Test suite()
  {
    return new TestSuite(GroupCountBug.class);
  }

  /**
   * Constructs a new set of tests.
   * @param name The name of the tests.
   */
  public GroupCountBug(final String name)
  {
    super(name);
  }

  /**
   * Common test setup.
   */
  protected void setUp()
  {

    final String[][] values = new String[][]{{"A", "1"}, {"A", "2"}, {"B", "3"}};
    final String[] columns = new String[]{"Letter", "Number"};
    final TableModel data = new DefaultTableModel(values, columns);

    this.report = new JFreeReport();
    this.report.setName("Test Report");
    this.report.setData(data);
    final ArrayList fields = new ArrayList();
    fields.add("Letter");
    final Group letterGroup = new Group();
    letterGroup.setName("Letter Group");
    letterGroup.setFields(fields);
    this.report.addGroup(letterGroup);

    final GroupCountFunction function = new GroupCountFunction();
    function.setName("f1");
    function.setGroup("Letter Group");
    try
    {
      this.report.addFunction(function);
    }
    catch (Exception e)
    {
      fail();
    }

    final BufferedImage buffer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    final Graphics2D g2 = (Graphics2D) buffer.getGraphics();
    this.target = new G2OutputTarget(g2, new PageFormat());

  }

  /**
   * Counts the number of groups.
   */
  public void testGroupCount() throws Exception
  {
    final PageableReportProcessor proc = new PageableReportProcessor(report);
    proc.setOutputTarget(target);
    final ReportStateList list = proc.repaginate();
    final ReportState state = list.get(list.size() - 1);
    System.out.println(state.getClass().getName());
    final Integer value = (Integer) state.getDataRow().get("f1");
    assertEquals(new Integer(2), value);

  }

}
