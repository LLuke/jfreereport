/**
 *
 *  Date: 17.07.2002
 *  JMIBug.java
 *  ------------------------------
 *  17.07.2002 : ...
 */
package com.jrefinery.report.ext.junit.bugs;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.demo.SampleData1;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.ui.RefineryUtilities;

import javax.swing.table.TableModel;
import java.awt.print.PageFormat;
import java.net.URL;

public class JMIBug
{
  public void preview (String urlname, TableModel data, boolean landscape)
  {
    try
    {
      URL in = getClass ().getResource (urlname);
      if (in == null)
      {
        System.out.println ("IdCurrencyPanel - preview(url, tableModel) - ERROR: xml file not found.");
        return;
      }
      ReportGenerator gen = ReportGenerator.getInstance ();
      JFreeReport report1 = null;
      try
      {
        report1 = gen.parseReport (in, in);
      }
      catch (Exception ioe)
      {
        System.out.println ("IdCurrencyPanel - preview(url, tableModel) - ERROR 1: report definition failure.");
        return;
      }

      if (report1 == null)
      {
        System.out.println ("IdCurrencyPanel - preview(url, tableModel) - ERROR 2: the report is null.");
      }
      if (landscape) report1.getDefaultPageFormat ().setOrientation (PageFormat.LANDSCAPE);
      report1.setData (data);

      PreviewFrame frame1 = new PreviewFrame (report1);
      frame1.pack ();
      RefineryUtilities.positionFrameRandomly (frame1);
      frame1.setVisible (true);
      frame1.requestFocus ();
    }
    catch (Exception e)
    {
      System.out.println ("IdCurrencyPanel - preview(url, tableModel) - ERROR 3: " + e.getMessage ());
    }
  }


  public static void main (String [] argh) throws Exception
  {
    SampleData1 m_dataModel = new SampleData1();
    for (int i = 0; i < 10; i++)
    {
      new JMIBug().preview ("/com/jrefinery/report/ext/junit/bugs/resource/countryreport.xml", m_dataModel, false);
    }
  }


}