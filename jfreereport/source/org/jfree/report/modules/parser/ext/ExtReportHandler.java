/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * $Id: ExtReportHandler.java,v 1.3 2003/07/18 17:56:38 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.IncludeParser;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.base.ReportRootHandler;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
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
public class ExtReportHandler implements ElementDefinitionHandler, ReportRootHandler
{
  /** The report definition tag name.*/
  public static final String REPORT_DEFINITION_TAG = ExtParserModuleInit.REPORT_DEFINITION_TAG;

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
  private ReportParser parser;

  /** The finish tag. */
  private String finishTag;

  private boolean updateReportName;

  public static final String EXT_PARSER_TYPE_HINT_VALUE = "org.jfree.report.modules.parser.ext";

  /**
   * Instantiates the handler. The handler must be initialized properly before
   * it can be used.
   */
  public ExtReportHandler ()
  {
  }

  /**
   * Initializes the new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public void init (final ReportParser parser, final String finishTag) throws SAXException
  {
    this.parser = parser;
    this.finishTag = finishTag;

    if (parser.getConfigProperty(IncludeParser.INCLUDE_PARSING_KEY, "false").equals("true"))
    {
      if (parser.getReport() == null)
      {
        throw new SAXException("This is an include report, but no report object found.");
      }
      updateReportName = false;
    }
    else
    {
      // create the initial JFreeReport object.
      final JFreeReport report = new JFreeReport();
      getParser().setHelperObject(ReportParser.HELPER_OBJ_REPORT_NAME, report);
      report.getReportBuilderHints().putHint(report, "parser.type", EXT_PARSER_TYPE_HINT_VALUE);
      updateReportName = true;
    }
    createClassFactoryHolder();
    createStyleKeyFactoryHolder();
    createTemplateFactoryHolder();
    createDataSourceFactoryHolder();
    createElementFactoryHolder();

    final DataSourceCollector dsfc = (DataSourceCollector)
        getParser().getHelperObject(ParserConfigHandler.DATASOURCE_FACTORY_TAG);
    dsfc.configure(getParser());
    final ClassFactoryCollector cffc = (ClassFactoryCollector)
        getParser().getHelperObject(ParserConfigHandler.OBJECT_FACTORY_TAG);
    cffc.configure(getParser());
    final TemplateCollector tffc = (TemplateCollector)
        getParser().getHelperObject(ParserConfigHandler.TEMPLATE_FACTORY_TAG);
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
  public void startElement(final String tagName, final Attributes attrs)
      throws SAXException
  {
    if (tagName.equals(ExtParserModuleInit.REPORT_DEFINITION_TAG))
    {
      if (updateReportName)
      {
        parser.getReport().setName(attrs.getValue("name"));
      }
      // ignore it ...
    }
    else if (tagName.equals(PARSER_CONFIG_TAG))
    {
      // create the various factories ... order does matter ...
      getParser().pushFactory(new ParserConfigHandler(getReportParser(), tagName));
    }
    else if (tagName.equals(REPORT_CONFIG_TAG))
    {
      getParser().pushFactory(new ReportConfigHandler(getReportParser(), tagName));
    }
    else if (tagName.equals(STYLES_TAG))
    {
      getParser().pushFactory(new StylesHandler(getReportParser(), tagName));
    }
    else if (tagName.equals(TEMPLATES_TAG))
    {
      getParser().pushFactory(new TemplatesHandler(getReportParser(), tagName));
    }
    else if (tagName.equals(FUNCTIONS_TAG))
    {
      getParser().pushFactory(new FunctionsHandler(getReportParser(), tagName));
    }
    else if (tagName.equals(DATA_DEFINITION_TAG))
    {
      getParser().pushFactory(new DataDefinitionHandler(getReportParser(), tagName));
    }
    else if (tagName.equals(REPORT_DESCRIPTION_TAG))
    {
      getParser().pushFactory(new ReportDescriptionHandler(getReportParser(), tagName));
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
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
  private void createClassFactoryHolder()
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
  private void createStyleKeyFactoryHolder()
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
  private void createTemplateFactoryHolder()
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
  private void createDataSourceFactoryHolder()
  {
    DataSourceCollector fc = (DataSourceCollector) getParser().getHelperObject(
        ParserConfigHandler.DATASOURCE_FACTORY_TAG);
    if (fc == null)
    {
      fc = new DataSourceCollector();
      getParser().setHelperObject(ParserConfigHandler.DATASOURCE_FACTORY_TAG, fc);

      final ClassFactoryCollector cfc = (ClassFactoryCollector) getParser().getHelperObject(
          ParserConfigHandler.OBJECT_FACTORY_TAG);
      cfc.addFactory(fc);
    }
  }

  /**
   * Creates an element factory collector.
   */
  private void createElementFactoryHolder()
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
  public void characters(final char[] ch, final int start, final int length)
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
  public void endElement(final String tagName) throws SAXException
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
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
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

  /**
   * Returns the parser as ReportParser reference.
   *
   * @return The parser.
   */
  private ReportParser getReportParser()
  {
    return parser;
  }
}
