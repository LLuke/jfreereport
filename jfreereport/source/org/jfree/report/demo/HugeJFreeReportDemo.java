/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------------------
 * HugeJFreeReportDemo.java
 * ------------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HugeJFreeReportDemo.java,v 1.4 2003/11/07 18:33:48 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 31-May-2002 : Derived from JFreeReportDemo
 * 04-Jun-2002 : Some documentation
 * 10-Jun-2002 : Updated code to work with latest version of the JCommon class library;
 * 29-Aug-2002 : Downport to JDK 1.2.2
 */

package org.jfree.report.demo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;

import org.jfree.report.Boot;
import org.jfree.ui.RefineryUtilities;

/**
 * The main frame in the report demonstration application. This demo has huge reports
 * added, so calling report 5 and 6 can require some patience.
 * <p>
 * If the system property "org.jfree.report.demo.DEBUG" is set to "true", debugging
 * messages to System.out stream are enabled.
 *
 * @author Thomas Morgner
 */
public class HugeJFreeReportDemo extends JFreeReportDemo
{
  /**
   * Constructs a frame containing sample reports created using the JFreeReport Class Library.
   */
  public HugeJFreeReportDemo()
  {
  }

  /**
   * Creates and returns a list of the available demos.
   *
   * @return The list.
   */
  protected List createAvailableDemos()
  {
    final ArrayList list = new ArrayList();

    list.add(new DemoDefinition(createExampleName(1),
        new SampleData1(),
        new URLDemoHandler("/org/jfree/report/demo/report1.xml")));

    list.add(new DemoDefinition(createExampleName(2),
        new SampleData2(),
        new URLDemoHandler("/org/jfree/report/demo/report2.xml")));

    list.add(new DemoDefinition(createExampleName(3),
        new SampleData3(),
        new URLDemoHandler("/org/jfree/report/demo/report3.xml")));

    list.add(new DemoDefinition(createExampleName(4),
        new SampleData4(),
        new URLDemoHandler("/org/jfree/report/demo/report4.xml")));

    list.add(new DemoDefinition(createExampleName(5) + " (HUGE)",
        new SampleData5(),
        new URLDemoHandler("/org/jfree/report/demo/report2.xml")));

    list.add(new DemoDefinition(createExampleName(6) + " (HUGE)",
        new SampleData6(),
        new URLDemoHandler("/org/jfree/report/demo/report2.xml")));

    return list;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * The starting point for the demonstration application.
   *
   * @param args ignored.
   */
  public static void main(final String[] args)
  {
    // initialize JFreeReport
    Boot.start();

    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e)
    {
      System.err.println("Look and feel problem.");
    }

    final HugeJFreeReportDemo frame = new HugeJFreeReportDemo();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }

}
