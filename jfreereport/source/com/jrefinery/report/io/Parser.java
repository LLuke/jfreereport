/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ---------------
 * Parser.java
 * ---------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Parser.java,v 1.3 2003/02/02 23:43:49 taqua Exp $
 *
 * Changes
 * -------
 * 09-Jan-2003 : Initial version.
 *
 */
package com.jrefinery.report.io;

import com.jrefinery.report.JFreeReport;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Hashtable;
import java.util.Stack;

/**
 * The Parser handles the SAXEvents and forwards the event call to the currently
 * active ReportDefinitionHandler. Contains methods to manage and
 * configure the parsing process.
 */
public class Parser extends DefaultHandler
{
  public static final String  CONTENTBASE_KEY = "content-base";

  private Stack activeFactories;
  private ReportDefinitionHandler initialFactory;
  private Hashtable parserConfiguration;

  public Parser()
  {
    activeFactories = new Stack();
    parserConfiguration = new Hashtable();
  }

  public void pushFactory (ReportDefinitionHandler factory)
  {
    activeFactories.push(factory);
  }

  public ReportDefinitionHandler peekFactory ()
  {
    return (ReportDefinitionHandler) activeFactories.peek();
  }

  public ReportDefinitionHandler popFactory ()
  {
    activeFactories.pop();
    return peekFactory();
  }

  /**
   * Receive notification of the end of the document.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the end
   * of a document (such as finalising a tree or closing an output
   * file).</p>
   *
   * @exception org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#endDocument
   */
  public void endDocument()
      throws SAXException
  {
    super.endDocument();
  }

  /**
   * Receive notification of the beginning of the document.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the beginning
   * of a document (such as allocating the root node of a tree or
   * creating an output file).</p>
   *
   * @exception org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#startDocument
   */
  public void startDocument()
      throws SAXException
  {
    if (initialFactory == null)
    {
      setInitialFactory(new InitialReportHandler(this));
    }
    activeFactories.clear();
    pushFactory(getInitialFactory());
  }

  /**
   * Receive notification of character data inside an element.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method to take specific actions for each chunk of character data
   * (such as adding the data to a node or buffer, or printing it to
   * a file).</p>
   *
   * @param ch The characters.
   * @param start The start position in the character array.
   * @param length The number of characters to use from the
   *               character array.
   * @exception org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#characters
   */
  public void characters(char ch[], int start, int length)
      throws SAXException
  {
    peekFactory().characters(ch, start, length);
  }

  /**
   * Receive notification of the end of an element.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the end of
   * each element (such as finalising a tree node or writing
   * output to a file).</p>
   *
   * @param uri
   * @param qName
   * @param localName The element type name.
   * @exception org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#endElement
   */
  public void endElement(String uri, String localName, String qName)
      throws SAXException
  {
    peekFactory().endElement(qName);
  }

  /**
   * Receive notification of the start of an element.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the start of
   * each element (such as allocating a new tree node or writing
   * output to a file).</p>
   *
   * @param uri
   * @param qName
   * @param localName The element type name.
   * @param attributes The specified or defaulted attributes.
   * @exception org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#startElement
   */
  public void startElement(String uri, String localName,
                           String qName, Attributes attributes)
      throws SAXException
  {
    peekFactory().startElement(qName, attributes);
  }

  public void setInitialFactory (ReportDefinitionHandler factory)
  {
    initialFactory = factory;
  }

  public ReportDefinitionHandler getInitialFactory ()
  {
    return initialFactory;
  }

  public Hashtable getParserConfiguration()
  {
    return parserConfiguration;
  }

  public Object getConfigurationValue (Object key)
  {
    return parserConfiguration.get(key);
  }

  public void setConfigurationValue (Object key, Object value)
  {
    if (value == null)
    {
      parserConfiguration.remove(key);
    }
    else
    {
      parserConfiguration.put(key, value);
    }
  }

  public Parser getInstance ()
  {
    return new Parser();
  }

  public JFreeReport getReport ()
  {
    return (JFreeReport) getConfigurationValue(InitialReportHandler.REPORT_DEFINITION_TAG);
  }
}
