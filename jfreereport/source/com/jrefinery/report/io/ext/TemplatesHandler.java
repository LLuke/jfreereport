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
 * ----------------------------
 * ElementDefinitionHandler.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TemplatesHandler.java,v 1.11 2003/06/04 21:09:07 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.ext.factory.templates.TemplateCollector;
import com.jrefinery.report.io.ext.factory.templates.TemplateDescription;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
import org.jfree.xml.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A templates handler. The Templates collection can be used to predefine
 * common datasource definitions for elements. This allows the definition of
 * global format strings, f.i. for Number or Date fields.
 *
 * @author Thomas Morgner.
 * @see com.jrefinery.report.filter.templates.Template 
 */
public class TemplatesHandler implements ElementDefinitionHandler
{
  /** The template tag. */
  public static final String TEMPLATE_TAG = "template";

  /** The parser. */
  private Parser parser;

  /** The finish tag. */
  private String finishTag;

  /** A template collector. */
  private TemplateCollector templateCollector;

  /** A template handler. */
  private TemplateHandler templateFactory;

  /**
   * Creates a new templates handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public TemplatesHandler(Parser parser, String finishTag)
  {
    if (parser == null) 
    {
      throw new NullPointerException("Parser is null");
    }
    if (finishTag == null) 
    {
      throw new NullPointerException("FinishTag is null");
    }

    this.parser = parser;
    this.finishTag = finishTag;
    templateCollector = (TemplateCollector)
        getParser().getHelperObject(ParserConfigHandler.TEMPLATE_FACTORY_TAG);
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
  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(TEMPLATE_TAG) == false)
    {
      throw new ParseException("Expected tag '" + TEMPLATE_TAG + "'",
          getParser().getLocator());
    }

    String templateName = attrs.getValue("name");
    if (templateName == null)
    {
      throw new ParseException("The 'name' attribute is required for template definitions",
          getParser().getLocator());
    }
    String references = attrs.getValue("references");
    TemplateDescription template = templateCollector.getTemplate(references);
    if (template == null)
    {
      throw new ParseException("The template '" + references + "' is not defined",
          getParser().getLocator());
    }

    // Clone the defined template ... we don't change the original ..
    template = (TemplateDescription) template.getInstance();
    template.setName(templateName);
    templateFactory = new TemplateHandler(getParser(), TEMPLATE_TAG, template);
    getParser().pushFactory(templateFactory);
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
    // ignore characters ...
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(String tagName)
    throws SAXException
  {
    if (tagName.equals(TEMPLATE_TAG))
    {
      templateCollector.addTemplate(templateFactory.getTemplate());
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new ParseException("Wrong tag, expected one of " + finishTag + ","
                             + TEMPLATE_TAG, getParser().getLocator());
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
