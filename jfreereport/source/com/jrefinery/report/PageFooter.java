/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ---------------
 * PageFooter.java
 * ---------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: PageFooter.java,v 1.7 2002/09/13 15:38:04 mungady Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Multiple changes with introduction of XML format for report definition (DG);
 * 10-May-2002 : Removed all but the default constructor. Added accessor functions for all
 *               properties.
 * 04-Jun-2002 : Documentation tags changed.
 */

package com.jrefinery.report;

import com.jrefinery.report.targets.style.BandStyleSheet;

/**
 * A report band that appears at the bottom of every page.  There is an option to suppress the
 * page footer on the first page, and an other option does the same for the last page.
 *
 * @author DG
 */
public class PageFooter extends Band
{

  /**
   * Constructs a page footer containing no elements.
   */
  public PageFooter ()
  {
  }

  /**
   * Returns true if the footer should be shown on page 1, and false otherwise.
   * @return A flag indicating whether or not the footer is shown on the first page.
   */
  public boolean isDisplayOnFirstPage ()
  {
    return getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE, false);
  }

  /**
   * defines whether the footer should be shown on the first page. This property defaults to
   * true.
   * @param b A flag indicating whether or not the footer is shown on the first page.
   */
  public void setDisplayOnFirstPage (boolean b)
  {
    getStyle().setStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE, new Boolean(b));
  }

  /**
   * Returns true if the footer should be shown on the last page, and false otherwise.
   * @return A flag indicating whether or not the footer is shown on the last page.
   */
  public boolean isDisplayOnLastPage ()
  {
    return getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE, false);
  }

  /**
   * defines whether the footer should be shown on the last page. This property defaults to
   * true.
   * @param b A flag indicating whether or not the footer is shown on the first page.
   */
  public void setDisplayOnLastPage (boolean b)
  {
    getStyle().setStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE, new Boolean(b));
  }
}
