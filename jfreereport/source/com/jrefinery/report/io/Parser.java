/**
 * Date: Jan 9, 2003
 * Time: 8:27:33 PM
 *
 * $Id: Parser.java,v 1.1 2003/01/12 21:35:08 taqua Exp $
 */
package com.jrefinery.report.io;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.ReportConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Hashtable;
import java.util.Stack;

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
