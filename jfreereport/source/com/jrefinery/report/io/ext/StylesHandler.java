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
 * ------------------
 * StylesHandler.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StylesHandler.java,v 1.14 2003/06/27 14:25:18 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import java.util.HashMap;

import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.ParseException;
import org.jfree.xml.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A styles handler. This handler processes the styles collection and collects
 * all predefined style sheets of the report definition. If the contained stylesheets
 * extend an other stylesheet, the extended stylesheet must be defined before it
 * is referenced.
 *
 * @author Thomas Morgner.
 * @see ElementStyleSheet
 */
public class StylesHandler implements ElementDefinitionHandler
{
  /** The 'styles-collection' tag name. */
  public static final String STYLES_COLLECTION = "styles-collection";

  /** The 'style' tag name. */
  public static final String STYLE_TAG = "style";

  /** The parser. */
  private Parser parser;

  /** The finish tag. */
  private String finishTag;

  /** The style collection. */
  private HashMap styleCollection;

  /** The style sheet. */
  private ElementStyleSheet styleSheet;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public StylesHandler(final Parser parser, final String finishTag)
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
    styleCollection = (HashMap) getParser().getHelperObject(STYLES_COLLECTION);
    if (styleCollection == null)
    {
      throw new IllegalStateException("No styles collection found in the configuration");
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
  public void startElement(final String tagName, final Attributes attrs) throws SAXException
  {
    if (tagName.equals(STYLE_TAG))
    {
      final String name = attrs.getValue("name");
      if (name == null)
      {
        throw new ParseException("Attribute 'name' is required", getParser().getLocator());
      }
      styleSheet = new ElementStyleSheet(name);

      final StyleSheetHandler styleSheetFactory = new StyleSheetHandler(getParser(),
          STYLE_TAG, styleSheet);
      getParser().pushFactory(styleSheetFactory);
    }
    else
    {
      throw new SAXException("Expected '" + STYLE_TAG + "'");
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
    // no such events ...
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
    if (tagName.equals(STYLE_TAG))
    {
      styleCollection.put(styleSheet.getName(), styleSheet);
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
}
