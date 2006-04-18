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
 * AbstractXmlReadHandler.java
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
package org.jfree.report.modules.factories.common;

import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/** A base class for implementing an {@link org.jfree.xml.parser.XmlReadHandler}. */
public abstract class AbstractXmlReadHandler implements XmlReadHandler
{
  /** The root handler. */
  private RootXmlReadHandler rootHandler;

  /** The tag name. */
  private String tagName;

  /** THe namespace URI */
  private String uri;

  /** A flag indicating the first call. */
  private boolean firstCall = true;

  /** Creates a new handler. */
  public AbstractXmlReadHandler()
  {
  }

  /**
   * Initialises the handler.
   *
   * @param rootHandler the root handler.
   * @param tagName     the tag name.
   */
  public void init(final RootXmlReadHandler rootHandler,
                   final String uri,
                   final String tagName)
  {
    if (rootHandler == null)
    {
      throw new NullPointerException("Root handler must not be null");
    }
    if (tagName == null)
    {
      throw new NullPointerException("Tag name must not be null");
    }
    this.uri = uri;
    this.rootHandler = rootHandler;
    this.tagName = tagName;
  }

  /**
   * This method is called at the start of an element.
   *
   * @param tagName the tag name.
   * @param attrs   the attributes.
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  public final void startElement(final String uri,
                                 final String tagName,
                                 final Attributes attrs)
          throws SAXException
  {
    if (this.firstCall)
    {
      if (!this.tagName.equals(tagName) || !this.uri.equals(uri))
      {
        throw new SAXException(
                "Expected <" + this.tagName + ">, found <" + tagName + ">");
      }
      this.firstCall = false;
      startParsing(attrs);
    }
    else
    {
      final XmlReadHandler childHandler = getHandlerForChild(uri, tagName, attrs);
      if (childHandler == null)
      {
        Log.warn("Unknown tag <" + tagName + ">");
        return;
      }
      childHandler.init(getRootHandler(), uri, tagName);
      this.rootHandler.recurse(childHandler, uri, tagName, attrs);
    }
  }

  /**
   * This method is called to process the character data between element
   * tags.
   *
   * @param ch     the character buffer.
   * @param start  the start index.
   * @param length the length.
   * @throws SAXException if there is a parsing error.
   */
  public void characters(final char[] ch, final int start, final int length)
          throws SAXException
  {
    // nothing required
  }

  /**
   * This method is called at the end of an element.
   *
   * @param tagName the tag name.
   * @throws SAXException if there is a parsing error.
   */
  public final void endElement(final String uri,
                               final String tagName) throws SAXException
  {
    if (this.tagName.equals(tagName) && this.uri.equals(uri))
    {
      doneParsing();
      this.rootHandler.unwind(uri, tagName);
    }
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing(final Attributes attrs) throws SAXException
  {
    // nothing required
  }

  /**
   * Done parsing.
   *
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected void doneParsing() throws SAXException
  {
    // nothing required
  }

  protected boolean isSameNamespace (String url)
  {
    return ObjectUtilities.equal(url, getUri());
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
    return null;
  }

  /**
   * Returns the tag name.
   *
   * @return the tag name.
   */
  public String getTagName()
  {
    return this.tagName;
  }

  public String getUri()
  {
    return uri;
  }

  /**
   * Returns the root handler for the parsing.
   *
   * @return the root handler.
   */
  public RootXmlReadHandler getRootHandler()
  {
    return this.rootHandler;
  }

}