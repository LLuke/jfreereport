/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id: StyleExpressionReadHandler.java,v 1.5 2006/12/03 20:24:09 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.modules.factories.report.flow;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.jfree.xmlns.parser.ParseException;

/**
 * Creation-Date: 09.04.2006, 13:40:08
 *
 * @author Thomas Morgner
 */
public class StyleExpressionReadHandler extends AbstractExpressionReadHandler
{
  private String styleKey;

  public StyleExpressionReadHandler()
  {
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing(final Attributes attrs) throws SAXException
  {
    final String styleKey = attrs.getValue
        (FlowReportFactoryModule.NAMESPACE, "style-key");
    if (styleKey == null)
    {
      throw new ParseException
          ("Required attribute stylekey is missing.", getLocator());
    }
    this.styleKey = styleKey;
    super.startParsing(attrs);
  }

  public String getStyleKey()
  {
    return styleKey;
  }

}
