/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * PropertyStringReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.base;

import org.jfree.report.util.PropertyLookupParser;
import org.xml.sax.SAXException;

public class PropertyStringReadHandler extends AbstractPropertyXmlReadHandler
{
  private class StringLookupParser extends PropertyLookupParser
  {
    public StringLookupParser ()
    {
    }

    protected Object performInitialLookup (final String name)
    {
      return getRootHandler().getHelperObject(name);
    }
  }


  private CommentHintPath hintPath;
  private StringBuffer buffer;
  private StringLookupParser lookupParser;

  public PropertyStringReadHandler (final CommentHintPath hintPath)
  {
    this.hintPath = hintPath;
    this.buffer = new StringBuffer(100);
    this.lookupParser = new StringLookupParser();
  }

  protected void storeComments ()
          throws SAXException
  {
    if (hintPath != null)
    {
      defaultStoreComments(hintPath);
    }
  }

  /**
   * May be null...
   *
   * @return
   */
  protected CommentHintPath getHintPath ()
  {
    return hintPath;
  }

  /**
   * This method is called to process the character data between element tags.
   *
   * @param ch     the character buffer.
   * @param start  the start index.
   * @param length the length.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  public void characters (final char[] ch, final int start, final int length)
          throws SAXException
  {
    buffer.append(ch, start, length);
  }

  public String getResult ()
  {
    return lookupParser.translateAndLookup(buffer.toString());
  }


  /**
   * Returns the object for this element.
   *
   * @return the object.
   */
  public Object getObject ()
  {
    return getResult();
  }
}
