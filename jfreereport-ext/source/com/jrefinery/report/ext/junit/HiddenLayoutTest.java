/**
 * Date: Feb 20, 2003
 * Time: 9:00:04 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.junit;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.Group;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManagerUtil;
import com.jrefinery.report.targets.base.layout.DefaultLayoutSupport;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.demo.SampleData2;

import java.net.URL;

public class HiddenLayoutTest
{
  /**
   * Displays a print preview screen for the sample report.
   */
  protected JFreeReport previewReport2()
  {
    JFreeReport report = null;
    URL in = getClass().getResource("/com/jrefinery/report/demo/report3.xml");
    report = parseReport(in);
    report.setData(new SampleData2 ());

    return report;
  }

  /**
   * Reads the report from the first.xml report template.
   *
   * @param templateURL The template location.
   *
   * @return A report.
   */
  private JFreeReport parseReport(URL templateURL)
  {

    JFreeReport result = null;
    ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      result = generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      ExceptionDialog.showExceptionDialog("Error on parsing",
                                          "Error while parsing " + templateURL, e);
    }
    return result;

  }


  public static void main (String [] args)
    throws Throwable
  {
    try
    {
      HiddenLayoutTest t = new HiddenLayoutTest();
      JFreeReport report = t.previewReport2();

      Log.debug ("report.pageHeader " + report.getPageHeader().getElements().size());
      Log.debug ("report.reportHeader " + report.getReportHeader().getElements().size());

      Group g = report.getGroup(1);
      BandLayoutManagerUtil.doLayout(g.getHeader(), new DefaultLayoutSupport(), 450, 500);
      Log.debug ("---------------------------------------------");
      g.getHeader().setVisible(false);
      BandLayoutManagerUtil.doLayout(g.getHeader(), new DefaultLayoutSupport(), 450, 500);
      Log.debug ("---------------------------------------------");
      g.getHeader().setVisible(true);
      BandLayoutManagerUtil.doLayout(g.getHeader(), new DefaultLayoutSupport(), 450, 500);
      Log.debug ("---------------------------------------------");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    System.exit(0);
  }

}
