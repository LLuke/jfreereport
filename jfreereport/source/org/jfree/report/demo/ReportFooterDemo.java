/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * ReportFooterDemo.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ReportFooterDemo.java,v 1.3 2005/03/03 22:59:58 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.demo;

import java.io.IOException;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.xml.ElementDefinitionException;

public class ReportFooterDemo extends SimpleDemoFrame
{
  public ReportFooterDemo ()
  {
    init();
  }

  protected JFreeReport createReport ()
          throws ElementDefinitionException, IOException
  {
    return loadReport("org/jfree/report/demo/footer-demo1.xml");
  }

  protected TableModel getData ()
  {
    return new SampleData2();
  }

  protected String getResourcePrefix ()
  {
    return "demo.report-footer.simple";
  }

  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();
    final ReportFooterDemo frame = new ReportFooterDemo();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
