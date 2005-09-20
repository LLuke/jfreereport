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
 * EventOrderTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: EventOrderTest.java,v 1.9 2005/08/08 15:56:01 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.functionality;

import java.awt.geom.Rectangle2D;
import java.net.URL;
import javax.swing.table.DefaultTableModel;

import junit.framework.TestCase;
import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.groups.GroupsDemo;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.EventMonitorFunction;
import org.jfree.report.function.PageFunction;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

public class EventOrderTest extends TestCase
{
  private static class PageVerifyFunction extends AbstractFunction implements PageEventListener
  {
    public PageVerifyFunction()
    {
    }

    /**
     * Creates an named function.
     */
    public PageVerifyFunction(final String name)
    {
      super(name);
    }

    /**
     * Receives notification that a page has ended.
     *
     * @param event  the event.
     */
    public void pageFinished(final ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }

      final Integer jupage = (Integer) event.getDataRow().get("JUnit-Page");
      if (event.getState().getCurrentPage() != jupage.intValue())
      {
        throw new IllegalStateException("JUnit-Page: " + jupage +
            " EventState: " + event.getState().getCurrentPage());
      }
    }

    /**
     * Receives notification that a page has started.
     *
     * @param event  the event.
     */
    public void pageStarted(final ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }

      final Integer jupage = (Integer) event.getDataRow().get("JUnit-Page");
      if (event.getState().getCurrentPage() != jupage.intValue())
      {
        throw new IllegalStateException("JUnit-Page: " + jupage +
            " EventState: " + event.getState().getCurrentPage());
      }
    }

    /**
     * Receives notification that a page was canceled by the ReportProcessor.
     * This method is called, when a page was removed from the report after
     * it was generated.
     *
     * @param event The event.
     */
    public void pageCanceled(ReportEvent event)
    {
    }

    /**
     * This event is fired, whenever an automatic pagebreak has been detected and the report
     * state had been reverted to the previous state.
     *
     * @param event
     */
    public void pageRolledBack (ReportEvent event)
    {
    }

    /**
     * Receives notification that report generation initializes the current run.
     * <P>
     * The event carries a ReportState.Started state.  Use this to initialize the report.
     *
     * @param event The event.
     */
    public void reportInitialized(final ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }

      final Integer jupage = (Integer) event.getDataRow().get("JUnit-Page");
      if (event.getState().getCurrentPage() != jupage.intValue())
      {
        throw new IllegalStateException("JUnit-Page: " + jupage +
            " EventState: " + event.getState().getCurrentPage());
      }
    }

    /**
     * Receives notification that the report has started.
     *
     * @param event  the event.
     */
    public void reportStarted(final ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }
      final Integer jupage = (Integer) event.getDataRow().get("JUnit-Page");
      if (event.getState().getCurrentPage() != jupage.intValue())
      {
        throw new IllegalStateException("JUnit-Page: " + jupage +
            " EventState: " + event.getState().getCurrentPage());
      }
    }

    /**
     * Receives notification that the report has finished.
     *
     * @param event  the event.
     */
    public void reportFinished(final ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }

      final Integer jupage = (Integer) event.getDataRow().get("JUnit-Page");
      if (event.getState().getCurrentPage() != jupage.intValue())
      {
        throw new IllegalStateException("JUnit-Page: " + jupage +
            " EventState: " + event.getState().getCurrentPage());
      }
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

      final Integer jupage = (Integer) event.getDataRow().get("JUnit-Page");
      if (event.getState().getCurrentPage() != jupage.intValue())
      {
        throw new IllegalStateException("JUnit-Page: " + jupage +
            " EventState: " + event.getState().getCurrentPage());
      }
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

      final Integer jupage = (Integer) event.getDataRow().get("JUnit-Page");
      if (event.getState().getCurrentPage() != jupage.intValue())
      {
        throw new IllegalStateException("JUnit-Page: " + jupage +
            " EventState: " + event.getState().getCurrentPage());
      }
    }

    /**
     * Receives notification that a row of data is being processed.
     *
     * @param event  the event.
     */
    public void itemsAdvanced(final ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }

      final Integer jupage = (Integer) event.getDataRow().get("JUnit-Page");
      if (event.getState().getCurrentPage() != jupage.intValue())
      {
        throw new IllegalStateException("JUnit-Page: " + jupage +
            " EventState: " + event.getState().getCurrentPage());
      }
    }

    /**
     * Receives notification that a group of item bands is about to be processed.
     * <P>
     * The next events will be itemsAdvanced events until the itemsFinished event is raised.
     *
     * @param event The event.
     */
    public void itemsStarted(final ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }

      final Integer jupage = (Integer) event.getDataRow().get("JUnit-Page");
      if (event.getState().getCurrentPage() != jupage.intValue())
      {
        throw new IllegalStateException("JUnit-Page: " + jupage +
            " EventState: " + event.getState().getCurrentPage());
      }
    }

    /**
     * Receives notification that a group of item bands has been completed.
     * <P>
     * The itemBand is finished, the report starts to close open groups.
     *
     * @param event The event.
     */
    public void itemsFinished(final ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }

      final Integer jupage = (Integer) event.getDataRow().get("JUnit-Page");
      if (event.getState().getCurrentPage() != jupage.intValue())
      {
        throw new IllegalStateException("JUnit-Page: " + jupage +
            " EventState: " + event.getState().getCurrentPage());
      }
    }

    /**
     * Receives notification that report generation has completed, the report footer was printed,
     * no more output is done. This is a helper event to shut down the output service.
     *
     * @param event The event.
     */
    public void reportDone(final ReportEvent event)
    {
      if (event.getLevel() >= 0)
      {
        return;
      }

      final Integer jupage = (Integer) event.getDataRow().get("JUnit-Page");
      if (event.getState().getCurrentPage() != jupage.intValue())
      {
        throw new IllegalStateException("JUnit-Page: " + jupage +
            " EventState: " + event.getState().getCurrentPage());
      }
    }

    /**
     * Return the current expression value.
     * <P>
     * The value depends (obviously) on the expression implementation.
     *
     * @return the value of the function.
     */
    public Object getValue()
    {
      return null;
    }
  }

  public EventOrderTest()
  {
  }

  public EventOrderTest(final String s)
  {
    super(s);
  }

  private JFreeReport getReport() throws Exception
  {
    final JFreeReport report = new JFreeReport();
    report.getReportHeader().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getReportFooter().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getPageHeader().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getPageFooter().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getItemBand().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getGroup(0).getHeader().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getGroup(0).getFooter().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.addExpression(new EventOrderFunction("event-order"));
    report.addExpression(new EventMonitorFunction("event-monitor"));
    return report;
  }

  public void testEventOrder() throws Exception
  {
    final JFreeReport report = getReport();
    final DefaultTableModel model = new DefaultTableModel(2, 1);
    model.setValueAt("0-0", 0, 0);
    model.setValueAt("0-1", 1, 0);
    report.setData(model);

    Log.debug("   GRAPHICS2D ..");
    assertTrue(FunctionalityTestLib.execGraphics2D(report));
    Log.debug("   PDF ..");
    assertTrue(FunctionalityTestLib.createPDF(report));
    Log.debug("   CSV ..");
    FunctionalityTestLib.createCSV(report);
    Log.debug("   PLAIN_TEXT ..");
    assertTrue(FunctionalityTestLib.createPlainText(report));
    Log.debug("   RTF ..");
    FunctionalityTestLib.createRTF(report);
    Log.debug("   STREAM_HTML ..");
    FunctionalityTestLib.createStreamHTML(report);
    Log.debug("   EXCEL ..");
    FunctionalityTestLib.createXLS(report);
    Log.debug("   ZIP_HTML ..");
    FunctionalityTestLib.createZIPHTML(report);
  }

  public void testPageCount() throws Exception
  {
    GroupsDemo demo = new GroupsDemo();
    JFreeReport report = demo.createReport();
    final PageFunction pf = new PageFunction("JUnit-Page");
    pf.setDependencyLevel(2);
    report.addExpression(pf);
    report.addExpression(new PageVerifyFunction("pf-verify"));

    assertTrue(FunctionalityTestLib.execGraphics2D(report));
  }
}
