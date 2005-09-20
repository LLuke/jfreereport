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
 * $Id: TotalItemCountTest.java,v 1.7 2005/09/19 13:34:24 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.functionality;

import junit.framework.TestCase;
import org.jfree.report.Group;
import org.jfree.report.GroupList;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.world.CountryReportSecurityXMLDemoHandler;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.FunctionUtilities;
import org.jfree.report.function.TotalItemCountFunction;

public class TotalItemCountTest extends TestCase
{
  private static final int[] GROUPCOUNTS = new int[]{
    2, 3, 1, 14, 2, 1
  };

  private static class TotalItemCountVerifyFunction
      extends AbstractFunction
  {
    private int index;

    /**
     * Creates an unnamed function. Make sure the name of the function is set using
     * {@link #setName} before the function is added to the report's function collection.
     */
    public TotalItemCountVerifyFunction()
    {
      setName("verification");
    }

    /**
     * Receives notification that report generation initializes the current run.
     * <P>
     * The event carries a ReportState.Started state.  Use this to initialize the report.
     *
     * @param event The event.
     */
    public void reportInitialized(ReportEvent event)
    {
      index = 0;
    }

    /**
     * Receives notification that a group has finished.
     *
     * @param event  the event.
     */
    public void groupFinished(ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }
      assertCount(event);
      index += 1;
    }

    /**
     * Receives notification that a group has started.
     *
     * @param event  the event.
     */
    public void groupStarted(ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }
      assertCount(event);
    }

    private void assertCount(ReportEvent event)
    {
      // The itemcount function is only valid within the defined group.
      if (FunctionUtilities.getCurrentGroup(event).getName().equals("Continent Group"))
      {
        // the number of continents in the report1
        Number n = (Number) event.getDataRow().get("continent-total-gc");
        assertEquals("continent-total-gc", GROUPCOUNTS[index], n.intValue());
      }

      // the number of continents in the report1 + default group start
      Number n2 = (Number) event.getDataRow().get("total-gc");
      assertEquals("total-gc", 23, n2.intValue());
    }

    public Object getValue()
    {
      return null;
    }
  }

  public TotalItemCountTest()
  {
  }

  public TotalItemCountTest(final String s)
  {
    super(s);
  }

  public void testGroupItemCount() throws ReportDefinitionException
  {
    CountryReportSecurityXMLDemoHandler demoHandler = new CountryReportSecurityXMLDemoHandler();
    JFreeReport report = demoHandler.createReport();
    report.addExpression(new TotalItemCountVerifyFunction());
    GroupList list = report.getGroups();
    // make sure that there is no default group ...
    Group g = list.getGroupByName("default");
    if (g != null)
    {
      list.remove(g);
    }
    report.setGroups(list);

    TotalItemCountFunction f = new TotalItemCountFunction();
    f.setName("continent-total-gc");
    f.setGroup("Continent Group");
    f.setDependencyLevel(1);
    report.addExpression(f);

    TotalItemCountFunction f2 = new TotalItemCountFunction();
    f2.setName("total-gc");
    f2.setDependencyLevel(1);
    report.addExpression(f2);

    assertTrue(FunctionalityTestLib.execGraphics2D(report));


  }
}
