/**
 * Date: Feb 20, 2003
 * Time: 9:00:04 PM
 *
 * $Id: HiddenLayoutTest.java,v 1.3 2003/06/10 18:17:24 taqua Exp $
 */
package com.jrefinery.report.ext.junit;

import java.net.URL;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.demo.SampleData2;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManagerUtil;
import com.jrefinery.report.targets.base.layout.DefaultLayoutSupport;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.Log;

public class HiddenLayoutTest
{
  /**
   * Displays a print preview screen for the sample report.
   */
  protected JFreeReport previewReport2()
  {
    JFreeReport report = null;
    final URL in = getClass().getResource("/com/jrefinery/report/demo/report3.xml");
    report = parseReport(in);
    report.setData(new SampleData2());

    return report;
  }

  /**
   * Reads the report from the first.xml report template.
   *
   * @param templateURL The template location.
   *
   * @return A report.
   */
  private JFreeReport parseReport(final URL templateURL)
  {

    JFreeReport result = null;
    final ReportGenerator generator = ReportGenerator.getInstance();
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


  public static void main(final String[] args)
      throws Throwable
  {
    try
    {
      final HiddenLayoutTest t = new HiddenLayoutTest();
      final JFreeReport report = t.previewReport2();

      Log.debug("report.pageHeader " + report.getPageHeader().getElementCount());
      Log.debug("report.reportHeader " + report.getReportHeader().getElementCount());

      final Group g = report.getGroup(1);
      BandLayoutManagerUtil.doLayout(g.getHeader(), new DefaultLayoutSupport(), 450, 500);
      Log.debug("---------------------------------------------");
      g.getHeader().setVisible(false);
      BandLayoutManagerUtil.doLayout(g.getHeader(), new DefaultLayoutSupport(), 450, 500);
      Log.debug("---------------------------------------------");
      g.getHeader().setVisible(true);
      BandLayoutManagerUtil.doLayout(g.getHeader(), new DefaultLayoutSupport(), 450, 500);
      Log.debug("---------------------------------------------");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    System.exit(0);
  }

}
