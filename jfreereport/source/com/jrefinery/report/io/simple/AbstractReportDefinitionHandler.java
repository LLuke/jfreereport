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
 * $Id: AbstractReportDefinitionHandler.java,v 1.2 2003/02/02 23:43:50 taqua Exp $
 *
 * Changes
 * -------
 * 24-Apr-2002 : Created to enable the XML-Parser to load external resources.
 * 10-May-2002 : Added helper functions to ease up the parsing.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 */

package com.jrefinery.report.io.simple;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.InitialReportHandler;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import org.xml.sax.SAXException;

import java.net.URL;

/**
 * Extends the SAX-DefaultHandler with ContentBase capabilities.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractReportDefinitionHandler implements ReportDefinitionHandler
{
  /** the name generator parser-property name. */
  private static final String NAME_GENERATOR = "name-generator";

  /** the parser used to coordinate the ReportDefinitionHandlers. */
  private Parser parser;
  /** the current finishtag. */
  private String finishTag;

  /**
   * Default constructor.
   *
   * @param parser the used parser to coordinate the parsing process.
   * @param finishTag the finish tag, that should trigger the deactivation of this parser.
   * @throws NullPointerException if the finishTag or the parser are null.
   */
  public AbstractReportDefinitionHandler (Parser parser, String finishTag)
  {
    if (parser == null) throw new NullPointerException();
    if (finishTag == null) throw new NullPointerException();
    this.parser = parser;
    this.finishTag = finishTag;
  }

  /**
   * Gets the selected finishTag for this ReportDefinitionHandler. The finish tag
   * is used to recognize the right moment for deactivating this handler.
   *
   * @return the defined finish tag.
   */
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
   * Gets the ContentBase used to resolve relative URLs.
   *
   * @return the current contentbase, or null if no contentBase is set.
   */
  public URL getContentBase ()
  {
    return (URL) getParser().getConfigurationValue(Parser.CONTENTBASE_KEY);
  }

  /**
   * Returns the name generator instance for naming anonymous elements.
   *
   * @return the name generator instance used to name anonymous element during
   * the parsing process.
   */
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
