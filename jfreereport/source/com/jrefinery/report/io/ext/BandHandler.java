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
 * ----------------
 * BandHandler.java
 * ----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandHandler.java,v 1.11 2003/06/04 21:09:05 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.io.ext.factory.elements.ElementFactoryCollector;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.jfree.xml.Parser;
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

  /** An element handler. */
  private ElementHandler elementHandler;

  /**
   * Creates a new band handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param band  the band.
   */
  public BandHandler(Parser parser, String finishTag, Band band)
  {
    super(parser, finishTag, band);
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
    if (tagName.equals(BAND_TAG))
    {
      Band band = new Band();
      String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      elementHandler = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(elementHandler);
      // ignore
    }
    else if (tagName.equals(ELEMENT_TAG))
    {
      String type = attrs.getValue("type");
      if (type == null)
      {
        throw new ParseException ("The element's 'type' attribute is missing",
            getParser().getLocator());
      }

      ElementFactoryCollector fc = (ElementFactoryCollector) getParser().getHelperObject(
          ParserConfigHandler.ELEMENT_FACTORY_TAG);
      Element element = fc.getElementForType(type);

      String name = attrs.getValue("name");
      if (name != null)
      {
        element.setName(name);
      }

      elementHandler = new ElementHandler(getParser(), tagName, element);
      getParser().pushFactory(elementHandler);
    }
    else if (tagName.equals(DEFAULT_STYLE_TAG))
    {
      ElementStyleSheet styleSheet = getBand().getBandDefaults();
      StyleSheetHandler styleSheetFactory = new StyleSheetHandler(getParser(), tagName, styleSheet);
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
  private Band getBand ()
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
  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(BAND_TAG))
    {
      if (elementHandler != null)
      {
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
      getBand().addElement(elementHandler.getElement());
      elementHandler = null;
    }
    else if (tagName.equals(DEFAULT_STYLE_TAG))
    {
      // ignore event ...
    }
    else
    {
      super.endElement(tagName);
    }
  }
}
