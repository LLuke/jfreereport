/**
 * Date: Jan 22, 2003
 * Time: 6:42:09 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.io.ext.factory.objects.URLClassFactory;
import com.jrefinery.report.io.ext.factory.objects.DefaultClassFactory;
import com.jrefinery.report.io.ext.factory.stylekey.DefaultStyleKeyFactory;
import com.jrefinery.report.io.ext.factory.stylekey.PageableLayoutStyleKeyFactory;
import com.jrefinery.report.io.ext.factory.templates.DefaultTemplateCollection;
import com.jrefinery.report.io.ext.factory.elements.DefaultElementFactory;
import com.jrefinery.report.io.ext.factory.datasource.DefaultDataSourceFactory;
import com.jrefinery.report.io.ReportGenerator;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.net.URL;

public class ReportConverter
{
  public ReportConverter()
  {
  }

  public void write (JFreeReport report, Writer w)
    throws IOException, ReportWriterException
  {
    ReportWriter writer = new ReportWriter(report);
    writer.addClassFactoryFactory(new URLClassFactory (new File(".").toURL()));
    writer.addClassFactoryFactory(new DefaultClassFactory());
    writer.addStyleKeyFactory(new DefaultStyleKeyFactory());
    writer.addStyleKeyFactory(new PageableLayoutStyleKeyFactory());
    writer.addTemplateCollection(new DefaultTemplateCollection());
    writer.addElementFactory(new DefaultElementFactory());
    writer.addDataSourceFactory(new DefaultDataSourceFactory());

    Log.debug ("Now Writing:");
    writer.write(w);
  }

  public URL findReport (String name)
    throws IOException
  {
    URL in  = getClass().getResource(name);
    if (in != null)
      return in;

    File f = new File (name);
    if (f.canRead())
    {
      return f.toURL();
    }
    return null;
  }


  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL  the template location.
   *
   * @return a report.
   */
  private JFreeReport parseReport(URL templateURL)
    throws IOException
  {
    JFreeReport result = null;
    ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
        result = generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      throw new IOException("Failed to parse the report");
    }
    return result;
  }

  public void convertReport (String inName, String outFile)
    throws IOException, ReportWriterException
  {
    URL reportURL = findReport(inName);
    if (reportURL == null)
      throw new IOException("The specified report definition was not found");

    JFreeReport report = parseReport(reportURL);
    write(report, new OutputStreamWriter (System.out));
  }

  public static void main (String [] args)
    throws Exception
  {
    String reportName = "/com/jrefinery/report/demo/report1.xml";
    ReportConverter converter = new ReportConverter();
    converter.convertReport(reportName, "");
  }
}
