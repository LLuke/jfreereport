/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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

 * $Id: InitialReportHandler.java,v 1.4 2003/02/26 16:42:15 mungady Exp $
 *
 * Changes
 * -------
 * 09-Jan-2003 : Initial version
 *
 */
package com.jrefinery.report.io;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.io.simple.ReportFactory;
import com.jrefinery.xml.ElementDefinitionHandler;
import com.jrefinery.xml.Parser;

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
  /** the document element tag for the extended report format. */
  public static final String REPORT_DEFINITION_TAG = "report-definition";
  
  /** the document element tag for the simple report format. */
  public static final String OLD_REPORT_TAG = "report";

  /** the parser that is used to coordinate the report parsing process. */
  private Parser parser;

  /**
   * Creates a new IntialReportHander for the given parser.
   *
   * @param parser the used parser for the report definition process.
   */
  public InitialReportHandler(Parser parser)
  {
    this.parser = parser;
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   * Selects the parser profile depending on the current tag name.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(REPORT_DEFINITION_TAG))
    {
      ElementDefinitionHandler reportDefinitionHandler = new ExtReportHandler(getParser(), tagName);
      getParser().pushFactory(reportDefinitionHandler);
    }
    else if (tagName.equals(OLD_REPORT_TAG))
    {
      ReportFactory reportDefinitionHandler = new ReportFactory(getParser(), tagName);
      getParser().pushFactory(reportDefinitionHandler);
      reportDefinitionHandler.startElement(tagName, attrs);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " 
                              + REPORT_DEFINITION_TAG + ".");
    }
  }

  /**
   * Callback to indicate that some character data has been read. This is ignored.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */
  public void characters(char ch[], int start, int length)
  {
    // characters are ignored at this point...
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(REPORT_DEFINITION_TAG))
    {
      // ignore the report definition tag
    }
    else if (tagName.equals(OLD_REPORT_TAG))
    {
      // also ignore this one ...
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " 
                              + REPORT_DEFINITION_TAG + ".");
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
