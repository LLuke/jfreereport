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
 * -------------------
 * ElementHandler.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementHandler.java,v 1.7 2003/08/20 17:24:35 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.Element;
import org.jfree.report.filter.DataSource;
import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateDescription;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.xml.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An element handler. Handles the creation and initialization of an Element.
 * The element is created using an ElementFactory and the element's content type
 * as creation key. All requested element definitions must be defined in one of
 * the ElementFactories, or the report definition will fail.
 *
 * @author Thomas Morgner.
 */
public class ElementHandler extends AbstractExtReportParserHandler
{
  /** The 'style' tag. */
  public static final String STYLE_TAG = StylesHandler.STYLE_TAG;

  /** The 'template' tag. */
  public static final String TEMPLATE_TAG = TemplatesHandler.TEMPLATE_TAG;

  /** The 'datasource' tag. */
  public static final String DATASOURCE_TAG = DataSourceHandler.DATASOURCE_TAG;

  /** The element. */
  private Element element;

  /** A template handler. */
  private TemplateHandler templateFactory;

  /** A template collector. */
  private TemplateCollector templateCollector;

  /** A datasource handler. */
  private DataSourceHandler dataSourceHandler;

  /** 
   * The comment hint path is used to store xml comments in the 
   * report builder hints collection. 
   */
  private CommentHintPath commentPath;

  /**
   * Creates a new element handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param element  the element.
   * @param path the path on where to search for ext-parser comments
   * in the report builder hints.
   */
  public ElementHandler(final ReportParser parser, final String finishTag,
                        final Element element, final CommentHintPath path)
  {
    super(parser, finishTag);
    if (path == null)
    {
      throw new NullPointerException("Comment hint path is not defined.");
    }
    if (element == null)
    {
      throw new NullPointerException("Element is null");
    }
    this.element = element;
    templateCollector = (TemplateCollector)
        getParser().getHelperObject(ParserConfigHandler.TEMPLATE_FACTORY_TAG);
    if (templateCollector == null)
    {
      throw new IllegalStateException("No template collector defined for this parser?");
    }
    this.commentPath = path.getInstance();
    this.commentPath.addName(element);
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(final String tagName, final Attributes attrs) throws SAXException
  {
    if (tagName.equals(TEMPLATE_TAG))
    {
      final String references = attrs.getValue("references");
      if (references == null)
      {
        throw new ParseException("A parent template must be specified", getParser().getLocator());
      }
      TemplateDescription template = templateCollector.getTemplate(references);
      if (template == null)
      {
        throw new ParseException("The template '" + references + "' is not defined",
            getParser().getLocator());
      }
      // Clone the defined template ... we don't change the original ..
      getParserHints().putHint(getElement(), "ext.parser.template-reference", references);
      template = (TemplateDescription) template.getInstance();
      CommentHintPath commentPath = createCommentPath(TEMPLATE_TAG);
      addComment(commentPath, CommentHandler.OPEN_TAG_COMMENT);
      templateFactory = new TemplateHandler(getReportParser(), TEMPLATE_TAG, template, commentPath);
      getParser().pushFactory(templateFactory);
    }
    else if (tagName.equals(DATASOURCE_TAG))
    {
      final String typeName = attrs.getValue("type");
      if (typeName == null)
      {
        throw new ParseException("The datasource type must be specified",
            getParser().getLocator());
      }
      CommentHintPath path = createCommentPath(tagName);
      dataSourceHandler = new DataSourceHandler(getReportParser(), tagName, typeName, path);
      getParser().pushFactory(dataSourceHandler);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
    }
    else if (tagName.equals(STYLE_TAG))
    {
      CommentHintPath path = createCommentPath(tagName);
      final ElementStyleSheet styleSheet = element.getStyle();
      final StyleSheetHandler styleSheetFactory
          = new StyleSheetHandler(getReportParser(), STYLE_TAG, styleSheet, path);
      getParser().pushFactory(styleSheetFactory);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
  }

  /**
   * Callback to indicate that some character data has been read.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void characters(final char[] ch, final int start, final int length) throws SAXException
  {
    // no characters allowed ...
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
    if (tagName.equals(TEMPLATE_TAG))
    {
      final TemplateDescription t = templateFactory.getTemplate();
      element.setDataSource(t.createTemplate());
      addComment(createCommentPath(tagName), CommentHandler.CLOSE_TAG_COMMENT);
      templateFactory = null;
    }
    else if (tagName.equals(DATASOURCE_TAG))
    {
      final DataSource ds = (DataSource) dataSourceHandler.getValue();
      element.setDataSource(ds);
      addComment(createCommentPath(tagName), CommentHandler.CLOSE_TAG_COMMENT);
      dataSourceHandler = null;
    }
    else if (tagName.equals(STYLE_TAG))
    {
      addComment(createCommentPath(tagName), CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Expected '" + STYLE_TAG + "' or "
          + getFinishTag() + "', found : " + tagName);
    }
  }

  /**
   * Returns the element generated by this handler.
   *
   * @return The element.
   */
  public Element getElement()
  {
    return element;
  }

  /**
   * Returns the comment hint path used in this factory. This path
   * is used to mark the parse position in the report builder hints.
   * 
   * @return the comment hint path.
   */
  protected CommentHintPath getCommentPath()
  {
    return commentPath;
  }

  /**
   * Creates a new comment hint path for the given name by appending
   * it to a copy of the current path.
   * 
   * @param name the name of the new path segment.
   * @return the new comment path.
   */
  protected CommentHintPath createCommentPath (Object name)
  {
    CommentHintPath path = getCommentPath().getInstance();
    path.addName(name);
    return path;
  }

}
