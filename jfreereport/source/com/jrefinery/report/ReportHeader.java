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
 * -----------------
 * ReportHeader.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: ReportHeader.java,v 1.4 2002/05/28 19:28:22 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Multiple changes with introduction of XML format for report definition (DG);
 * 10-May-2002 : Removed all complex constructors. Property "ownPage" can be set by accessor.
 * 04-Jun-2002 : Documentation tags changed.
 */

package com.jrefinery.report;

/**
 * A report band that is printed once-only at the beginning of the report.  A report header is
 * optional.
 * <P>
 * A flag can be set forcing the report generator to start a new page after printing the report
 * header.
 */
public class ReportHeader extends Band
{

  /** Flag indicating whether or not the report header appears on its own on page 1 of the
   report. */
  private boolean ownPage;

  /**
   * Constructs a report header, initially containing no elements.
   */
  public ReportHeader()
  {
  }

  /**
   * defines whether the report header appears on its own page.
   * @param b the new flag indicating whether to force a pagebreak.
   */
  public void setOwnPage(boolean b)
  {
    this.ownPage = b;

  }

  /**
   * Returns true if the report header appears on its own page, and false otherwise.
   * @return A flag indicating whether or not the header appears on its own page.
   */
  public boolean isOwnPage()
  {
    return this.ownPage;
  }
}
