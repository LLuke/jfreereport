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
 * PageHeader.java
 * ---------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: PageHeader.java,v 1.4 2002/06/04 19:20:37 taqua Exp $
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


/**
 * A report band used to print information at the top of every page in the report.  There is an
 * option to suppress the page header on the first page and the last page.
 *
 * @author DG
 */
public class PageHeader extends Band
{
  /** Flag that indicates whether or not the footer is printed on the first page of the report. */
  private boolean displayOnFirstPage;

  /** Flag that indicates whether or not the footer is printed on the last page of the report. */
  private boolean displayOnLastPage;

  /**
   * Constructs a page header.
   */
  public PageHeader ()
  {
    displayOnFirstPage = true;
    displayOnLastPage = true;
  }

  /**
   * Returns true if the header should be shown on page 1, and false otherwise.
   * @return A flag indicating whether or not the footer is shown on the first page.
   */
  public boolean isDisplayOnFirstPage ()
  {
    return this.displayOnFirstPage;
  }

  /**
   * defines whether the header should be shown on the first page. This property defaults
   * to true.
   * @param b A flag indicating whether or not the header is shown on the first page.
   */
  public void setDisplayOnFirstPage (boolean b)
  {
    this.displayOnFirstPage = b;
  }

  /**
   * Returns true if the header should be shown on the last page, and false otherwise.
   * @return A flag indicating whether or not the header is shown on the last page.
   */
  public boolean isDisplayOnLastPage ()
  {
    return this.displayOnLastPage;
  }

  /**
   * defines whether the header should be shown on the last page. This property defaults to
   * true
   * @param b A flag indicating whether or not the header is shown on the last page.
   */
  public void setDisplayOnLastPage (boolean b)
  {
    this.displayOnLastPage = b;
  }

}
