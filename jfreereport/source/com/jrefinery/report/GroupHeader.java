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
 * ----------------
 * GroupHeader.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: GroupHeader.java,v 1.4 2002/05/28 19:28:22 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Changed height from Number --> float (DG);
 * 10-May-2002 : remove all compex constructors, added PageBreakBeforePrintProperty
 * 04-Jun-2002 : Documentation tags changed.
 */

package com.jrefinery.report;

/**
 * A report band that appears at the beginning of each instance of a group.
 */
public class GroupHeader extends Band
{

  private boolean pageBreak;

  /**
   * Constructs a group header band, containing no elements.
   */
  public GroupHeader()
  {
    pageBreak = false;
  }

  /**
   * @return true if this group should trigger a pagebreak before its header is printed,
   * false otherwise
   */
  public boolean hasPageBreakBeforePrint()
  {
    return pageBreak;
  }

  /**
   * defines whether this group should trigger a pagebreak before its header is printed.
   * @param pageBreakBefore the new trigger state
   */
  public void setPageBreakBeforePrint(boolean pageBreakBefore)
  {
    this.pageBreak = pageBreakBefore;
  }

}
