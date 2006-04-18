/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * StyleExpressionReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.factories.report.flow;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.report.modules.factories.common.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 09.04.2006, 13:40:08
 *
 * @author Thomas Morgner
 */
public class StyleExpressionReadHandler extends AbstractExpressionReadHandler
{
  private StyleKeyReadHandler styleKeyReadHandler;
  private StyleKey styleKey;

  public StyleExpressionReadHandler()
  {
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final Attributes atts)
          throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }
    if (tagName.equals("style-key"))
    {
      styleKeyReadHandler = new StyleKeyReadHandler();
      return styleKeyReadHandler;
    }
    return super.getHandlerForChild(uri, tagName, atts);
  }


  /**
   * Done parsing.
   *
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected void doneParsing() throws SAXException
  {
    super.doneParsing();
    if (styleKeyReadHandler == null)
    {
      throw new SAXException("Required element 'style-key' is missing.");
    }

    styleKey = styleKeyReadHandler.getStyleKey();
    if (styleKey == null)
    {
      // or should we simply create a dummy for that? // todo
      throw new SAXException("That stylekey is not recognized");
    }
  }

  public StyleKey getStyleKey()
  {
    return styleKey;
  }
  
}
