/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * --------------------
 * ReportGenerator.java
 * --------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportGenerator.java,v 1.23 2003/02/01 18:27:03 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 12-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package com.jrefinery.report.io;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

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
public class ReportGenerator
{
  /** The report generator. */
  private static ReportGenerator generator;

  /** The report handler. */
  private Parser defaulthandler;

  /** The parser factory. */
  private SAXParserFactory factory;

  /** The DTD. */
  private ParserEntityResolver entityResolver;

  private boolean validateDTD;

  /**
   * Creates a new report generator. The generator uses the singleton pattern by default,
   * so use generator.getInstance() to get the generator.
   */
  protected ReportGenerator ()
  {
    defaulthandler = new Parser();
    initFromSystem ();
  }

  public boolean isValidateDTD()
  {
    return validateDTD;
  }

  public void setValidateDTD(boolean validateDTD)
  {
    this.validateDTD = validateDTD;
  }

  public ParserEntityResolver getEntityResolver()
  {
    return entityResolver;
  }

  public void setEntityResolver(ParserEntityResolver entityResolver)
  {
    this.entityResolver = entityResolver;
  }

  /**
   * Tries to initilialize the generator by reading the system property "com.jrefinery.report.dtd".
   * This property should point to the dtd used for parsing.
   */
  public void initFromSystem ()
  {
    setValidateDTD(ReportConfiguration.getGlobalConfig().isValidateXML());
    entityResolver = ParserEntityResolver.getDefaultResolver();
  }

  /**
   * Parses a report using the given parameter as filename and the directory containing
   * the file as content base.
   *
   * @param file  the file name.
   *
   * @return the report.
   *
   * @throws java.io.IOException if an I/O error occurs.
   * @throws com.jrefinery.report.io.ReportDefinitionException if there is a problem parsing the report template.
   */
  public JFreeReport parseReport (String file) throws IOException, ReportDefinitionException
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
   * @throws java.io.IOException if an I/O error occurs.
   * @throws com.jrefinery.report.io.ReportDefinitionException if there is a problem parsing the report template.
   */
  public JFreeReport parseReport (URL file)
          throws ReportDefinitionException, IOException
  {
    return parseReport (file, file);
  }

  /**
   * Parses an XML file which is loaded using the given URL. All
   * needed relative file- and resourcespecification are loaded
   * using the URL <code>contentBase</code> as base.
   * <p>
   * After the report is generated, the ReportDefinition-source and the contentbase are
   * stored as string in the reportproperties.
   *
   * @param file  the URL for the report template file.
   * @param contentBase  the URL for the report template content base.
   *
   * @return the parsed report.
   *
   * @throws java.io.IOException if an I/O error occurs.
   * @throws com.jrefinery.report.io.ReportDefinitionException if there is a problem parsing the report template.
   */
  public JFreeReport parseReport (URL file, URL contentBase)
          throws ReportDefinitionException, IOException
  {
    if (file == null)
    {
      throw new NullPointerException ("File may not be null");
    }

    BufferedInputStream bin = new BufferedInputStream (file.openStream ());
    InputSource in = new InputSource (bin);
    in.setSystemId (file.toString ());
    JFreeReport report = parseReport (in, contentBase);
    report.setProperty(JFreeReport.REPORT_DEFINITION_SOURCE, file.toString());
    if (contentBase != null)
    {
      report.setProperty(JFreeReport.REPORT_DEFINITION_CONTENTBASE, contentBase.toString());
    }
    else
    {
      report.setProperty(JFreeReport.REPORT_DEFINITION_CONTENTBASE, file.toString());
    }
    bin.close ();
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
   * @throws java.io.IOException if an I/O error occurs.
   * @throws com.jrefinery.report.io.ReportDefinitionException if there is a problem parsing the report template.
   */
  public JFreeReport parseReport (File file) throws IOException, ReportDefinitionException
  {
    if (file == null)
    {
      throw new NullPointerException ();
    }

    File contentBase = file.getCanonicalFile().getParentFile ();
    return parseReport (file.toURL (), contentBase.toURL ());
  }

  /**
   * Returns a SAX parser.
   *
   * @return a SAXParser.
   *
   * @throws javax.xml.parsers.ParserConfigurationException if there is a problem configuring the parser.
   * @throws org.xml.sax.SAXException if there is a problem with the parser initialisation
   */
  protected SAXParser getParser () throws ParserConfigurationException, SAXException
  {
    if (factory == null)
    {
      factory = SAXParserFactory.newInstance ();
    }
    return factory.newSAXParser ();
  }

  /**
   * Sets the default handler used for parsing reports. This handler is used to
   * initiate parsing.
   *
   * @param handler  the handler.
   */
  public void setDefaultHandler (Parser handler)
  {
    if (handler == null)
    {
      throw new NullPointerException ();
    }
    this.defaulthandler = handler;
  }

  /**
   * Returns the ReportDefinitionHandler used for parsing reports.
   *
   * @return the report handler.
   */
  public Parser getDefaultHandler ()
  {
    return defaulthandler;
  }

  /**
   * Creates a new instance of the currently set default handler and sets the contentbase
   * for the handler to <code>contentBase</code>
   *
   * @param contentBase  the content base.
   *
   * @return the report handler.
   */
  protected Parser createDefaultHandler (URL contentBase)
  {
    Parser handler = getDefaultHandler ().getInstance ();
    handler.setConfigurationValue (Parser.CONTENTBASE_KEY, contentBase);
    Log.debug ("ContentBase: " + handler.getConfigurationValue (Parser.CONTENTBASE_KEY));
    return handler;
  }

  /**
   * Parses an XML report template file.
   *
   * @param input  the input source.
   * @param contentBase  the content base.
   *
   * @return the report.
   *
   * @throws com.jrefinery.report.io.ReportDefinitionException if an error occurred.
   */
  public JFreeReport parseReport (InputSource input, URL contentBase)
          throws ReportDefinitionException
  {
    try
    {
      SAXParser parser = getParser ();
      XMLReader reader = parser.getXMLReader();

      reader.setFeature("http://xml.org/sax/features/validation", isValidateDTD());
      reader.setEntityResolver(getEntityResolver());

      Log.debug ("IS Validate: " + isValidateDTD());

      Parser handler = createDefaultHandler (contentBase);
      try
      {
        Log.debug ("ContentBase: " + handler.getConfigurationValue (Parser.CONTENTBASE_KEY));
        reader.setContentHandler(handler);
        reader.setDTDHandler(handler);
        reader.setEntityResolver(getEntityResolver());
        reader.setErrorHandler(handler);
        reader.parse (input);
        return handler.getReport ();
      }
      catch (IOException e)
      {
        throw new ReportDefinitionException (e);
      }
    }
    catch (ParserConfigurationException e)
    {
      throw new ReportDefinitionException (e);
    }
    catch (SAXException e)
    {
      throw new ReportDefinitionException (e);
    }
  }

  /**
   * Returns a new ReportGenerator reference.
   *
   * @return the report generator.
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
