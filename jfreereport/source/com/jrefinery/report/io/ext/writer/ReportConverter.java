/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * --------------------
 * ReportConverter.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportConverter.java,v 1.11 2003/05/02 12:40:16 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.io.ext.factory.datasource.DefaultDataSourceFactory;
import com.jrefinery.report.io.ext.factory.elements.DefaultElementFactory;
import com.jrefinery.report.io.ext.factory.objects.DefaultClassFactory;
import com.jrefinery.report.io.ext.factory.objects.BandLayoutClassFactory;
import com.jrefinery.report.io.ext.factory.stylekey.DefaultStyleKeyFactory;
import com.jrefinery.report.io.ext.factory.stylekey.PageableLayoutStyleKeyFactory;
import com.jrefinery.report.io.ext.factory.templates.DefaultTemplateCollection;
import com.jrefinery.report.util.Log;
import org.jfree.xml.factory.objects.URLClassFactory;

/**
 * A utility class for converting XML report definitions from the old format to the 
 * new format.
 * 
 * @author Thomas Morgner
 */
public class ReportConverter
{
  /**
   * Default constructor.
   */
  public ReportConverter()
  {
  }

  /**
   * Writes a report in the new XML format.
   * 
   * @param report  the report.
   * @param w  a character stream writer.
   * 
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there were problems while serializing
   * the report definition.
   */
  public void write (JFreeReport report, Writer w, URL contentBase)
    throws IOException, ReportWriterException
  {
    if (contentBase == null)
    {
      throw new NullPointerException("ContentBase is null");
    }
    ReportWriter writer = new ReportWriter(report);
    writer.addClassFactoryFactory(new URLClassFactory ());
    writer.addClassFactoryFactory(new DefaultClassFactory());
    writer.addClassFactoryFactory(new BandLayoutClassFactory());
    writer.addStyleKeyFactory(new DefaultStyleKeyFactory());
    writer.addStyleKeyFactory(new PageableLayoutStyleKeyFactory());
    writer.addTemplateCollection(new DefaultTemplateCollection());
    writer.addElementFactory(new DefaultElementFactory());
    writer.addDataSourceFactory(new DefaultDataSourceFactory());
    writer.write(w);
  }

  /**
   * Returns the URL of a report.
   * 
   * @param name  the report name.
   * 
   * @return The URL (or <code>null</code>).
   * 
   * @throws IOException if there is an I/O problem.
   */
  public URL findReport (String name)
    throws IOException
  {
    URL in  = getClass().getResource(name);
    if (in != null)
    {
      return in;
    }
    File f = new File (name);
    if (f.canRead())
    {
      return f.toURL();
    }
    return null;
  }

  /**
   * Parses a report from the specified template file.
   *
   * @param templateURL  the template location.
   *
   * @return The report.
   * 
   * @throws IOException if there is an I/O problem.
   */
  private JFreeReport parseReport(URL templateURL)
    throws IOException
  {
    try
    {
      ReportGenerator generator = ReportGenerator.getInstance();
      return generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      Log.info ("ParseReport failed; Cause: ", e);
      throw new IOException("Failed to parse the report");
    }
  }

  /**
   * Parses a report from the old version of the XML report format, and writes a file
   * in the new XML report format.
   * 
   * @param inName  the input report file.
   * @param outFile  the output report file.
   * 
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void convertReport (String inName, String outFile)
    throws IOException, ReportWriterException
  {
    URL reportURL = findReport(inName);
    if (reportURL == null)
    {
      throw new IOException("The specified report definition was not found");
    }
    JFreeReport report = parseReport(reportURL);
    Writer w = new FileWriter (outFile);
    write(report, w, new File(outFile).toURL());
    w.flush();
    w.close();

    parseReport(new File(inName).toURL());
  }

  /**
   * The starting point for the conversion utility.  The utility accepts two command line
   * arguments, the first is the name of the input file (a report in the old format) and
   * the second is the name of the output file (a report in the new format will be written to
   * this file).
   * 
   * @param args  command line arguments.
   * 
   * @throws Exception if there is any problem.
   */
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
