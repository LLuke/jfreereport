/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------------------
 * AbstractReportDefinitionHandler.java
 * ------------------------------------
 * (C)opyright 2002, 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractReportDefinitionHandler.java,v 1.4 2003/08/25 14:29:33 taqua Exp $
 *
 * Changes
 * -------
 * 24-Apr-2002 : Created to enable the XML-Parser to load external resources.
 * 10-May-2002 : Added helper functions to ease up the parsing.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 */

package org.jfree.report.modules.parser.simple;

import java.net.MalformedURLException;
import java.net.URL;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Extends the SAX-DefaultHandler with ContentBase capabilities.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractReportDefinitionHandler implements ElementDefinitionHandler
{
  /** the name generator parser-property name. */
  private static final String NAME_GENERATOR = "name-generator";

  /** the parser used to coordinate the ReportDefinitionHandlers. */
  private ReportParser parser;

  /** the current finishtag. */
  private String finishTag;

  /**
   * Constructor for the root handler.
   */
  protected AbstractReportDefinitionHandler()
  {
  }

  /**
   * Initializes the report definition handler.
   *
   * @param parser  the used parser to coordinate the parsing process.
   * @param finishTag  the finish tag, that should trigger the deactivation of this parser.
   *
   * @throws NullPointerException if the finishTag or the parser are null.
   */
  protected void init(final ReportParser parser, final String finishTag)
  {
    if (parser == null)
    {
      throw new NullPointerException();
    }
    if (finishTag == null)
    {
      throw new NullPointerException();
    }
    this.parser = parser;
    this.finishTag = finishTag;
  }

  /**
   * Default constructor.
   *
   * @param parser  the used parser to coordinate the parsing process.
   * @param finishTag  the finish tag, that should trigger the deactivation of this parser.
   *
   * @throws NullPointerException if the finishTag or the parser are null.
   */
  public AbstractReportDefinitionHandler(final ReportParser parser, final String finishTag)
  {
    init(parser, finishTag);
  }

  /**
   * Gets the selected finishTag for this ElementDefinitionHandler. The finish tag
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
  public JFreeReport getReport()
  {
    final ReportParser rparser = (ReportParser) getParser();
    return rparser.getReport();
  }

  /**
   * Gets the ContentBase used to resolve relative URLs.
   *
   * @return the current contentbase, or null if no contentBase is set.
   */
  public URL getContentBase()
  {
    final String contentBase = getParser().getConfigProperty(Parser.CONTENTBASE_KEY);
    if (contentBase == null)
    {
      throw new IllegalStateException("Content Base is null.");
    }
    try
    {
      return new URL(contentBase);
    }
    catch (MalformedURLException mfe)
    {
      throw new IllegalStateException("Content Base is illegal." + contentBase);
    }
  }

  /**
   * Returns the name generator instance for naming anonymous elements.
   *
   * @return the name generator instance used to name anonymous element during
   * the parsing process.
   */
  public NameGenerator getNameGenerator()
  {
    NameGenerator ng = (NameGenerator) getParser().getHelperObject(NAME_GENERATOR);
    if (ng == null)
    {
      ng = new NameGenerator();
      getParser().setHelperObject(NAME_GENERATOR, ng);
    }
    return ng;
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

  /**
   * Returns the parser as ReportParser reference.
   *
   * @return The parser.
   */
  public ReportParser getReportParser()
  {
    return parser;
  }

  /**
   * Processes characters (in this case, does nothing).
   *
   * @param ch  the character array.
   * @param start  the index of the first valid character.
   * @param length  the number of valid characters.
   *
   * @throws SAXException if there is a parsing problem.
   */
  public void characters(final char[] ch, final int start, final int length) throws SAXException
  {

  }

  /**
   * Returns the document locator of the parser or null, if there is no
   * locator defined.
   *
   * @return the document locator or null.
   */
  protected Locator getLocator()
  {
    return getParser().getLocator();
  }

}
