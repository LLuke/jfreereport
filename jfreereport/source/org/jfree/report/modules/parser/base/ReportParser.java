/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ----------------
 * ReportParser.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportParser.java,v 1.14 2003/11/07 18:33:56 taqua Exp $
 *
 * Changes
 * -------
 * 13.04.2003 : Initial version
 */
package org.jfree.report.modules.parser.base;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportBuilderHints;
import org.jfree.xml.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The report parser initializes the parsing engine and coordinates the parsing
 * process. Once the parsing is complete, the generated report instance can be
 * queries with getResult();
 * <p>
 * This parser produces JFreeReport objects.
 *
 * @author Thomas Morgner
 */
public class ReportParser extends Parser
{
  /** The key that stores the report defintion in the helper objects collection. */
  public static final String HELPER_OBJ_REPORT_NAME = "report";

  /**
   * Default constuctor. Initalizes the parser to use the JFreeReport parser
   * files.
   */
  public ReportParser()
  {
    setInitialFactory(new InitialReportHandler(this));
  }

  /**
   * Returns a new instance of the parser.
   *
   * @return The instance.
   */
  public Parser getInstance()
  {
    return new ReportParser();
  }

  /**
   * Returns the parsered object. This method will return the currently parsed
   * JFreeReport object.
   *
   * @return the parsed JFreeReport instance.
   */
  public Object getResult()
  {
    return getHelperObject(HELPER_OBJ_REPORT_NAME);
  }

  /**
   * Returns the jfreereport instance that is currently created.
   *
   * @return the current JFreeReport instance.
   */
  public JFreeReport getReport()
  {
    return (JFreeReport) getResult();
  }

  /**
   * Returns the report builder hints instance used to collect all
   * comments and other valueable information that cannot be restored
   * with just the parsed object model. This information is optional
   * but may support other automated tools like the ReportWriter.
   *
   * @return the report builder hints used to build this report.
   */
  public ReportBuilderHints getParserHints()
  {
    if (getReport() == null)
    {
      throw new IllegalStateException("There is no report defined yet.");
    }
    return getReport().getReportBuilderHints();
  }

  /**
   * Checks whether this report is a included report and not the main
   * report definition.
   *
   * @return true, if the report is included, false otherwise.
   */
  public boolean isIncluded()
  {
    return getConfigProperty(IncludeParser.INCLUDE_PARSING_KEY, "false").equals("true");
  }

  /**
   * Handles the end of an element.
   *
   * @see org.xml.sax.ContentHandler#endElement
   * (java.lang.String, java.lang.String, java.lang.String)
   *
   * @param tagName the tagname of the element.
   * @param namespace the current namespace
   * @param qName the fully qualified name
   * @throws SAXException if an error occured.
   */
  public void endElement
    (final String tagName, final String namespace, final String qName) 
    throws SAXException
  {
    super.endElement(tagName, namespace, qName);
  }

  /**
   * Handles the start of an element.
   * @see org.xml.sax.ContentHandler#startElement
   * (java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
   *
   * @param tagName the tagname of the element.
   * @param namespace the current namespace
   * @param qName the fully qualified name
   * @param attributes the elements attributes.
   * @throws SAXException if an error occured.
   */
  public void startElement(final String tagName, final String namespace,
                           final String qName, final Attributes attributes) throws SAXException
  {
    super.startElement(tagName, namespace, qName, attributes);
  }

  /**
   * Report a fatal XML parsing error.
   *
   * <p>The default implementation throws a SAXParseException.
   * Application writers may override this method in a subclass if
   * they need to take specific actions for each fatal error (such as
   * collecting all of the errors into a single report): in any case,
   * the application must stop all regular processing when this
   * method is invoked, since the document is no longer reliable, and
   * the parser may no longer report parsing events.</p>
   *
   * @param e The error information encoded as an exception.
   * @exception SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ErrorHandler#fatalError
   * @see SAXParseException
   */
  public void fatalError(final SAXParseException e)
      throws SAXException
  {
    if (getReport() != null)
    {
      final Locator locator = getLocator();
      int column = -1;
      int line = -1;
      if (locator != null)
      {
        column = locator.getColumnNumber();
        line = locator.getLineNumber();
      }
      final OperationResult result = new OperationResult
          (e.getMessage(), SeverityLevel.FATAL_ERROR, line, column);
      getParserHints().addHintList(ReportParser.class.getName(), "AllMessages", result);
      getParserHints().addHintList(ReportParser.class.getName(), "Error", result);
    }
    super.fatalError(e);
  }

  /**
   * Receive notification of a parser warning.
   *
   * <p>The default implementation does nothing.  Application writers
   * may override this method in a subclass to take specific actions
   * for each warning, such as inserting the message in a log file or
   * printing it to the console.</p>
   *
   * @param e The warning information encoded as an exception.
   * @exception SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ErrorHandler#warning
   * @see SAXParseException
   */
  public void warning(final SAXParseException e)
      throws SAXException
  {
    if (getReport() != null)
    {
      final Locator locator = getLocator();
      int column = -1;
      int line = -1;
      if (locator != null)
      {
        column = locator.getColumnNumber();
        line = locator.getLineNumber();
      }
      final OperationResult result = new OperationResult
          (e.getMessage(), SeverityLevel.WARNING, line, column);
      getParserHints().addHintList(ReportParser.class.getName(), "AllMessages", result);
      getParserHints().addHintList(ReportParser.class.getName(), "Warning", result);
    }
    super.warning(e);
  }

  /**
   * Receive notification of a recoverable parser error.
   *
   * <p>The default implementation does nothing.  Application writers
   * may override this method in a subclass to take specific actions
   * for each error, such as inserting the message in a log file or
   * printing it to the console.</p>
   *
   * @param e The warning information encoded as an exception.
   * @exception SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ErrorHandler#warning
   * @see SAXParseException
   */
  public void error(final SAXParseException e)
      throws SAXException
  {
    if (getReport() != null)
    {
      final Locator locator = getLocator();
      int column = -1;
      int line = -1;
      if (locator != null)
      {
        column = locator.getColumnNumber();
        line = locator.getLineNumber();
      }
      final OperationResult result = new OperationResult
          (e.getMessage(), SeverityLevel.ERROR, line, column);
      getParserHints().addHintList(ReportParser.class.getName(), "AllMessages", result);
      getParserHints().addHintList(ReportParser.class.getName(), "Error", result);
    }
    super.error(e);
  }

  /**
   * Places an informational message on the message queue.
   *
   * @param message the message.
   * @throws SAXException if an error occurs.
   */
  public void info(final String message)
      throws SAXException
  {
    if (getReport() != null)
    {
      final Locator locator = getLocator();
      int column = -1;
      int line = -1;
      if (locator != null)
      {
        column = locator.getColumnNumber();
        line = locator.getLineNumber();
      }
      final OperationResult result = new OperationResult
          (message, SeverityLevel.ERROR, line, column);
      getParserHints().addHintList(ReportParser.class.getName(), "AllMessages", result);
      getParserHints().addHintList(ReportParser.class.getName(), "Info", result);
    }
  }
}
