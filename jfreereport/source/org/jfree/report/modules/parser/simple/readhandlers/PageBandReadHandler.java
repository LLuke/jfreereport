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
 * PageBandReadHandler.java
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
import org.jfree.xml.ParserUtil;
import org.xml.sax.SAXException;

public class PageBandReadHandler extends RootLevelBandReadHandler
{
  /**
   * Literal text for an XML attribute.
   */
  public static final String ON_FIRST_PAGE_ATTR = "onfirstpage";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ON_LAST_PAGE_ATTR = "onlastpage";

  public PageBandReadHandler (final Band band)
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
    handleOnFirstPage(attr);
    handleOnLastPage(attr);
  }

  protected boolean isManualBreakAllowed ()
  {
    return false;
  }

  private void handleOnFirstPage (final PropertyAttributes attr)
  {
    final String breakBeforeAttr = attr.getValue(ON_FIRST_PAGE_ATTR);
    if (breakBeforeAttr != null)
    {
      final boolean breakBefore = ParserUtil.parseBoolean(breakBeforeAttr, false);
      getBand().getStyle().setBooleanStyleProperty
              (BandStyleKeys.DISPLAY_ON_FIRSTPAGE, breakBefore);
    }
  }

  private void handleOnLastPage (final PropertyAttributes attr)
  {
    final String breakBeforeAttr = attr.getValue(ON_LAST_PAGE_ATTR);
    if (breakBeforeAttr != null)
    {
      final boolean breakBefore = ParserUtil.parseBoolean(breakBeforeAttr, false);
      getBand().getStyle().setBooleanStyleProperty
              (BandStyleKeys.DISPLAY_ON_LASTPAGE, breakBefore);
    }
  }
}
