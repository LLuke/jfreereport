/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -----------------
 * ReportHeader.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ReportHeader.java,v 1.11 2003/06/27 14:25:16 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Multiple changes with introduction of XML format for report definition (DG);
 * 10-May-2002 : Removed all complex constructors. Property "ownPage" can be set by accessor.
 * 04-Jun-2002 : Documentation tags changed.
 * 06-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report;

import com.jrefinery.report.targets.style.BandStyleSheet;

/**
 * A report band that is printed once only at the beginning of the report.
 * <p>
 * A flag can be set forcing the report generator to start a new page after printing the report
 * header.
 * <p>
 * Note that if there is a page header on the first page of your report, it will be printed
 * above the report header, the logic being that the page header *always* appears at the top
 * of the page.  In many cases, it makes better sense to suppress the page header on the first
 * page of the report (leaving just the report header on page 1).
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class ReportHeader extends Band
{
  /**
   * Constructs a report header, initially containing no elements.
   */
  public ReportHeader()
  {
  }

  /**
   * Defines whether the report header appears on its own page. The pagebreak
   * is forced after the report header was printed.
   *
   * @param b  the new flag indicating whether to force a pagebreak.
   */
  public void setOwnPage(final boolean b)
  {
    getStyle().setBooleanStyleProperty
        (BandStyleSheet.PAGEBREAK_AFTER, b);
  }

  /**
   * Returns true if the report header appears on its own page, and false otherwise.
   *
   * @return a flag indicating whether or not the header appears on its own page.
   */
  public boolean isOwnPage()
  {
    return getStyle().getBooleanStyleProperty(BandStyleSheet.PAGEBREAK_AFTER, false);
  }
}
