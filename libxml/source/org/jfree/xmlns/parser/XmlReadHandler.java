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
 * $Id: XmlReadHandler.java,v 1.4 2007/04/01 13:46:34 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.xmlns.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A handler for reading an XML element.
 *
 * @author Thomas Morgner
 */
public interface XmlReadHandler
{

  /**
   * This method is called at the start of an element.
   *
   * @param uri     the namespace uri.
   * @param tagName the tag name.
   * @param attrs   the attributes.
   * @throws SAXException if there is a parsing error.
   */
  public void startElement(String uri, String tagName, Attributes attrs)
      throws SAXException;

  /**
   * This method is called to process the character data between element tags.
   *
   * @param ch     the character buffer.
   * @param start  the start index.
   * @param length the length.
   * @throws SAXException if there is a parsing error.
   */
  public void characters(char[] ch, int start, int length)
      throws SAXException;

  /**
   * This method is called at the end of an element.
   *
   * @param uri     the namespace uri.
   * @param tagName the tag name.
   * @throws SAXException if there is a parsing error.
   */
  public void endElement(String uri, String tagName)
      throws SAXException;

  /**
   * Returns the object for this element or null, if this element does
   * not create an object.
   *
   * @return the object.
   * @throws SAXException if an parser error occured.
   */
  public Object getObject() throws SAXException;

  /**
   * Initialise.
   *
   * @param rootHandler the root handler.
   * @param uri         the namespace uri.
   * @param tagName     the tag name.
   * @throws SAXException if an parser-error occured.
   */
  public void init(RootXmlReadHandler rootHandler, String uri, String tagName)
      throws SAXException;

}
