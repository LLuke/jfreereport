/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PageFooter.java,v 1.8 2002/12/02 18:24:08 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Multiple changes with introduction of XML format for report definition (DG);
 * 10-May-2002 : Removed all but the default constructor. Added accessor functions for all
 *               properties.
 * 04-Jun-2002 : Documentation tags changed.
 * 06-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report;

import com.jrefinery.report.targets.style.BandStyleSheet;

/**
 * A report band that appears at the bottom of every page.  There is an option to suppress the
 * page footer on the first page, and another option does the same for the last page.
 *
 * @author David Gilbert
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
   *
   * @return true or false.
   */
  public boolean isDisplayOnFirstPage ()
  {
    return getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE, false);
  }

  /**
   * Defines whether the footer should be shown on the first page. 
   *
   * @param b  a flag indicating whether or not the footer is shown on the first page.
   */
  public void setDisplayOnFirstPage (boolean b)
  {
    getStyle().setStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE, new Boolean(b));
  }

  /**
   * Returns true if the footer should be shown on the last page, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isDisplayOnLastPage ()
  {
    return getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE, false);
  }

  /**
   * Defines whether the footer should be shown on the last page. 
   *
   * @param b  a flag indicating whether or not the footer is shown on the first page.
   */
  public void setDisplayOnLastPage (boolean b)
  {
    getStyle().setStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE, new Boolean(b));
  }
}
