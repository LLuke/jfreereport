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
 * -----------------
 * ReportFooter.java
 * -----------------
 * (C)opyright 2000-2002, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ReportFooter.java,v 1.3 2003/08/24 15:13:21 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Multiple changes with introduction of XML format for report definition (DG);
 * 10-May-2002 : Removed all complex constructors. property "ownpage" added.
 * 06-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report;

/**
 * A report band that appears as the very last band on the report.
 * <p>
 * Note that if there is a page footer on the last page of your report, it will be printed
 * below the report footer, the logic being that the page footer *always* appears at the bottom
 * of the page.  In many cases, it makes better sense to suppress the page footer on the last
 * page of the report (leaving just the report footer on the final page).
 *
 * @author David Gilbert
 */
public class ReportFooter extends Band implements RootLevelBand
{
  /**
   * Constructs a report footer containing no elements.
   */
  public ReportFooter()
  {
  }

  public void setReportDefinition (final ReportDefinition reportDefinition)
  {
    super.setReportDefinition(reportDefinition);
  }
  
}
