/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * ReportFooterReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.Band;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.util.Log;
import org.jfree.xml.ParserUtil;
import org.xml.sax.SAXException;

public class ReportFooterReadHandler extends RootLevelBandReadHandler
{
  public ReportFooterReadHandler (final Band band)
  {
    super(band);
  }

  /**
   * Starts parsing.
   *
   * @param attr the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attr)
          throws SAXException
  {
    super.startParsing(attr);
    handleOwnPageAttr(attr);
  }

  private void handleOwnPageAttr (final PropertyAttributes attr)
  {
    final String ownPageAttr = attr.getValue("ownpage");
    if (ownPageAttr != null)
    {
      Log.warn("The 'ownpage' attribute of the <report-footer> tag is deprecated. " +
              "Use the 'pagebreak-before' attribute instead.");
      final boolean ownPage = ParserUtil.parseBoolean(ownPageAttr, false);
      getBand().getStyle().setBooleanStyleProperty
              (BandStyleKeys.PAGEBREAK_BEFORE, ownPage);
    }
  }

}
