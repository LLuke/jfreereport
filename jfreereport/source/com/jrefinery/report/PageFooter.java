/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * $Id: PageFooter.java,v 1.1.1.1 2002/04/25 17:02:24 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Multiple changes with introduction of XML format for report definition (DG);
 * 10-May-2002 : removed all but the default constructor. Added accessor functions for all properties.
 *
 */

package com.jrefinery.report;


/**
 * A report band that appears at the bottom of every page.  There is an option to suppress the
 * page footer on the first page, and also on the last page.
 */
public class PageFooter extends Band
{

  /** Flag that indicates whether or not the footer is printed on the first page of the report. */
  private boolean displayOnFirstPage;

  /** Flag that indicates whether or not the footer is printed on the last page of the report. */
  private boolean displayOnLastPage;

  /**
   * Constructs a page footer containing no elements.
   *
   * @param height The band height (in points).
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
    return this.displayOnFirstPage;
  }

  /**
   * defines whether the footer should be shown on the first page. This property defaults to
   * true.
   * @param b A flag indicating whether or not the footer is shown on the first page.
   */
  public void setDisplayOnFirstPage (boolean b)
  {
    this.displayOnFirstPage = b;
  }

  /**
   * Returns true if the footer should be shown on the last page, and false otherwise.
   * @return A flag indicating whether or not the footer is shown on the last page.
   */
  public boolean isDisplayOnLastPage ()
  {
    return this.displayOnLastPage;
  }

  /**
   * defines whether the footer should be shown on the first page. This property defaults to
   * true.
   * @param b A flag indicating whether or not the footer is shown on the first page.
   */
  public void setDisplayOnLastPage (boolean b)
  {
    this.displayOnLastPage = b;
  }
}