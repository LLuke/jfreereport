/**
 *
 *  Date: 17.07.2002
 *  JMIBug.java
 *  ------------------------------
 *  17.07.2002 : ...
 */
package com.jrefinery.report.ext.junit.bugs;

import java.awt.print.PageFormat;
import java.net.URL;
import javax.swing.table.TableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.demo.SampleData1;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import org.jfree.ui.RefineryUtilities;

public class JMIBug
{
  public void preview(final String urlname, final TableModel data, final boolean landscape)
  {
    try
    {
      final URL in = getClass().getResource(urlname);
      if (in == null)
      {
        System.out.println("IdCurrencyPanel - preview(url, tableModel) - ERROR: xml file not found.");
        return;
      }
      final ReportGenerator gen = ReportGenerator.getInstance();
      JFreeReport report1 = null;
      try
      {
        report1 = gen.parseReport(in, in);
      }
      catch (Exception ioe)
      {
        System.out.println("IdCurrencyPanel - preview(url, tableModel) - ERROR 1: report definition failure.");
        return;
      }

      if (report1 == null)
      {
        System.out.println("IdCurrencyPanel - preview(url, tableModel) - ERROR 2: the report is null.");
      }
      if (landscape) report1.getDefaultPageFormat().setOrientation(PageFormat.LANDSCAPE);
      report1.setData(data);

      final PreviewFrame frame1 = new PreviewFrame(report1);
      frame1.pack();
      RefineryUtilities.positionFrameRandomly(frame1);
      frame1.setVisible(true);
      frame1.requestFocus();
    }
    catch (Exception e)
    {
      System.out.println("IdCurrencyPanel - preview(url, tableModel) - ERROR 3: " + e.getMessage());
    }
  }


  public static void main(final String[] argh) throws Exception
  {
    final SampleData1 m_dataModel = new SampleData1();
    for (int i = 0; i < 10; i++)
    {
      new JMIBug().preview("/com/jrefinery/report/ext/junit/bugs/resource/countryreport.xml", m_dataModel, false);
    }
  }


}