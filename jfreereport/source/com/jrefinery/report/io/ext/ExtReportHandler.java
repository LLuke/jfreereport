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
 * ---------------------
 * ExtReportHandler.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExtReportHandler.java,v 1.12 2003/05/14 22:26:38 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import java.util.HashMap;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.InitialReportHandler;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceCollector;
import com.jrefinery.report.io.ext.factory.elements.ElementFactoryCollector;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyFactoryCollector;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollector;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
import org.jfree.xml.factory.objects.ClassFactoryCollector;
import org.jfree.xml.factory.objects.URLClassFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A handler for the extended report definition format. This is the root handler for
 * the report definition format.
 *
 * @author Thomas Morgner.
 */
public class ExtReportHandler implements ElementDefinitionHandler
{
  /** The report definition tag name.*/
  public static final String REPORT_DEFINITION_TAG = InitialReportHandler.REPORT_DEFINITION_TAG;

  /** The parser config tag name. */
  public static final String PARSER_CONFIG_TAG = "parser-config";

  /** The report config tag name. */
  public static final String REPORT_CONFIG_TAG = "report-config";

  /** The styles tag name. */
  public static final String STYLES_TAG = "styles";

  /** The templates tag name. */
  public static final String TEMPLATES_TAG = "templates";

  /** The report description tag name. */
  public static final String REPORT_DESCRIPTION_TAG = "report-description";

  /** The functions tag name. */
  public static final String FUNCTIONS_TAG = "functions";

  /** The data definition tag name. */
  public static final String DATA_DEFINITION_TAG = "data-definition";

  /** The parser. */
  private Parser parser;

  /** The finish tag. */
  private String finishTag;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public ExtReportHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;

    // create the initial JFreeReport object.
    JFreeReport report = new JFreeReport();
    getParser().setHelperObject(REPORT_DEFINITION_TAG, report);
    getParser().setHelperObject(StylesHandler.STYLES_COLLECTION, new HashMap());
    createClassFactoryHolder();
    createStyleKeyFactoryHolder();
    createTemplateFactoryHolder();
    createDataSourceFactoryHolder();
    createElementFactoryHolder();

    DataSourceCollector dsfc =
        (DataSourceCollector) getParser().getHelperObject(ParserConfigHandler.DATASOURCE_FACTORY_TAG);
    dsfc.configure(getParser());
    ClassFactoryCollector cffc =
        (ClassFactoryCollector) getParser().getHelperObject(ParserConfigHandler.OBJECT_FACTORY_TAG);
    cffc.configure(getParser());
    TemplateCollector tffc =
        (TemplateCollector) getParser().getHelperObject(ParserConfigHandler.TEMPLATE_FACTORY_TAG);
    tffc.configure(getParser());

  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(PARSER_CONFIG_TAG))
    {
      // create the various factories ... order does matter ...
      getParser().pushFactory(new ParserConfigHandler(getParser(), tagName));
    }
    else if (tagName.equals(REPORT_CONFIG_TAG))
    {
      getParser().pushFactory(new ReportConfigHandler(getParser(), tagName));
    }
    else if (tagName.equals(STYLES_TAG))
    {
      getParser().pushFactory(new StylesHandler(getParser(), tagName));
    }
    else if (tagName.equals(TEMPLATES_TAG))
    {
      getParser().pushFactory(new TemplatesHandler(getParser(), tagName));
    }
    else if (tagName.equals(FUNCTIONS_TAG))
    {
      getParser().pushFactory(new FunctionsHandler(getParser(), tagName));
    }
    else if (tagName.equals(DATA_DEFINITION_TAG))
    {
      getParser().pushFactory(new DataDefinitionHandler(getParser()));
    }
    else if (tagName.equals(REPORT_DESCRIPTION_TAG))
    {
      getParser().pushFactory(new ReportDescriptionHandler(getParser(), tagName));
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
                              + PARSER_CONFIG_TAG + ", "
                              + REPORT_DESCRIPTION_TAG + ", "
                              + REPORT_CONFIG_TAG + ", "
                              + STYLES_TAG + ", "
                              + TEMPLATES_TAG + ", "
                              + FUNCTIONS_TAG + ", "
                              + DATA_DEFINITION_TAG + ".");
    }
  }

  /**
   * Creates a class factory collector.
   */
  private void createClassFactoryHolder ()
  {
    ClassFactoryCollector fc = (ClassFactoryCollector) getParser().getHelperObject(
        ParserConfigHandler.OBJECT_FACTORY_TAG);
    if (fc == null)
    {
      fc = new ClassFactoryCollector();

      // special treatment for URLs, they need the content base information ...
      fc.addFactory(new URLClassFactory());
      getParser().setHelperObject(ParserConfigHandler.OBJECT_FACTORY_TAG, fc);
    }
  }

  /**
   * Creates a style-key factory collector.
   */
  private void createStyleKeyFactoryHolder ()
  {
    StyleKeyFactoryCollector fc = (StyleKeyFactoryCollector) getParser().getHelperObject(
        ParserConfigHandler.STYLEKEY_FACTORY_TAG);
    if (fc == null)
    {
      fc = new StyleKeyFactoryCollector();
      getParser().setHelperObject(ParserConfigHandler.STYLEKEY_FACTORY_TAG, fc);
    }
  }

  /**
   * Creates a template collector.
   */
  private void createTemplateFactoryHolder ()
  {
    TemplateCollector fc = (TemplateCollector) getParser().getHelperObject(
        ParserConfigHandler.TEMPLATE_FACTORY_TAG);
    if (fc == null)
    {
      fc = new TemplateCollector();
      getParser().setHelperObject(ParserConfigHandler.TEMPLATE_FACTORY_TAG, fc);
    }
  }

  /**
   * Creates a data source collector.
   */
  private void createDataSourceFactoryHolder ()
  {
    DataSourceCollector fc = (DataSourceCollector) getParser().getHelperObject(
        ParserConfigHandler.DATASOURCE_FACTORY_TAG);
    if (fc == null)
    {
      fc = new DataSourceCollector();
      getParser().setHelperObject(ParserConfigHandler.DATASOURCE_FACTORY_TAG, fc);

      ClassFactoryCollector cfc = (ClassFactoryCollector) getParser().getHelperObject(
          ParserConfigHandler.OBJECT_FACTORY_TAG);
      cfc.addFactory(fc);
    }
  }

  /**
   * Creates an element factory collector.
   */
  private void createElementFactoryHolder ()
  {
    ElementFactoryCollector fc = (ElementFactoryCollector) getParser().getHelperObject(
        ParserConfigHandler.ELEMENT_FACTORY_TAG);
    if (fc == null)
    {
      fc = new ElementFactoryCollector();
      getParser().setHelperObject(ParserConfigHandler.ELEMENT_FACTORY_TAG, fc);
    }
  }

  /**
   * Callback to indicate that some character data has been read.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */
  public void characters(char ch[], int start, int length)
  {
    // characters are ignored at this point...
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else if (tagName.equals(PARSER_CONFIG_TAG))
    {
      // ignore this event ... forwarded from subFactory ...
    }
    else if (tagName.equals(REPORT_CONFIG_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else if (tagName.equals(STYLES_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else if (tagName.equals(TEMPLATES_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else if (tagName.equals(FUNCTIONS_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else if (tagName.equals(DATA_DEFINITION_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else if (tagName.equals(REPORT_DESCRIPTION_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
                              + REPORT_DESCRIPTION_TAG + ", "
                              + PARSER_CONFIG_TAG + ", "
                              + REPORT_CONFIG_TAG + ", "
                              + STYLES_TAG + ", "
                              + TEMPLATES_TAG + ", "
                              + FUNCTIONS_TAG + ", "
                              + DATA_DEFINITION_TAG + ".");
    }
  }

  /**
   * Returns the parser.
   *
   * @return The parser.
   */
  public Parser getParser()
  {
    return parser;
  }
}
