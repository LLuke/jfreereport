/**
 * Date: Jan 22, 2003
 * Time: 6:42:09 PM
 *
 * $Id: ReportConverter.java,v 1.2 2003/01/23 18:07:46 taqua Exp $
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
import java.io.FileWriter;
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
      Log.debug ("Cause: ", e);
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
    Writer w = new FileWriter (outFile);
    write(report, w);
    w.flush();
    w.close();

    parseReport(new File(inName).toURL());
  }

  public static void main (String [] args)
    throws Exception
  {
//    String reportName = "/com/jrefinery/report/demo/report1.xml";
    if (args.length != 2)
    {
      System.err.println ("Usage: ReportConverter <InFile> <OutFile>");
      System.exit(1);
    }
    ReportConverter converter = new ReportConverter();
    converter.convertReport(args[0], args[1]);
  }
}
