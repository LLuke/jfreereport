/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * ----------------------
 * PreItemGroupState.java
 * ----------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PreItemGroupState.java,v 1.5 2003/02/04 17:56:22 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.states;

/**
 * Prepare to print the items. This state fires the itemStarted-Event and advances to
 * the InItemGroupState state.
 *
 * @author David Gilbert
 */
public final class PreItemGroupState extends ReportState
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
   * Advances to the next state.  Normally this will be the '<code>IN-ITEM-GROUP</code>' state, 
   * but if the report's data (TableModel) has no rows, proceed to the 
   * '<code>POST-ITEM-GROUP</code>' state.
   *
   * @return the next state.
   */
  public ReportState advance ()
  {
    // inform everybody, that now items will be processed
    fireItemsStartedEvent ();

    // if the report has no data, proceed to PostItemGroup ...
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
   * @return true; Header related states preview the next itemband DataRow.
   */
  public boolean isPrefetchState ()
  {
    return true;
  }
}
