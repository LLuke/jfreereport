/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
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
 * ------------------------------------
 * AbstractReportDefinitionHandler.java
 * ------------------------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractReportDefinitionHandler.java,v 1.11 2002/12/12 12:26:56 mungady Exp $
 *
 * Changes
 * -------
 * 24-Apr-2002 : Created to enable the XML-Parser to load external resources.
 * 10-May-2002 : Added helper functions to ease up the parsing.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 */

package com.jrefinery.report.io.simple;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.InitialReportHandler;

import java.net.URL;

import org.xml.sax.SAXException;

/**
 * Extends the SAX-DefaultHandler with ContentBase capabilities.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractReportDefinitionHandler implements ReportDefinitionHandler
{
  private static final String NAME_GENERATOR = "name-generator";

  private Parser parser;
  private String finishTag;

  /**
   * Default constructor.
   */
  public AbstractReportDefinitionHandler (Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;
  }

  public String getFinishTag()
  {
    return finishTag;
  }

  /**
   * Returns the report after the parsing is complete.
   * <P>
   * Don't call until the report is completely built or you may get unexpected results.
   *
   * @return The parsed report.
   */
  public JFreeReport getReport ()
  {
    return (JFreeReport) getParser().getConfigurationValue(InitialReportHandler.REPORT_DEFINITION_TAG);
  }

  /**
   * @return the current contentbase, or null if no contentBase is set.
   */
  public URL getContentBase ()
  {
    return (URL) getParser().getConfigurationValue(Parser.CONTENTBASE_KEY);
  }

  public NameGenerator getNameGenerator ()
  {
    NameGenerator ng = (NameGenerator) getParser().getConfigurationValue(NAME_GENERATOR);
    if (ng == null)
    {
      ng = new NameGenerator();
      getParser().setConfigurationValue(NAME_GENERATOR, ng);
    }
    return ng;
  }

  public Parser getParser()
  {
    return parser;
  }

  public void characters(char ch[], int start, int length) throws SAXException
  {
  }
}
