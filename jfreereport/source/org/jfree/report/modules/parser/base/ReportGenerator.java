/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportGenerator.java,v 1.10 2005/02/19 13:30:03 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 12-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package org.jfree.report.modules.parser.base;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.report.JFreeReport;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.FrontendDefaultHandler;
import org.jfree.xml.ParserFrontend;
import org.jfree.xml.parser.RootXmlReadHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * The reportgenerator initializes the parser and provides an interface the the default
 * parser.
 * <p/>
 * To create a report from an URL, use <code> ReportGenerator.getInstance().parseReport
 * (URL myURl, URL contentBase); </code>
 *
 * @author Thomas Morgner
 */
public class ReportGenerator extends ParserFrontend
{
  /**
   * Enable DTD validation of the parsed XML.
   */
  public static final String PARSER_VALIDATE
          = "org.jfree.report.modules.parser.base.Validate";

  /**
   * disable DTD validation by default.
   */
  public static final String PARSER_VALIDATE_DEFAULT = "true";

  /**
   * The report generator.
   */
  private static ReportGenerator generator;

  private HashMap helperObjects;

  /**
   * Creates a new report generator. The generator uses the singleton pattern by default,
   * so use generator.getInstance() to get the generator.
   */
  protected ReportGenerator ()
  {
    super(new ReportParser());
    initFromSystem();
    helperObjects = new HashMap();
  }

  /**
   * Tries to initilialize the generator by reading the system configuration. This will
   * enable the validation feature depending on the global configuration.
   */
  public void initFromSystem ()
  {
    setEntityResolver(ParserEntityResolver.getDefaultResolver());
  }

  /**
   * Set to false, to globaly disable the xml-validation.
   *
   * @param validate true, if the parser should validate the xml files.
   */
  public void setValidateDTD (final boolean validate)
  {
    ReportConfiguration.getGlobalConfig().setConfigProperty
            (PARSER_VALIDATE, String.valueOf(validate));
  }

  /**
   * returns true, if the parser should validate the xml files against the DTD supplied
   * with JFreeReport.
   *
   * @return true, if the parser should validate, false otherwise.
   */
  public boolean isValidateDTD ()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
            (PARSER_VALIDATE, PARSER_VALIDATE_DEFAULT).equalsIgnoreCase("true");
  }


  /**
   * Parses a report using the given parameter as filename and the directory containing
   * the file as content base.
   *
   * @param file the file name.
   * @return the report.
   *
   * @throws java.io.IOException if an I/O error occurs.
   * @throws org.jfree.xml.ElementDefinitionException
   *                             if there is a problem parsing the report template.
   */
  public JFreeReport parseReport (final String file)
          throws IOException, ElementDefinitionException
  {
    if (file == null)
    {
      throw new NullPointerException("File may not be null");
    }

    return parseReport(new File(file));
  }

  /**
   * Parses an XML file which is loaded using the given URL. All needed relative file- and
   * resourcespecification are loaded using the URL <code>file</code> as base.
   *
   * @param file the URL for the report template file.
   * @return the report.
   *
   * @throws java.io.IOException if an I/O error occurs.
   * @throws org.jfree.xml.ElementDefinitionException
   *                             if there is a problem parsing the report template.
   */
  public JFreeReport parseReport (final URL file)
          throws ElementDefinitionException, IOException
  {
    return parseReport(file, file);
  }

  /**
   * Parses an XML file which is loaded using the given URL. All needed relative file- and
   * resourcespecification are loaded using the URL <code>contentBase</code> as base.
   * <p/>
   * After the report is generated, the ReportDefinition-source and the contentbase are
   * stored as string in the reportproperties.
   *
   * @param file        the URL for the report template file.
   * @param contentBase the URL for the report template content base.
   * @return the parsed report.
   *
   * @throws java.io.IOException if an I/O error occurs.
   * @throws org.jfree.xml.ElementDefinitionException
   *                             if there is a problem parsing the report template.
   */
  public JFreeReport parseReport (final URL file, final URL contentBase)
          throws ElementDefinitionException, IOException
  {
    final JFreeReport report = (JFreeReport) parse(file, contentBase);
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
   * Parses an XML file which is loaded using the given file. All needed relative file-
   * and resourcespecification are loaded using the parent directory of the file
   * <code>file</code> as base.
   *
   * @param file the report template file.
   * @return the parsed report.
   *
   * @throws java.io.IOException if an I/O error occurs.
   * @throws org.jfree.xml.ElementDefinitionException
   *                             if there is a problem parsing the report template.
   */
  public JFreeReport parseReport (final File file)
          throws IOException, ElementDefinitionException
  {
    if (file == null)
    {
      throw new NullPointerException();
    }

    final File contentBase = file.getCanonicalFile().getParentFile();
    return parseReport(file.toURL(), contentBase.toURL());
  }

  /**
   * Parses an XML report template file.
   *
   * @param input       the input source.
   * @param contentBase the content base.
   * @return the report.
   *
   * @throws org.jfree.xml.ElementDefinitionException
   *          if an error occurred.
   */
  public JFreeReport parseReport (final InputSource input, final URL contentBase)
          throws ElementDefinitionException
  {
    return (JFreeReport) super.parse(input, contentBase);
  }

  /**
   * Returns a single shared instance of the <code>ReportGenerator</code>. This instance
   * cannot add helper objects to configure the report parser.
   *
   * @return The shared report generator.
   */
  public synchronized static ReportGenerator getInstance ()
  {
    if (generator == null)
    {
      generator = new ReportGenerator();
    }
    return generator;
  }

  /**
   * Returns a private (non-shared) instance of the <code>ReportGenerator</code>. Use this
   * instance when defining helper objects.
   *
   * @return The shared report generator.
   */
  public static ReportGenerator createInstance ()
  {
    return new ReportGenerator();
  }

  public void setObject (final String key, final Object value)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    if (value == null)
    {
      helperObjects.remove(key);
    }
    else
    {
      helperObjects.put(key, value);
    }
  }

  public Object getObject (final String key)
  {
    return helperObjects.get(key);
  }

  /**
   * Configures the xml reader. Use this to set features or properties before the
   * documents get parsed.
   *
   * @param handler the parser implementation that will handle the SAX-Callbacks.
   * @param reader  the xml reader that should be configured.
   */
  protected void configureReader (final XMLReader reader,
                                  final FrontendDefaultHandler handler)
  {
    super.configureReader(reader, handler);
    if (handler instanceof RootXmlReadHandler)
    {
      final RootXmlReadHandler readHandler = (RootXmlReadHandler) handler;
      final Iterator helperObjectsIterator = helperObjects.entrySet().iterator();
      while (helperObjectsIterator.hasNext())
      {
        final Map.Entry entry = (Map.Entry) helperObjectsIterator.next();
        readHandler.setHelperObject((String) entry.getKey(), entry.getValue());
      }
    }
  }
}
