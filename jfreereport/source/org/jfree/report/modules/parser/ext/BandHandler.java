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
 * ----------------
 * BandHandler.java
 * ----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandHandler.java,v 1.2 2003/07/18 17:56:38 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.xml.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A band handler. Handles the creation of a band. Bands can contain other bands
 * or elements and have a DefaultStyle, which is assigned to all child elements.
 *
 * @author Thomas Morgner
 */
public class BandHandler extends ElementHandler
{
  /** The 'band' tag. */
  public static final String BAND_TAG = "band";

  /** The 'element' tag. */
  public static final String ELEMENT_TAG = "element";

  /** The 'default-style' tag. */
  public static final String DEFAULT_STYLE_TAG = "default-style";

  /** An element handler, holding a reference to the currently processed child element. */
  private ElementHandler elementHandler;

  /**
   * Creates a new band handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param band  the band.
   */
  public BandHandler(final ReportParser parser, final String finishTag,
                     final Band band, final CommentHintPath path)
  {
    super(parser, finishTag, band, path);
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
    if (tagName.equals(BAND_TAG))
    {
      final Band band = new Band();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      CommentHintPath path = createCommentPath(band);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      elementHandler = new BandHandler(getReportParser(), tagName, band, getCommentPath());
      getParser().pushFactory(elementHandler);
      // ignore
    }
    else if (tagName.equals(ELEMENT_TAG))
    {
      final String type = attrs.getValue("type");
      if (type == null)
      {
        throw new ParseException("The element's 'type' attribute is missing",
            getParser().getLocator());
      }

      final ElementFactoryCollector fc = (ElementFactoryCollector) getParser().getHelperObject(
          ParserConfigHandler.ELEMENT_FACTORY_TAG);
      final Element element = fc.getElementForType(type);

      final String name = attrs.getValue("name");
      if (name != null)
      {
        element.setName(name);
      }

      CommentHintPath path = createCommentPath(element);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      elementHandler = new ElementHandler(getReportParser(), tagName, element, getCommentPath());
      getParser().pushFactory(elementHandler);
    }
    else if (tagName.equals(DEFAULT_STYLE_TAG))
    {
      CommentHintPath path = createCommentPath(tagName);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);

      final ElementStyleSheet styleSheet = getBand().getBandDefaults();
      final StyleSheetHandler styleSheetFactory =
          new StyleSheetHandler(getReportParser(), tagName, styleSheet, path);
      getParser().pushFactory(styleSheetFactory);
    }
    else
    {
      super.startElement(tagName, attrs);
    }
  }

  /**
   * Returns the band.
   *
   * @return The band.
   */
  private Band getBand()
  {
    return (Band) getElement();
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
    if (tagName.equals(BAND_TAG))
    {
      if (elementHandler != null)
      {
        addComment(createCommentPath(elementHandler.getElement()), CommentHandler.CLOSE_TAG_COMMENT);
        getBand().addElement(elementHandler.getElement());
        elementHandler = null;
      }
      else
      {
        super.endElement(tagName);
      }
    }
    else if (tagName.equals(ELEMENT_TAG))
    {
      addComment(createCommentPath(elementHandler.getElement()), CommentHandler.CLOSE_TAG_COMMENT);
      getBand().addElement(elementHandler.getElement());
      elementHandler = null;
    }
    else if (tagName.equals(DEFAULT_STYLE_TAG))
    {
      addComment(createCommentPath(tagName), CommentHandler.CLOSE_TAG_COMMENT);
    }
    else
    {
      super.endElement(tagName);
    }
  }
}
