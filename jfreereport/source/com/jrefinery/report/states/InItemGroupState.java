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
 * $Id$
 *
 * Changes
 * -------
 * ----------------------
 * InItemGroupState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.Group;
import com.jrefinery.report.event.ReportEvent;

/**
 * Prints the itemBand. Before the band is printed, the items are advanced and the next
 * data row gets activated. Before any row has been read, the currentItem state is
 * BEFORE_FIRST_ITEM, comparable to ResultSet.isBeforeFirst () in java.sql.ResultSet.
 * After the item has advanced and before the band is printed, the elements are populated and an
 * itemsAdvanced-Event is fired.
 * <p>
 * If the activated Item is the last item in its group, the next state will be an
 * PostItemGroupHeader.  In the other case, the current state remains this ItemsAdvanced state.
 */
public class InItemGroupState extends ReportState
{
  /**
   * Creates a new 'in-item-group' state.
   *
   * @param previous  the previous state.
   */
  public InItemGroupState (ReportState previous)
  {
    super (previous);
  }

  /**
   * Advances from this state to the next.
   *
   * @param rpc  the report processor.
   *
   * @return the next state.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  public ReportState advance (ReportProcessor rpc) throws ReportProcessingException
  {
    JFreeReport report = this.getReport ();
    ItemBand itemBand = report.getItemBand ();

    // If there is enough space to print the itemband, advance the items, populate
    // the band and print it. If there was not enough space, the engine will return
    // here after the pagebreak.
    if (rpc.isSpaceFor (itemBand))
    {
      this.advanceItem ();

      int currItem = this.getCurrentDataItem ();
      int currGroup = this.getCurrentGroupIndex ();

      ReportEvent event = new ReportEvent (this);
      this.fireItemsAdvancedEvent (event);

      this.getDataRowConnector ().setDataRowBackend (this.getDataRowBackend ());
      rpc.printItemBand (itemBand);

      // we have more data to work on
      // If the group is done, print the GroupFooter of the parent
      Group group = report.getGroup (this.getCurrentGroupIndex ());

      if (group.isLastItemInGroup (this.getDataRowBackend (),
                                   this.getDataRowBackend ().previewNextRow ()))
      {
        return new PostItemGroupState (this);
      }
    }
    return this;
  }
}
