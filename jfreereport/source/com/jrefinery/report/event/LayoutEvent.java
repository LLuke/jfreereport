/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * LayoutEvent.java
 * ----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner
 * Contributor(s):   -;
 *
 * $Id: LayoutEvent.java,v 1.2 2003/02/26 13:57:56 mungady Exp $
 *
 * Changes
 * -------
 * 25-Feb-2003 : Initial version
 */
package com.jrefinery.report.event;

import com.jrefinery.report.Band;
import com.jrefinery.report.states.ReportState;

/**
 * The LayoutEvent describes the current report state and the current band, which
 * had been laid out for printing.
 * 
 * @author Thomas Morgner
 */
public class LayoutEvent extends ReportEvent
{
  public static final int LAYOUT_EVENT = 0x20000;

  /** the current band. */
  private Band layoutedBand;

  /**
   * Creates a new LayoutEvent.
   *
   * @param state the current report state.
   * @param band the layouted band.
   */
  public LayoutEvent(ReportState state, Band band, int type)
  {
    super(state, type);

    if (band == null)
    {
      throw new NullPointerException();
    }
    this.layoutedBand = band;
  }

  /**
   * Gets the layouted band. This band will be printed next.
   *
   * @return the layouted band.
   */
  public Band getLayoutedBand()
  {
    return layoutedBand;
  }
}
