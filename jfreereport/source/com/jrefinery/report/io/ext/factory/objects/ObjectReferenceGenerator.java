/**
 * Date: Feb 12, 2003
 * Time: 3:43:21 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.io.ext.factory.datasource.DefaultDataSourceFactory;
import com.jrefinery.report.io.ext.factory.templates.TemplateClassFactory;
import com.jrefinery.report.targets.support.ReportProcessorUtil;

import java.net.URL;

public class ObjectReferenceGenerator
{
  private static final String REFERENCE_REPORT =
    "/com/jrefinery/report/io/ext/factory/objects/ObjectReferenceReport.xml";

  public static void main (String [] args)
  {
    ClassFactoryCollector cc = new ClassFactoryCollector();
    cc.addFactory(new DefaultClassFactory());
    cc.addFactory(new DefaultDataSourceFactory());
    cc.addFactory(new TemplateClassFactory());

    ObjectReferenceTableModel model = new ObjectReferenceTableModel(cc);

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
      ReportProcessorUtil.createStreamHTML(report, System.getProperty("user.home") + "/object-reference.html");
      ReportProcessorUtil.createPDF(report, System.getProperty("user.home") + "/object-reference.pdf");
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
