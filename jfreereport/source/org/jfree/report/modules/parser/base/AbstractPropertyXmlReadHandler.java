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
 * AbstractPropertyXmlReadHandler.java
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

import org.jfree.report.ReportBuilderHints;
import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Todo: Handle comments ...
 */
public abstract class AbstractPropertyXmlReadHandler extends AbstractXmlReadHandler
{
  public static final String OPEN_TAG_COMMENT = "xml-comment-open-tag";
  public static final String CLOSE_TAG_COMMENT = "xml-comment-close-tag";

  private String[] preOpenTagComments;
  private String[] preCloseTagComments;

  protected AbstractPropertyXmlReadHandler ()
  {
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected final XmlReadHandler getHandlerForChild (final String tagName,
                                                     final Attributes atts)
          throws XmlReaderException, SAXException
  {
    return getHandlerForChild(tagName, new PropertyAttributes(getRootHandler(), atts));
  }

  protected XmlReadHandler getHandlerForChild (final String tagName,
                                               final PropertyAttributes attrs)
          throws XmlReaderException, SAXException
  {
    return null;
  }


  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected final void startParsing (final Attributes attrs)
          throws SAXException, XmlReaderException
  {
    preOpenTagComments = getRootHandler().getCommentHandler().getComments();
    startParsing(new PropertyAttributes(getRootHandler(), attrs));
  }

  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException, XmlReaderException
  {
    super.startParsing(attrs);
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    storeCloseComments();
    storeComments();
  }

  protected final void storeCloseComments ()
  {
    preCloseTagComments = getRootHandler().getCommentHandler().getComments();
  }

  protected abstract void storeComments ()
          throws SAXException;

  protected final void defaultStoreComments (final CommentHintPath hintPath)
  {
    if (preOpenTagComments != null)
    {
      for (int i = 0; i < preOpenTagComments.length; i++)
      {
        getBuilderHints().addHintList
                (hintPath, OPEN_TAG_COMMENT, preOpenTagComments[i], false);
      }
    }
    if (preCloseTagComments != null)
    {
      for (int i = 0; i < preCloseTagComments.length; i++)
      {
        getBuilderHints().addHintList
                (hintPath, CLOSE_TAG_COMMENT, preCloseTagComments[i], false);
      }
    }
  }

  public String[] getPreCloseTagComments ()
  {
    return preCloseTagComments;
  }

  public String[] getPreOpenTagComments ()
  {
    return preOpenTagComments;
  }

  protected ReportBuilderHints getBuilderHints ()
  {
    final ReportParser parser = (ReportParser) getRootHandler();
    return parser.getParserHints();
  }
}
