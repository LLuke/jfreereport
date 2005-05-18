/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------------
 * TotalGroupSumTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TotalGroupCountTest.java,v 1.5 2005/02/22 20:28:10 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.functionality;

import java.net.URL;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.GroupList;
import org.jfree.report.Group;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.TotalGroupCountFunction;
import org.jfree.report.demo.SampleData1;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;
import org.jfree.util.ObjectUtilities;

public class TotalGroupCountTest extends TestCase
{
  private static class TotalGroupCountVerifyFunction
      extends AbstractFunction
  {
    /**
     * Creates an unnamed function. Make sure the name of the function is set using
     * {@link #setName} before the function is added to the report's function collection.
     */
    public TotalGroupCountVerifyFunction()
    {
      setName("verification");
    }

    /**
     * Receives notification that a group has finished.
     *
     * @param event  the event.
     */
    public void groupFinished(final ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }
      assertEvent(event);
    }

    /**
     * Receives notification that a group has started.
     *
     * @param event  the event.
     */
    public void groupStarted(final ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }
      assertEvent(event);
    }

    private void assertEvent(final ReportEvent event)
    {
      // the number of continents in the report1
      final Number n = (Number) event.getDataRow().get("continent-total-gc");
      assertEquals("continent-total-gc", 6, n.intValue());

      // the number of continents in the report1
      // we don't have the default group, so it should return the same as above
      final Number n2 = (Number) event.getDataRow().get("total-gc");
      assertEquals("total-gc", 6, n2.intValue());
    }

    public Object getValue()
    {
      return null;
    }
  }

  private static final FunctionalityTestLib.ReportTest REPORT2 =
      new FunctionalityTestLib.ReportTest("/org/jfree/report/demo/report1.xml",
          new SampleData1());

  public TotalGroupCountTest()
  {
  }

  public TotalGroupCountTest(final String s)
  {
    super(s);
  }

  public void testGroupCount()
  {
    final URL url = ObjectUtilities.getResource
            (REPORT2.getReportDefinition(), TotalGroupCountTest.class);
    assertNotNull(url);
    JFreeReport report = null;
    try
    {
      report = ReportGenerator.getInstance().parseReport(url);
      report.setData(REPORT2.getReportTableModel());
      final GroupList list = report.getGroups();
      // make sure that there is no default group ...
      final Group g = list.getGroupByName("default");
      if (g != null)
      {
        list.remove(g);
      }
      report.setGroups(list);
      report.addExpression(new TotalGroupCountVerifyFunction());

      final TotalGroupCountFunction f = new TotalGroupCountFunction();
      f.setName("continent-total-gc");
      f.setGroup("Continent Group");
      f.setDependencyLevel(1);
      report.addExpression(f);

      final TotalGroupCountFunction f2 = new TotalGroupCountFunction();
      f2.setName("total-gc");
      f2.setDependencyLevel(1);
      report.addExpression(f2);
    }
    catch (Exception e)
    {
      Log.debug("Failed to parse " + url, e);
      fail();
    }

    assertTrue(FunctionalityTestLib.execGraphics2D(report));

  }
}
