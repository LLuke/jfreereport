/**
 * =========================================
 * LibXML : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libxml/
 *
 * (C) Copyright 2006, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id: StringReadHandler.java,v 1.4 2007/04/01 13:46:34 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.xmlns.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A XmlReadHandler that reads character-data for the given element.
 *
 * @author Thomas Morgner
 */
public class StringReadHandler extends AbstractXmlReadHandler
{

  /**
   * A buffer containing the characters read so far.
   */
  private StringBuffer buffer;

  /**
   * The string under construction.
   */
  private String result;

  /**
   * Creates a new handler.
   */
  public StringReadHandler ()
  {
    super();
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing (final Attributes attrs)
          throws SAXException
  {
    this.buffer = new StringBuffer();
  }

  /**
   * This method is called to process the character data between element tags.
   *
   * @param ch     the character buffer.
   * @param start  the start index.
   * @param length the length.
   * @throws SAXException if there is a parsing error.
   */
  public void characters (final char[] ch, final int start, final int length)
          throws SAXException
  {
    this.buffer.append(ch, start, length);
  }

  /**
   * Done parsing.
   *
   * @throws SAXException       if there is a parsing error.
   */
  protected void doneParsing ()
          throws SAXException
  {
    this.result = this.buffer.toString();
    this.buffer = null;
  }

  /**
   * Returns the result as string.
   *
   * @return the parse-result as string.
   */
  public String getResult ()
  {
    return result;
  }

  /**
   * Returns the object for this element.
   *
   * @return the object.
   */
  public Object getObject ()
  {
    return this.result;
  }
}
