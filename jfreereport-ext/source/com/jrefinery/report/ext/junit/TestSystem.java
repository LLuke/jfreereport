/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TestSystem.java,v 1.4 2003/05/14 22:36:46 taqua Exp $
 *
 * Changes
 * -------
 * 26.03.2003 : Initial version
 */
package com.jrefinery.report.ext.junit;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.table.TableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.demo.SampleData2;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.report.util.Log;
import org.jfree.ui.RefineryUtilities;

public class TestSystem
{

  public static JFreeReport loadReport(String urlname, TableModel data)
  {
    URL in = new String().getClass().getResource(urlname);
    if (in == null)
    {
      Log.error("xml file not found.");
      return null;
    }
    ReportGenerator gen = ReportGenerator.getInstance();
    JFreeReport report1 = null;
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

  public static void showPreviewFrame(JFreeReport report1)
      throws ReportProcessingException
  {
    showPreviewFrameWExit(report1, false);
  }

  public static void showPreviewFrameWExit(JFreeReport report1, boolean close)
      throws ReportProcessingException
  {
    PreviewFrame frame1 = new PreviewFrame(report1);
    frame1.pack();
    RefineryUtilities.positionFrameRandomly(frame1);
    if (close)
    {
      frame1.addWindowListener(new WindowAdapter()
      {
        /**
         * Invoked when a window is in the process of being closed.
         * The close operation can be overridden at this point.
         */
        public void windowClosing(WindowEvent e)
        {
          System.exit(0);
        }
      });
    }
    frame1.setVisible(true);
    frame1.requestFocus();
  }

  public static void main(String[] args)
      throws Exception
  {
    SampleData2 m_dataModel = new SampleData2();
    JFreeReport report = TestSystem.loadReport("/com/jrefinery/report/ext/junit/pagebreak.xml", m_dataModel);
    if (report == null)
      System.exit(1);

    TestSystem.showPreviewFrame(report);
  }
}
