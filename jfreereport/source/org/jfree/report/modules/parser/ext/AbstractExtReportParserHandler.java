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
 * ------------------------------
 * AbstractExtReportParserHandler.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractExtReportParserHandler.java,v 1.1 2003/07/18 18:31:25 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 15.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.ReportBuilderHints;
import org.jfree.report.JFreeReport;
import org.jfree.report.util.Log;

public abstract class AbstractExtReportParserHandler
    implements ElementDefinitionHandler
{
  /** The finish tag. */
  private String finishTag;

  private ReportParser parser;

  /**
   * Initializes this ElementDefinitionHandler implementation with the give parser.
   * @param parser
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

  public Parser getParser()
  {
    return parser;
  }

  public ReportParser getReportParser()
  {
    return parser;
  }

  public JFreeReport getReport()
  {
    return parser.getReport();
  }

  public ReportBuilderHints getParserHints ()
  {
    return parser.getParserHints();
  }

  public String getFinishTag()
  {
    return finishTag;
  }

  protected void addComment (CommentHintPath path, String hint)
  {
    String[] comments = getReportParser().getComments();
    if (comments != null)
    {
      // Log.debug ("Adding Comment: " + path + " COm: " + comments);
      getReport().getReportBuilderHints().putHint
          (path, hint, comments);
    }
  }
}
