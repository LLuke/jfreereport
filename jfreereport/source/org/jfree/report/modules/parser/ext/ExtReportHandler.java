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
 * ---------------------
 * ExtReportHandler.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExtReportHandler.java,v 1.9 2003/08/25 14:29:32 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.base.ReportRootHandler;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
import org.jfree.xml.CommentHandler;
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

  /** A flag indicating whether to update the report name. */
  private boolean updateReportName;

  /**
   * The common hint name used to store report builder hints generated
   * by the ext-parser.
   */
  public static final String EXT_PARSER_TYPE_HINT_VALUE =
      "org.jfree.report.modules.parser.ext";

  /**
   * Instantiates the handler. The handler must be initialized properly before
   * it can be used.
   */
  public ExtReportHandler()
  {
  }

  /**
   * Initializes the new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @throws SAXException if an error occurs.
   */
  public void init(final ReportParser parser, final String finishTag)
      throws SAXException
  {
    this.parser = parser;
    this.finishTag = finishTag;

    if (parser.isIncluded())
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
        addComment(tagName, CommentHandler.OPEN_TAG_COMMENT);
        final JFreeReport report = parser.getReport();
        report.setName(attrs.getValue("name"));
      }
      // ignore it ...
    }
    else if (tagName.equals(PARSER_CONFIG_TAG))
    {
      // creates the various factories ... order does matter ...
      addComment(tagName, CommentHandler.OPEN_TAG_COMMENT);
      getParser().pushFactory(new ParserConfigHandler(parser, tagName));
    }
    else if (tagName.equals(REPORT_CONFIG_TAG))
    {
      addComment(tagName, CommentHandler.OPEN_TAG_COMMENT);
      getParser().pushFactory(new ReportConfigHandler(parser, tagName));
    }
    else if (tagName.equals(STYLES_TAG))
    {
      addComment(tagName, CommentHandler.OPEN_TAG_COMMENT);
      getParser().pushFactory(new StylesHandler(parser, tagName));
    }
    else if (tagName.equals(TEMPLATES_TAG))
    {
      addComment(tagName, CommentHandler.OPEN_TAG_COMMENT);
      getParser().pushFactory(new TemplatesHandler(parser, tagName));
    }
    else if (tagName.equals(FUNCTIONS_TAG))
    {
      addComment(tagName, CommentHandler.OPEN_TAG_COMMENT);
      getParser().pushFactory(new FunctionsHandler(parser, tagName));
    }
    else if (tagName.equals(DATA_DEFINITION_TAG))
    {
      addComment(tagName, CommentHandler.OPEN_TAG_COMMENT);
      getParser().pushFactory(new DataDefinitionHandler(parser, tagName));
    }
    else if (tagName.equals(REPORT_DESCRIPTION_TAG))
    {
      addComment(tagName, CommentHandler.OPEN_TAG_COMMENT);
      getParser().pushFactory(new ReportDescriptionHandler(parser, tagName));
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
      addComment(tagName, CommentHandler.CLOSE_TAG_COMMENT);
      getParser().popFactory().endElement(tagName);
    }
    else if (tagName.equals(PARSER_CONFIG_TAG))
    {
      // ignore this event ... forwarded from subFactory ...
      addComment(tagName, CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(REPORT_CONFIG_TAG))
    {
      // ignore this event ... should not happen here ...
      addComment(tagName, CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(STYLES_TAG))
    {
      // ignore this event ... should not happen here ...
      addComment(tagName, CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(TEMPLATES_TAG))
    {
      // ignore this event ... should not happen here ...
      addComment(tagName, CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(FUNCTIONS_TAG))
    {
      // ignore this event ... should not happen here ...
      addComment(tagName, CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(DATA_DEFINITION_TAG))
    {
      // ignore this event ... should not happen here ...
      addComment(tagName, CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(REPORT_DESCRIPTION_TAG))
    {
      // ignore this event ... should not happen here ...
      addComment(tagName, CommentHandler.CLOSE_TAG_COMMENT);
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
   * Adds an comment to the report builder hints collection.
   *
   * @param tagName the tag name for which to store the comment.
   * @param commentHint the comment hint path.
   */
  private void addComment(final String tagName, final String commentHint)
  {
    parser.getReport().getReportBuilderHints().putHint
        (createCommentHintPath(tagName), commentHint, parser.getComments());
  }

  /**
   * Creates a comment hint path for the given name.
   *
   * @param tagName the path name
   * @return the created comment hint path.
   */
  private CommentHintPath createCommentHintPath(final String tagName)
  {
    final CommentHintPath path = new CommentHintPath();
    path.addName(ExtParserModuleInit.REPORT_DEFINITION_TAG);
    if (tagName.equals(ExtParserModuleInit.REPORT_DEFINITION_TAG))
    {
      return path;
    }
    path.addName(tagName);
    return path;
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
