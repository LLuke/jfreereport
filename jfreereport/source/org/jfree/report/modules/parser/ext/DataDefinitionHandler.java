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
 * --------------------------
 * DataDefinitionHandler.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataDefinitionHandler.java,v 1.2 2003/07/18 17:56:38 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.modules.parser.base.ReportParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Not fully implemented. Will serve as description of the used tables for the
 * subreports ... not usefull or even implemented ...
 *
 * @author Thomas Morgner.
 */
public class DataDefinitionHandler extends AbstractExtReportParserHandler
{
  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag the finish tag.
   */
  public DataDefinitionHandler(final ReportParser parser, final String finishTag)
  {
    super(parser, finishTag);
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   * @throws SAXException as this is not defined in the DTD yet.
   */
  public void startElement(final String tagName, final Attributes attrs) 
    throws SAXException
  {
    throw new SAXException
      ("Did not expect child elements here. [this section is not yet defined.]");
  }

  /**
   * Callback to indicate that some character data has been read.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */
  public void characters(final char[] ch, final int start, final int length)
  {
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @throws SAXException as this not yet defined. 
   */
  public void endElement(final String tagName) throws SAXException
  {
    if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Expected '" + getFinishTag() + "'");
    }
  }
}
