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
 * ------------------------
 * ParserConfigHandler.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ParserConfigHandler.java,v 1.2 2003/07/18 17:56:38 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceFactory;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactory;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollection;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.report.util.Log;
import org.jfree.xml.ParseException;
import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.factory.objects.ClassFactoryCollector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A parser configuration handler. Handles the configuration of the parser itself.
 * The parser uses the factories defined here, to create the referenced Elements,
 * styles and objects.
 * <p>
 * The parser configuration must be the first tag in the report definition.
 *
 * @author Thomas Morgner
 */
public class ParserConfigHandler extends AbstractExtReportParserHandler
{
  /** The 'stylekey-factory' tag name. */
  public static final String STYLEKEY_FACTORY_TAG = "stylekey-factory";

  /** The 'template-factory' tag name. */
  public static final String TEMPLATE_FACTORY_TAG = "template-factory";

  /** The 'object-factory' tag name. */
  public static final String OBJECT_FACTORY_TAG = "object-factory";

  /** The 'datadefinition-factory' tag name. */
  public static final String DATADEFINITION_FACTORY_TAG = "datadefinition-factory";

  /** The 'datasource-factory' tag name. */
  public static final String DATASOURCE_FACTORY_TAG = "datasource-factory";

  /** The 'element-factory' tag name. */
  public static final String ELEMENT_FACTORY_TAG = "element-factory";

  /** The class attribute name. */
  public static final String CLASS_ATTRIBUTE = "class";

  public static final String STYLEKEY_FACTORY_HINT = "ext.parser.parser-config.stylekeyfactories";
  public static final String OBJECT_FACTORY_HINT = "ext.parser.parser-config.objectfactories";
  public static final String DATASOURCE_FACTORY_HINT = "ext.parser.parser-config.datasourcefactories";
  public static final String TEMPLATE_FACTORY_HINT = "ext.parser.parser-config.templatefactories";
  public static final String ELEMENT_FACTORY_HINT = "ext.parser.parser-config.elementfactories";

  /**
   * The parser configuration handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public ParserConfigHandler(final ReportParser parser, final String finishTag)
  {
    super(parser, finishTag);
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
    if (tagName.equals(STYLEKEY_FACTORY_TAG))
    {
      final String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null)
      {
        throw new ParseException("Attribute 'class' is missing.", getParser().getLocator());
      }
      final StyleKeyFactoryCollector fc =
          (StyleKeyFactoryCollector) getParser().getHelperObject(STYLEKEY_FACTORY_TAG);

      final StyleKeyFactory factory = (StyleKeyFactory) createFactory(className);
      fc.addFactory(factory);
      getParserHints().addHintList(getReport(), STYLEKEY_FACTORY_HINT, className);
      addComment(tagName, className);
    }
    else if (tagName.equals(OBJECT_FACTORY_TAG))
    {
      final String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null)
      {
        throw new ParseException("Attribute 'class' is missing.", getParser().getLocator());
      }
      final ClassFactoryCollector fc =
          (ClassFactoryCollector) getParser().getHelperObject(OBJECT_FACTORY_TAG);
      fc.addFactory((ClassFactory) createFactory(className));
      getParserHints().addHintList(getReport(), OBJECT_FACTORY_HINT, className);
      addComment(tagName, className);
    }
    else if (tagName.equals(TEMPLATE_FACTORY_TAG))
    {
      final String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null)
      {
        throw new ParseException("Attribute 'class' is missing.", getParser().getLocator());
      }
      final TemplateCollector fc =
          (TemplateCollector) getParser().getHelperObject(TEMPLATE_FACTORY_TAG);
      fc.addTemplateCollection((TemplateCollection) createFactory(className));
      getParserHints().addHintList(getReport(), TEMPLATE_FACTORY_HINT, className);
      addComment(tagName, className);
    }
    else if (tagName.equals(DATASOURCE_FACTORY_TAG))
    {
      final String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null)
      {
        throw new ParseException("Attribute 'class' is missing.", getParser().getLocator());
      }
      final DataSourceFactory factory = (DataSourceFactory) createFactory(className);
      final DataSourceCollector fc =
          (DataSourceCollector) getParser().getHelperObject(DATASOURCE_FACTORY_TAG);
      fc.addFactory(factory);
      getParserHints().addHintList(getReport(), DATASOURCE_FACTORY_HINT, className);
      addComment(tagName, className);
    }
    else if (tagName.equals(ELEMENT_FACTORY_TAG))
    {
      final String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null)
      {
        throw new ParseException("Attribute 'class' is missing.", getParser().getLocator());
      }
      final ElementFactoryCollector fc =
          (ElementFactoryCollector) getParser().getHelperObject(ELEMENT_FACTORY_TAG);
      fc.addFactory((ElementFactory) createFactory(className));
      getParserHints().addHintList(getReport(), ELEMENT_FACTORY_HINT, className);
      addComment(tagName, className);
    }
    else if (tagName.equals(DATADEFINITION_FACTORY_TAG))
    {
      // is not yet defined nor implemented ...
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + STYLEKEY_FACTORY_TAG + ", "
          + DATASOURCE_FACTORY_TAG + ", "
          + DATADEFINITION_FACTORY_TAG + ", "
          + TEMPLATE_FACTORY_TAG + ", "
          + OBJECT_FACTORY_TAG
      );
    }
  }

  /**
   * Creates a factory object.
   *
   * @param classname  the class name.
   *
   * @return The factory.
   *
   * @throws SAXException if the specified class cannot be loaded.
   */
  private Object createFactory(final String classname) throws SAXException
  {
    try
    {
      final Class f = getClass().getClassLoader().loadClass(classname);
      return f.newInstance();
    }
    catch (Exception e)
    {
      Log.error("Failed to parse Factory: ", e);
      throw new ParseException("Invalid Factory class specified: " + classname,
          getParser().getLocator());
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
    // is not used ...
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
    if (tagName.equals(STYLEKEY_FACTORY_TAG)
        || tagName.equals(DATADEFINITION_FACTORY_TAG)
        || tagName.equals(TEMPLATE_FACTORY_TAG)
        || tagName.equals(DATASOURCE_FACTORY_TAG)
        || tagName.equals(ELEMENT_FACTORY_TAG)
        || tagName.equals(OBJECT_FACTORY_TAG))
    {
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + STYLEKEY_FACTORY_TAG + ", "
          + DATADEFINITION_FACTORY_TAG + ", "
          + TEMPLATE_FACTORY_TAG + ", "
          + DATASOURCE_FACTORY_TAG + ", "
          + ELEMENT_FACTORY_TAG + ", "
          + OBJECT_FACTORY_TAG);
    }
  }

  private void addComment (String tagName, String className)
  {
    CommentHintPath path = new CommentHintPath();
    path.addName(ExtParserModuleInit.REPORT_DEFINITION_TAG);
    path.addName(ExtReportHandler.PARSER_CONFIG_TAG);
    path.addName(tagName);
    path.addName(className);
    getReport().getReportBuilderHints().putHint
        (path, CommentHandler.OPEN_TAG_COMMENT, getReportParser().getComments());
  }
}
