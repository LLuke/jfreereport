/**
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
 * ReportGenerator.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: FinishState.java,v 1.2 2002/11/07 21:45:28 taqua Exp $
 *
 * Changes
 * -------
 * ----------------------
 * FinishState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessingException;

/**
 * The report is done. No advance will be done, every call to advance will return this
 * FinishState-State.
 */
public class FinishState extends ReportState
{
  /**
   * Creates a new 'finish' report state.
   *
   * @param previous  the previous state.
   */
  public FinishState (ReportState previous)
  {
    super (previous);
  }

  /**
   * Advances from this state to the next.  Since this is the 'finish' state, this method just
   * returns itself.
   *
   * @return this state.
   */
  public ReportState advance () throws ReportProcessingException
  {
    throw new ReportProcessingException ("Cannot advance beyond finish!");
  }

  /**
   * Returns true, to indicate that this is the 'finish' state.
   *
   * @return true, as this report is done and will no longer advance.
   */
  public boolean isFinish ()
  {
    return true;
  }

}
