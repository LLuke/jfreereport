/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ----------------
 * ReportParser.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportParser.java,v 1.4 2003/07/20 19:31:16 taqua Exp $
 *
 * Changes
 * -------
 * 13.04.2003 : Initial version
 */
package org.jfree.report.modules.parser.base;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportBuilderHints;
import org.jfree.xml.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

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
  public static final String HELPER_OBJ_REPORT_NAME = "report";
  private CommentHandler commentHandler;

  /**
   * Default constuctor. Initalizes the parser to use the JFreeReport parser
   * files.
   */
  public ReportParser()
  {
    setInitialFactory(new InitialReportHandler(this));
    commentHandler = new CommentHandler();
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
  public JFreeReport getReport ()
  {
    return (JFreeReport) getResult();
  }

  /**
   *
   * @return
   */
  public ReportBuilderHints getParserHints ()
  {
    if (getReport() == null)
    {
      throw new IllegalStateException("There is no report defined yet.");
    }
    return getReport().getReportBuilderHints();
  }

  public CommentHandler getCommentHandler()
  {
    return commentHandler;
  }

  public String[] getComments ()
  {
    return getCommentHandler().getComments();
  }

  public boolean isIncluded ()
  {
    return getConfigProperty(IncludeParser.INCLUDE_PARSING_KEY, "false").equals("true");
  }

  public void endElement(String tagName, String namespace, String qName) throws SAXException
  {
    super.endElement(tagName, namespace, qName);
    getCommentHandler().clearComments();
  }

  public void startElement(String tagName, String namespace, String qName, Attributes attributes) throws SAXException
  {
    super.startElement(tagName, namespace, qName, attributes);
    getCommentHandler().clearComments();
  }
}
