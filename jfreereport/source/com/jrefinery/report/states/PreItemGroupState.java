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
 * PreItemGroupState.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: PreItemGroupState.java,v 1.2 2002/11/07 21:45:28 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.states;

/**
 * Prepare to print the items. This state fires the itemStarted-Event and advances to
 * the InItemGroupState state.
 */
public class PreItemGroupState extends ReportState
{

  /**
   * Creates a new 'pre-item-group' state.
   *
   * @param previous  the previous state.
   */
  public PreItemGroupState (ReportState previous)
  {
    super (previous);
  }

  /**
   * Advances to the next state.
   *
   * @return the next state.
   */
  public ReportState advance ()
  {
    // Inform everybody, that now items will be processed.
    fireItemsStartedEvent ();

    // if the datasource is empty, skip the inItem-Section and proceed to PostItemGroup ...
    if (getReport().getData().getRowCount() == 0)
    {
      return new PostItemGroupState(this);
    }
    return new InItemGroupState (this);
  }

  /**
   * Returns the corrected display item for this state. As the currentItem has not yet advanced
   * we perform a readAHead lookup when populating elements.
   *
   * @return true.
   */
  public boolean isPrefetchState ()
  {
    return true;
  }
}
