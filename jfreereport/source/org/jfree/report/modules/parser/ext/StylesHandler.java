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
 * $Id: StylesHandler.java,v 1.4 2003/07/23 13:56:43 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.xml.ParseException;
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
public class StylesHandler extends AbstractExtReportParserHandler
{
  private static final CommentHintPath STYLES_PATH = new CommentHintPath(new String[]{
    ExtParserModuleInit.REPORT_DEFINITION_TAG,
    ExtReportHandler.REPORT_DESCRIPTION_TAG,
    ExtReportHandler.STYLES_TAG
  });


  /** The 'styles-collection' tag name. */
  public static final String STYLES_COLLECTION = "styles-collection";

  /** The 'style' tag name. */
  public static final String STYLE_TAG = "style";

  /** The style collection. */
  private StyleSheetCollection styleCollection;

  /** The style sheet. */
  private ElementStyleSheet styleSheet;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public StylesHandler(final ReportParser parser, final String finishTag)
  {
    super(parser, finishTag);
    styleCollection = getReport().getStyleSheetCollection();
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

      CommentHintPath path = createPath(styleSheet);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      final StyleSheetHandler styleSheetFactory = new StyleSheetHandler(getReportParser(),
          STYLE_TAG, styleSheet, path);
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
      addComment(createPath(styleSheet), CommentHandler.CLOSE_TAG_COMMENT);
      styleCollection.addStyleSheet(styleSheet);
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

  private CommentHintPath createPath (ElementStyleSheet tdesc)
  {
    CommentHintPath path = STYLES_PATH.getInstance();
    path.addName(tdesc);
    return path;
  }

}
