/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ---------------
 * PageHeader.java
 * ---------------
 * (C)opyright 2000-2002, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PageHeader.java,v 1.6 2003/11/07 18:33:47 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Multiple changes with introduction of XML format for report definition (DG);
 * 10-May-2002 : Removed all but the default constructor. Added accessor functions for all
 *               properties.
 * 04-Jun-2002 : Documentation tags changed.
 * 06-Dec-2002 : Updated Javadocs (DG);
 */
package org.jfree.report;

import org.jfree.report.style.BandStyleSheet;

/**
 * A report band used to print information at the background of every page in the report.
 * There is an option to suppress the printing on the first page and the last page of the
 * report (this is often useful if you are using a report header and/or report footer).
 *
 * @author David Gilbert
 */
public class Watermark extends Band
{
  /**
   * Constructs a page header.
   */
  public Watermark()
  {
  }

  /**
   * Constructs a page footer containing no elements.
   * 
   * @param onFirstPage defines, whether the page header will be printed
   * on the first page
   * @param onLastPage defines, whether the page footer will be printed on the
   * last page.
   * 
   */
  public Watermark(final boolean onFirstPage, final boolean onLastPage)
  {
    super();
    setDisplayOnFirstPage(onFirstPage);
    setDisplayOnLastPage(onLastPage);
  }

  /**
   * Returns true if the header should be shown on page 1, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isDisplayOnFirstPage()
  {
    return getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE);
  }

  /**
   * Defines whether the header should be shown on the first page.
   *
   * @param b  a flag indicating whether or not the header is shown on the first page.
   */
  public void setDisplayOnFirstPage(final boolean b)
  {
    getStyle().setBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE, b);
  }

  /**
   * Returns true if the header should be shown on the last page, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isDisplayOnLastPage()
  {
    return getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE);
  }

  /**
   * Defines whether the header should be shown on the last page.
   *
   * @param b  a flag indicating whether or not the header is shown on the last page.
   */
  public void setDisplayOnLastPage(final boolean b)
  {
    getStyle().setBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE, b);
  }
}