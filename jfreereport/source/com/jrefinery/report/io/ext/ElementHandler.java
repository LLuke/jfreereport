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
 * -------------------
 * ElementHandler.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementHandler.java,v 1.6 2003/02/25 12:48:19 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import java.util.HashMap;
import java.util.Map;

import com.jrefinery.report.Element;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollector;
import com.jrefinery.report.io.ext.factory.templates.TemplateDescription;
import com.jrefinery.report.targets.style.ElementStyleSheet;
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
public class ElementHandler implements ReportDefinitionHandler
{
  /** The 'style' tag. */
  public static final String STYLE_TAG = StylesHandler.STYLE_TAG;

  /** The 'template' tag. */
  public static final String TEMPLATE_TAG = TemplatesHandler.TEMPLATE_TAG;

  /** The 'datasource' tag. */
  public static final String DATASOURCE_TAG = DataSourceHandler.DATASOURCE_TAG;

  /** The finish tag is used to detect the end of the current processing level. */
  private String finishTag;

  /** The parser. */
  private Parser parser;

  /** The element. */
  private Element element;

  /** A style collection. */
  private HashMap styleCollection;

  /** A template handler. */
  private TemplateHandler templateFactory;

  /** A template collector. */
  private TemplateCollector templateCollector;

  /** A datasource handler. */
  private DataSourceHandler dataSourceHandler;

  /**
   * Creates a new element handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param element  the element.
   */
  public ElementHandler(Parser parser, String finishTag, Element element)
  {
    this.finishTag = finishTag;
    this.parser = parser;
    this.element = element;
    styleCollection
        = (HashMap) getParser().getConfigurationValue(StylesHandler.STYLES_COLLECTION);
    if (styleCollection == null)
    {
      throw new IllegalStateException("No styles collection found in the configuration");
    }
    templateCollector = (TemplateCollector)
        getParser().getConfigurationValue(ParserConfigHandler.TEMPLATE_FACTORY_TAG);
    if (templateCollector == null)
    {
      throw new IllegalStateException("No template collector defined for this parser?");
    }
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(TEMPLATE_TAG))
    {
      String references = attrs.getValue("references");
      if (references == null)
      {
        throw new SAXException("A parent template must be specified");
      }
      TemplateDescription template = templateCollector.getTemplate(references);
      if (template == null)
      {
        throw new SAXException("The template '" + references + "' is not defined");
      }
      // Clone the defined template ... we don't change the original ..
      template = (TemplateDescription) template.getInstance();
      templateFactory = new TemplateHandler(getParser(), TEMPLATE_TAG, template);
      getParser().pushFactory(templateFactory);
    }
    else if (tagName.equals(DATASOURCE_TAG))
    {
      String typeName = attrs.getValue("type");
      if (typeName == null)
      {
        throw new SAXException("The datasource type must be specified");
      }
      dataSourceHandler = new DataSourceHandler(getParser(), tagName, typeName);
      getParser().pushFactory(dataSourceHandler);
    }
    else if (tagName.equals(STYLE_TAG))
    {
      ElementStyleSheet styleSheet = element.getStyle();
      StyleSheetHandler styleSheetFactory
          = new StyleSheetHandler(getParser(), STYLE_TAG, styleSheet);
      getParser().pushFactory(styleSheetFactory);
    }
    else if (tagName.equals(finishTag))
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
  public void characters(char ch[], int start, int length) throws SAXException
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
  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(TEMPLATE_TAG))
    {
      TemplateDescription t = templateFactory.getTemplate();
      element.setDataSource(t.createTemplate());
      templateFactory = null;
    }
    else if (tagName.equals(DATASOURCE_TAG))
    {
      DataSource ds = (DataSource) dataSourceHandler.getValue();
      element.setDataSource(ds);
      dataSourceHandler = null;
    }
    else if (tagName.equals(STYLE_TAG))
    {
      // ignore event ...
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Expected '" + STYLE_TAG + "' or "
                             + finishTag + "', found : " + tagName);
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
   * Returns the element.
   *
   * @return The element.
   */
  public Element getElement()
  {
    return element;
  }

  /**
   * Returns the style collection.
   *
   * @return The style collection.
   */
  public Map getStyleCollection()
  {
    return styleCollection;
  }
}
