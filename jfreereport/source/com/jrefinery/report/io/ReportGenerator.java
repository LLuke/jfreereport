/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -----------------------
 * ReportFactory.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 10-May-2002 : Initial version
 */
package com.jrefinery.report.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.jrefinery.io.FileUtilities;
import com.jrefinery.report.JFreeReport;

/**
 * The reportgenerator initializes the parser and provides an interface
 * the the default parser.
 *
 * To create a report from an URL, use
 *
 * <code>
 * ReportGenerator.getInstance().parseReport (URL myURl, URL contentBase);
 * </code>
 */
public class ReportGenerator
{
  private static String defaultDtd;
  private static ReportGenerator generator;
  private SAXParserFactory factory;
  private String dtd;

  protected ReportGenerator()
  {
    dtd = defaultDtd;
  }

  /**
   * Defines a DTD used to validate the report definition. Your XMLParser
   * must be an validating parse for this feature to work.
   */
  public void setDTDLocation(String dtd)
  {
    this.dtd = dtd;
  }

  public String getDTDLocation()
  {
    return dtd;
  }

  public void initFromSystem()
  {
    String reportDtd = System.getProperty("com.jrefinery.report.dtd");
    if (reportDtd == null)
      return;

    File f = new File(reportDtd);
    if (f.exists() && f.isFile() && f.canRead())
    {
      defaultDtd = reportDtd;
    }
  }

  public JFreeReport parseReport(String file) throws IOException, ReportDefinitionException
  {
    if (file == null)
      throw new NullPointerException("File may not be null");

    return parseReport(new File(file));
  }

  /**
   * Parses an XML file which is loaded using the given URL. All
   * needed relative file- and resourcespecification are loaded
   * using the URL <code>contentBase</code> as base.
   */
  public JFreeReport parseReport(URL file, URL contentBase)
    throws ReportDefinitionException, IOException
  {
    if (file == null)
      throw new NullPointerException("File may not be null");

    BufferedInputStream bin = new BufferedInputStream(file.openStream());
    InputSource in = new InputSource(bin);
    in.setSystemId(file.toString());
    JFreeReport report = parseReport(in, contentBase);
    bin.close();
    return report;
  }

  /**
   * Parses an XML file which is loaded using the given file. All
   * needed relative file- and resourcespecification are loaded
   * using the parent directory of the file <code>file</code> as base.
   */
  public JFreeReport parseReport(File file) throws IOException, ReportDefinitionException
  {
    if (file == null)
      throw new NullPointerException();

    File contentBase = file.getParentFile();
    return parseReport(file.toURL(), contentBase.toURL());
  }

  /**
   * @return an SAXParser.
   */
  protected SAXParser getParser() throws ParserConfigurationException, SAXException
  {
    if (factory == null)
    {
      factory = SAXParserFactory.newInstance();
    }
    return factory.newSAXParser();
  }

  protected AbstractReportDefinitionHandler createDefaultHandler(URL contentBase)
  {
    ReportDefinitionContentHandler handler = new ReportDefinitionContentHandler();
    handler.setContentBase(contentBase);
    return handler;
  }

  /**
   * @return an created JFreeReport.
   * @throws ReportDefinitionException if an error occured
   */
  public JFreeReport parseReport(InputSource input, URL contentBase)
    throws ReportDefinitionException
  {
    try
    {
      SAXParser parser = getParser();
      AbstractReportDefinitionHandler handler = createDefaultHandler(contentBase);
      try
      {
        parser.parse(input, handler);
        return handler.getReport();
      }
      catch (IOException e)
      {
        throw new ReportDefinitionException(e);
      }
    }
    catch (ParserConfigurationException e)
    {
      throw new ReportDefinitionException(e);
    }
    catch (SAXException e)
    {
      throw new ReportDefinitionException(e);
    }
  }

  /**
   * Returns a new ReportGenerator reference.
   */
  public static ReportGenerator getInstance()
  {
    if (generator == null)
    {
      generator = new ReportGenerator();
      generator.initFromSystem();
    }
    return generator;
  }

  public static void main(String[] args)
  {
    File file1 = FileUtilities.findFileOnClassPath("report1.xml");
    if (file1 == null)
    {
      JOptionPane.showMessageDialog(
        null,
        "ReportDefinition report1.xml not found on classpath");
      return;
    }
    ReportGenerator gen = ReportGenerator.getInstance();

    try
    {
      for (int i = 0; i < 100; i++)
        gen.parseReport(file1);
    }
    catch (Exception ioe)
    {
      JOptionPane.showMessageDialog(
        null,
        ioe.getMessage(),
        "Error: " + ioe.getClass().getName(),
        JOptionPane.ERROR_MESSAGE);
      return;
    }
  }
}