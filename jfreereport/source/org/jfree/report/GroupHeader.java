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
 * ----------------
 * GroupHeader.java
 * ----------------
 * (C)opyright 2000-2002, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: GroupHeader.java,v 1.5 2005/01/24 23:57:45 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Changed height from Number --> float (DG);
 * 10-May-2002 : remove all compex constructors, added PageBreakBeforePrintProperty
 * 04-Jun-2002 : Documentation tags changed.
 * 02-Dec-2002 : Moved local properties into the StyleSheet.
 * 06-Dec-2002 : Updated Javadocs (DG);
 * 06-Dec-2002 : Updated the changelog, deprecated the local methods.
 */

package org.jfree.report;

import org.jfree.report.style.BandStyleKeys;


/**
 * A report band that appears at the beginning of each instance of a group.
 *
 * @author David Gilbert
 */
public class GroupHeader extends Band implements RootLevelBand
{
  /**
   * Constructs a group header band, containing no elements.
   */
  public GroupHeader()
  {
  }

  /**
   * Checks, whether this group header should be repeated on new pages.
   *
   * @return true, if the header will be repeated, false otherwise
   */
  public boolean isRepeat()
  {
    return getStyle().getBooleanStyleProperty(BandStyleKeys.REPEAT_HEADER);
  }

  /**
   * Defines, whether this group header should be repeated on new pages.
   *
   * @param repeat true, if the header will be repeated, false otherwise
   */
  public void setRepeat(final boolean repeat)
  {
    getStyle().setBooleanStyleProperty(BandStyleKeys.REPEAT_HEADER, repeat);
  }

  public void setReportDefinition (final ReportDefinition reportDefinition)
  {
    super.setReportDefinition(reportDefinition);
  }
}
