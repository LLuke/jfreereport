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
 * ReportGenerator.java
 * --------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportGenerator.java,v 1.31 2003/05/02 12:39:53 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 12-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package com.jrefinery.report.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.ReportConfiguration;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.ParserFrontend;
import org.xml.sax.InputSource;

/**
 * The reportgenerator initializes the parser and provides an interface
 * the the default parser.
 *
 * To create a report from an URL, use
 * <code>
 * ReportGenerator.getInstance().parseReport (URL myURl, URL contentBase);
 * </code>
 *
 * @author Thomas Morgner
 */
public class ReportGenerator extends ParserFrontend
{
  /** The report generator. */
  private static ReportGenerator generator;

  /**
   * Creates a new report generator. The generator uses the singleton pattern by
   * default, so use generator.getInstance() to get the generator.
   */
  protected ReportGenerator ()
  {
    super(new ReportParser());
    initFromSystem ();
  }

  /**
   * Tries to initilialize the generator by reading the system property
   * "com.jrefinery.report.dtd". This property should point to the dtd
   * used for parsing.
   */
  public void initFromSystem ()
  {
    setValidateDTD(ReportConfiguration.getGlobalConfig().isValidateXML());
    setEntityResolver(ParserEntityResolver.getDefaultResolver());
  }

  /**
   * Parses a report using the given parameter as filename and the directory
   * containing the file as content base.
   *
   * @param file  the file name.
   *
   * @return the report.
   *
   * @throws IOException if an I/O error occurs.
   * @throws ElementDefinitionException if there is a problem parsing the
   * report template.
   */
  public JFreeReport parseReport (String file)
      throws IOException, ElementDefinitionException
  {
    if (file == null)
    {
      throw new NullPointerException ("File may not be null");
    }

    return parseReport (new File (file));
  }

  /**
   * Parses an XML file which is loaded using the given URL. All
   * needed relative file- and resourcespecification are loaded
   * using the URL <code>file</code> as base.
   *
   * @param file  the URL for the report template file.
   *
   * @return the report.
   *
   * @throws IOException if an I/O error occurs.
   * @throws ElementDefinitionException if there is a problem parsing
   * the report template.
   */
  public JFreeReport parseReport (URL file)
          throws ElementDefinitionException, IOException
  {
    return parseReport (file, file);
  }

  /**
   * Parses an XML file which is loaded using the given URL. All
   * needed relative file- and resourcespecification are loaded
   * using the URL <code>contentBase</code> as base.
   * <p>
   * After the report is generated, the ReportDefinition-source and the
   * contentbase are stored as string in the reportproperties.
   *
   * @param file  the URL for the report template file.
   * @param contentBase  the URL for the report template content base.
   *
   * @return the parsed report.
   *
   * @throws IOException if an I/O error occurs.
   * @throws ElementDefinitionException if there is a problem parsing
   * the report template.
   */
  public JFreeReport parseReport (URL file, URL contentBase)
          throws ElementDefinitionException, IOException
  {
    JFreeReport report = (JFreeReport) parse (file, contentBase);
    report.setProperty(JFreeReport.REPORT_DEFINITION_SOURCE, file.toString());
    if (contentBase != null)
    {
      report.setProperty
          (JFreeReport.REPORT_DEFINITION_CONTENTBASE, contentBase.toString());
    }
    else
    {
      report.setProperty
          (JFreeReport.REPORT_DEFINITION_CONTENTBASE, file.toString());
    }
    return report;
  }

  /**
   * Parses an XML file which is loaded using the given file. All
   * needed relative file- and resourcespecification are loaded
   * using the parent directory of the file <code>file</code> as base.
   *
   * @param file  the report template file.
   *
   * @return the parsed report.
   *
   * @throws IOException if an I/O error occurs.
   * @throws ElementDefinitionException if there is a problem parsing
   * the report template.
   */
  public JFreeReport parseReport (File file)
      throws IOException, ElementDefinitionException
  {
    if (file == null)
    {
      throw new NullPointerException ();
    }

    File contentBase = file.getCanonicalFile().getParentFile ();
    return parseReport (file.toURL (), contentBase.toURL ());
  }

  /**
   * Parses an XML report template file.
   *
   * @param input  the input source.
   * @param contentBase  the content base.
   *
   * @return the report.
   *
   * @throws ElementDefinitionException if an error occurred.
   */
  public JFreeReport parseReport (InputSource input, URL contentBase)
          throws ElementDefinitionException
  {
    return (JFreeReport) super.parse(input, contentBase);
  }

  /**
   * Returns a single shared instance of the <code>ReportGenerator</code>.
   * 
   * @return The shared report generator.
   */
  public static ReportGenerator getInstance ()
  {
    if (generator == null)
    {
      generator = new ReportGenerator ();
    }
    return generator;
  }
}
