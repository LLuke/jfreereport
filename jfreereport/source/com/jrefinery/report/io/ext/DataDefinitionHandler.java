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
 * --------------------------
 * DataDefinitionHandler.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataDefinitionHandler.java,v 1.3 2003/02/24 10:37:53 mungady Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import org.xml.sax.Attributes;

/**
 * Not fully implemented. Will serve as description of the used tables for the
 * subreports ... not usefull or even implemented ...
 * 
 * @author Thomas Morgner.
 */
public class DataDefinitionHandler implements ReportDefinitionHandler
{
  /** The parser. */
  private Parser parser;

  /**
   * Creates a new handler. 
   * 
   * @param parser  the parser.
   */
  public DataDefinitionHandler(Parser parser)
  {
    this.parser = parser;
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser. 
   * 
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   */
  public void startElement(String tagName, Attributes attrs)
  {
  }

  /**
   * Callback to indicate that some character data has been read.
   * 
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */
  public void characters(char ch[], int start, int length)
  {
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser. 
   * 
   * @param tagName  the tag name.
   */
  public void endElement(String tagName)
  {
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
   * Sets the parser.
   * 
   * @param parser  the parser.
   */
  public void setParser(Parser parser)
  {
    this.parser = parser;
  }
}
