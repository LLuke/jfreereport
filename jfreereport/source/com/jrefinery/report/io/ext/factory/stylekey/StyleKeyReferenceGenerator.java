/**
 * Date: Feb 12, 2003
 * Time: 5:54:34 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.support.ReportProcessorUtil;

import java.net.URL;

public class StyleKeyReferenceGenerator
{
  private static final String REFERENCE_REPORT =
    "/com/jrefinery/report/io/ext/factory/stylekey/StylekeyReferenceReport.xml";

  public static void main (String [] args)
  {
    StyleKeyFactoryCollector cc = new StyleKeyFactoryCollector();
    cc.addFactory(new DefaultStyleKeyFactory());
    cc.addFactory(new PageableLayoutStyleKeyFactory());

    StyleKeyReferenceTableModel model = new StyleKeyReferenceTableModel(cc);

    ReportGenerator gen = ReportGenerator.getInstance();
    URL reportURL = gen.getClass().getResource(REFERENCE_REPORT);
    if (reportURL == null)
    {
      System.err.println ("The report was not found in the classpath");
      System.err.println ("File: " + REFERENCE_REPORT);
      System.exit(1);
      return;
    }

    JFreeReport report;
    try
    {
      report = gen.parseReport(reportURL);
    }
    catch (Exception e)
    {
      System.err.println ("The report could not be parsed.");
      System.err.println ("File: " + REFERENCE_REPORT);
      e.printStackTrace(System.err);
      System.exit(1);
      return;
    }
    report.setData(model);
    try
    {
      ReportProcessorUtil.createStreamHTML(report, System.getProperty("user.home") + "/stylekey-reference.html");
      ReportProcessorUtil.createPDF(report, System.getProperty("user.home") + "/stylekey-reference.pdf");
    }
    catch (Exception e)
    {
      System.err.println ("The report processing failed.");
      System.err.println ("File: " + REFERENCE_REPORT);
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }


}
