/**
 * Date: Feb 20, 2003
 * Time: 9:00:04 PM
 *
 * $Id: HiddenLayoutTest.java,v 1.2 2003/07/23 16:06:24 taqua Exp $
 */
package org.jfree.report.ext.junit;

import java.net.URL;

import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.SampleData2;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.modules.gui.base.components.ExceptionDialog;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;
import org.jfree.util.ObjectUtilities;

public class HiddenLayoutTest
{
  /**
   * Displays a print preview screen for the sample report.
   */
  protected JFreeReport previewReport2()
  {
    JFreeReport report = null;
    final URL in = ObjectUtilities.getResource
            ("/org/jfree/report/demo/report3.xml", HiddenLayoutTest.class);
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
