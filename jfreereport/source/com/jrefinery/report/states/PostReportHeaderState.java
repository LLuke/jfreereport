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
 * --------------------
 * PostReportHeaderState.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;

/**
 * The report header has been printed. Proceed to the first group.
 */
public class PostReportHeaderState extends ReportState
{
  /**
   * Creates a 'post-report-header' state.
   *
   * @param reportstate the previous report state.
   */
  public PostReportHeaderState (ReportState reportstate)
  {
    super (reportstate);
  }

  /**
   * This state does nothing and advances directly to the first PreGroupHeaderState.
   *
   * @param rpc  the report processor.
   *
   * @return the next state.
   */
  public ReportState advance (ReportProcessor rpc)
  {
    return new PreGroupHeaderState (this);
  }

  /**
   * Returns the corrected display item for this state. As the currentItem has not yet advanced
   * we perform a readAHead lookup when populating elements.
   *
   * @return true
   */
  public boolean isPrefetchState ()
  {
    return true;
  }
}
