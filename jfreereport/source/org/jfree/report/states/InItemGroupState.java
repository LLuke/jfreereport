/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * ---------------------
 * InItemGroupState.java
 * ---------------------
 * (C)opyright 2000-2002, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: InItemGroupState.java,v 1.7 2005/09/19 15:38:48 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.states;

import org.jfree.report.Group;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;
import org.jfree.util.Log;

/**
 * Prints the itemBand. Before the band is printed, the items are advanced and the next
 * data row gets activated. Before any row has been read, the currentItem state is
 * BEFORE_FIRST_ITEM, comparable to ResultSet.isBeforeFirst () in java.sql.ResultSet.
 * After the item has advanced the elements are populated and an itemsAdvancedEvent is
 * fired.
 * <p/>
 * If the activated Item is the last item in its group, the next state will be an
 * PostItemGroupHeader.  In the other case, the current state remains this ItemsAdvanced
 * state.
 *
 * @author David Gilbert
 */
public final class InItemGroupState extends ReportState
{
  private boolean prevStateWasPrefetch;
  /**
   * Creates a new '<code>IN-ITEM-GROUP</code>' state.
   *
   * @param previous the previous state.
   */
  public InItemGroupState (final ReportState previous)
  {
    super(previous);
    prevStateWasPrefetch = previous.isPrefetchState();
    getDataRowBackend().setCurrentRow(getCurrentDisplayItem());
  }

  public int getEventCode ()
  {
    return ReportEvent.ITEMS_ADVANCED;
  }

  public boolean isPrevStateWasPrefetch()
  {
    return prevStateWasPrefetch;
  }

  public boolean isPrefetchState()
  {
    return prevStateWasPrefetch;
  }

  /**
   * Advances from this state to the next.
   *
   * @return the next state.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  public ReportState advance ()
  {
    // If there is enough space to print the itemband, advance the items, populate
    // the band and print it. If there was not enough space, the engine will return
    // here after the pagebreak.


    firePrepareEvent();

    prevStateWasPrefetch = false;
    advanceItem();

    fireItemsAdvancedEvent();

    // we have more data to work on
    // If the group is done, print the GroupFooter of the parent
    final Group group = getReport().getGroup(getCurrentGroupIndex());

    if (isLastItemInGroup(group, getDataRowBackend(), getDataRowPreview()))
    {
      return new PostItemGroupState(this);
    }
    else
    {
      return this;
    }
  }

  /**
   * Advances the active data row to the next line.
   */
  private void advanceItem ()
  {
    setCurrentDataItem(getCurrentDataItem() + 1);
    getDataRowBackend().setCurrentRow(getCurrentDisplayItem());
  }

}
