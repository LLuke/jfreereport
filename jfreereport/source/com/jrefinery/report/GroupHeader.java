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
 * ----------------
 * GroupHeader.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: GroupHeader.java,v 1.8 2002/09/13 15:38:04 mungady Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Changed height from Number --> float (DG);
 * 10-May-2002 : remove all compex constructors, added PageBreakBeforePrintProperty
 * 04-Jun-2002 : Documentation tags changed.
 */

package com.jrefinery.report;

import com.jrefinery.report.targets.style.BandStyleSheet;

/**
 * A report band that appears at the beginning of each instance of a group.
 *
 * @author DG
 */
public class GroupHeader extends Band
{
  /**
   * Constructs a group header band, containing no elements.
   */
  public GroupHeader ()
  {
  }

  /**
   * @return true if this group should trigger a pagebreak before its header is printed,
   * false otherwise
   */
  public boolean hasPageBreakBeforePrint ()
  {
    return getStyle().getBooleanStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE, false);
  }

  /**
   * Defines whether this group should trigger a pagebreak before its header is printed.
   *
   * @param pageBreakBefore the new trigger state
   */
  public void setPageBreakBeforePrint (boolean pageBreakBefore)
  {
    getStyle().setStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE, new Boolean(pageBreakBefore));
  }
}
