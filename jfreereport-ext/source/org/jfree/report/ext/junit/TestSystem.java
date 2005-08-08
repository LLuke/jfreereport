/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ----------------
 * TestSystem.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TestSystem.java,v 1.8 2005/05/31 18:28:00 taqua Exp $
 *
 * Changes
 * -------
 * 26.03.2003 : Initial version
 */
package org.jfree.report.ext.junit;

import java.net.URL;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.SampleData2;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.Log;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

public final class TestSystem
{
  private TestSystem()
  {
  }

  public static JFreeReport loadReport(final String urlname, final TableModel data)
  {
    final URL in = ObjectUtilities.getResource(urlname, TestSystem.class);
    if (in == null)
    {
      Log.error("xml file not found.");
      return null;
    }
    final ReportGenerator gen = ReportGenerator.getInstance();
    final JFreeReport report1;
    try
    {
      report1 = gen.parseReport(in, in);
    }
    catch (Exception ioe)
    {
      Log.error("1: report definition failure.", ioe);
      return null;
    }

    if (report1 == null)
    {
      Log.error("2: the report is null.");
      return null;
    }
    report1.setData(data);
    return report1;
  }

  public static void showPreview(final JFreeReport report1)
      throws ReportProcessingException
  {
    final PreviewDialog frame1 = new PreviewDialog(report1);
    frame1.setModal(true);
    frame1.pack();
    RefineryUtilities.positionFrameRandomly(frame1);
    frame1.setVisible(true);
  }

  public static void main(final String[] args)
      throws Exception
  {
    final SampleData2 m_dataModel = new SampleData2();
    final JFreeReport report = TestSystem.loadReport
      ("org/jfree/report/ext/junit/pagebreak.xml", m_dataModel);
    if (report == null)
    {
      System.exit(1);
    }

    TestSystem.showPreview(report);
  }
}
