/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * AbstractExtReportParserHandler.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: AbstractExtReportParserHandler.java,v 1.6 2003/08/25 14:29:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 15-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportBuilderHints;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;

/**
 * An abstract base implementation of the extended parser handlers.
 * This is used to reduce the codesize and the ammount of common code.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractExtReportParserHandler
    implements ElementDefinitionHandler
{
  /** The finish tag. */
  private String finishTag;

  /** The report parser is used to coordinate the whole parsing process. */
  private ReportParser parser;

  /**
   * Initializes this ElementDefinitionHandler implementation with the give parser.
   *
   * @param parser the parser used to process the xml events.
   * @param finishTag the finish tag specifies on what tag the handler should
   * deactivate itself.
   */
  public AbstractExtReportParserHandler(final ReportParser parser, final String finishTag)
  {
    if (parser == null)
    {
      throw new NullPointerException("Parser is null");
    }
    if (finishTag == null)
    {
      throw new NullPointerException("FinishTag is null");
    }
    this.parser = parser;
    this.finishTag = finishTag;
  }

  /**
   * Returns the parser instance used to coordinate the parsing process.
   * @see org.jfree.xml.ElementDefinitionHandler#getParser()
   *
   * @return the parser.
   */
  public Parser getParser()
  {
    return parser;
  }

  /**
   * Returns the parser instance as ReportParser implementation. This method
   * is implemented to reduce the ammount of casts and to support lazy
   * programmers. :)
   *
   * @return the parser.
   */
  public ReportParser getReportParser()
  {
    return parser;
  }

  /**
   * Returns the JFreeReport instance currently processed by the parser.
   * This method may return null, if the parsing is currently being started.
   *
   * @return the jfreereport instance processed.
   */
  public JFreeReport getReport()
  {
    return parser.getReport();
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
    return parser.getParserHints();
  }

  /**
   * Returns the current finish tag. When this tag is encountered, the
   * handler should deactivate itself.
   *
   * @return the finish tag.
   */
  public String getFinishTag()
  {
    return finishTag;
  }

  /**
   * An helper function to add an comment to the report builder hints
   * under the given path and hint name. The comment itself is read from
   * the parser.
   *
   * @param path the comment hint path used to mark the parse location.
   * @param hint the hint name to store the comment on the given position.
   */
  protected void addComment(final CommentHintPath path, final String hint)
  {
    final String[] comments = getReportParser().getComments();
    if (comments != null)
    {
      // Log.debug ("Adding Comment: " + path + " COm: " + comments);
      getReport().getReportBuilderHints().putHint
          (path, hint, comments);
    }
  }
}
