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
 * -------------------------
 * InitialReportHandler.java
 * -------------------------
 * (C)opyright 2002, 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *

 * $Id: InitialReportHandler.java,v 1.1 2003/07/07 22:44:08 taqua Exp $
 *
 * Changes
 * -------
 * 09-Jan-2003 : Initial version
 *
 */
package org.jfree.report.modules.parser.base;

import java.util.Hashtable;

import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The InitialReportHandler is used to decide, which parser profile to use
 * for parsing the xml definition.
 * <p>
 * If the root element is <code>report-definition</code>, then the extended
 * profile is used, if the root element is <code>report</code> then the simple
 * report definition format will be used.
 * <p>
 * Once one of the profiles is activated, the parser forwards all SAXEvents to
 * the selected ElementDefinitionHandler.
 *
 * @author Thomas Morgner
 */
public class InitialReportHandler implements ElementDefinitionHandler
{
  public static Hashtable definedHandlers;

  public static void registerHandler (String handler, String handlerClass)
  {
    if (definedHandlers == null)
    {
      definedHandlers = new Hashtable();
    }
    definedHandlers.put(handler, handlerClass);
  }

  public static void unregisterHandler (String handler)
  {
    if (definedHandlers == null)
    {
      return;
    }
    definedHandlers.remove(handler);
  }

  public static String getRegisteredHandler (String handler)
  {
    if (definedHandlers == null)
    {
      return null;
    }
    return (String) definedHandlers.get(handler);
  }

  /** the parser that is used to coordinate the report parsing process. */
  private Parser parser;

  private String activeRootTag;

  /**
   * Creates a new IntialReportHander for the given parser.
   *
   * @param parser the used parser for the report definition process.
   */
  public InitialReportHandler(final Parser parser)
  {
    this.parser = parser;
  }

  private ReportRootHandler loadHandler (String className) throws SAXException
  {
    try
    {
      Class handler = Class.forName(className);
      return (ReportRootHandler) handler.newInstance();
    }
    catch (Exception e)
    {
      throw new SAXException
          ("Unable to load handler. An optional handler module for " +
          "this report definition type is missing.");
    }
  }
  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   * Selects the parser profile depending on the current tag name.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws org.xml.sax.SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(final String tagName, final Attributes attrs)
      throws SAXException
  {
    String activeHandler = getRegisteredHandler(tagName);
    if (activeHandler == null)
    {
      throw new SAXException("No handler registered for the tag '" + tagName + "'");
    }

    final ReportRootHandler reportDefinitionHandler = loadHandler(activeHandler);
    reportDefinitionHandler.init((ReportParser) getParser(), tagName);
    getParser().pushFactory(reportDefinitionHandler);
    activeRootTag = tagName;
    reportDefinitionHandler.startElement(tagName, attrs);
  }

  /**
   * Callback to indicate that some character data has been read. This is ignored.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */
  public void characters(final char[] ch, final int start, final int length)
  {
    // characters are ignored at this point...
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws org.xml.sax.SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(final String tagName) throws SAXException
  {
    if (tagName.equals(activeRootTag))
    {
      // ignore the report definition tag
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of the "
          + " registered root tag names.");
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
