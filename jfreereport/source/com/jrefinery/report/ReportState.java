/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * ReportState.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morger;
 *
 * $Id: ReportState.java,v 1.1.1.1 2002/04/25 17:02:18 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 19-Feb-2002 : Moved constants into the ReportStateConstants interface (DG);
 * 18-Apr-2002 : Added detection whether a report did proceed.
 * 10-May-2002 : ReportState is now smarter. All state transitions formerly handled by
 *               JFreeReport.advanceState has moved into the corresponding ReportState-class.
 *               ReportEvents are used to inform listeners, a ReportProcessor encapsulates the
 *               knowledge how to display the elements.
 */

package com.jrefinery.report;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.event.ReportListener;
import com.jrefinery.report.util.Log;

import javax.swing.table.TableModel;
import java.util.Date;
import java.util.Vector;

/**
 * Captures state information for a report while it is in the process of being displayed or
 * printed.  In most cases, we are interested in the report state at the end of a page, so that
 * we can begin the next page in the right manner.
 */
public abstract class ReportState implements JFreeReportConstants, ReportStateConstants, Cloneable
{
  /**
   * Initial state for an report. Prints the report header and proceeds to PostProcessHeader-State.<p>
   * alias PreReportHeader<br>
   * advances to PostReportHeader<br>
   * before the print, a reportStarted event gets fired.
   */
  public static class Start extends ReportState
  {
    /**
     * Default constructor and the only constructor to create a state without cloning another.
     */
    public Start (JFreeReport report)
    {
      super (report);
    }

    /**
     * The report started. Print the report header.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      JFreeReport report = getReport ();
      setCurrentPage (1);

      // PropertyHandler should set the properties.
      report.setProperty (JFreeReport.REPORT_DATE_PROPERTY, new Date ());

      ReportEvent event = new ReportEvent (report, this);
      fireReportStartedEvent (event);

      ReportHeader reportHeader = report.getReportHeader ();
      reportHeader.populateElements (this);
      rpc.printReportHeader (reportHeader);
      return new PostReportHeader (this);
    }

    /**
     * Tests whether is state is a start-state.
     *
     * @return always true
     */
    public boolean isStart ()
    {
      return true;
    }

    /**
     * Returns the corrected display item for this state. As the currentItem has not yet advanced
     * we perform a readAHead lookup when populating elements.
     */
    public int getCurrentDisplayItem ()
    {
      return getCurrentDataItem () + 1;
    }
  }

  /**
   * The report header has been printed. Proceed to the first group.
   */
  protected class PostReportHeader extends ReportState
  {
    public PostReportHeader (ReportState reportstate)
    {
      super (reportstate);
    }

    /**
     * This state does nothing and advances directly to the first PreGroupHeader.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      //return new GroupStart (this);
      return new PreGroupHeader (this);
    }

    /**
     * Returns the corrected display item for this state. As the currentItem has not yet advanced
     * we perform a readAHead lookup when populating elements.
     */
    public int getCurrentDisplayItem ()
    {
      return getCurrentDataItem () + 1;
    }
  }

  /**
   * Processes an groupheader. If there is not enough space to print the header,
   * no transition is done, else a PostGroupHeader-State gets activated.
   * Before the print, a groupStartEvent gets fired.
   * <p>
   * Before the group is printed, the group is activated and the currentGroup state
   * is adjusted using the enterGroup() function.
   */
  protected class PreGroupHeader extends ReportState
  {
    private boolean handledPagebreak;

    public PreGroupHeader (ReportState reportstate)
    {
      super (reportstate);
    }

    public ReportState advance (ReportProcessor rpc)
    {
      enterGroup();

      Group group = (Group) report.getGroup (getCurrentGroupIndex ());

      // if there is no header, fire the event and proceed to PostGroupHeader
      GroupHeader header = group.getHeader ();

      if (handledPagebreak == false &&
          header.hasPageBreakBeforePrint() &&
          getCurrentDataItem() != BEFORE_FIRST_ROW)
      {
        handledPagebreak = true;
        rpc.setPageDone();
      }
      else
      // there is a header, test if there is enough space to print it
      if (rpc.isSpaceFor (header))
      {
        // enough space, fire the events and proceed to PostGroupHeader
        header.populateElements (this);
        ReportEvent event = new ReportEvent (report, this);
        fireGroupStartedEvent (event);
        rpc.printGroupHeader (header);
        return new PostGroupHeader (this);
      }

      // Not enough space to print the header. Undo the GroupChange and wait until the
      // pagebreak has been done. After this the engine may return here to attemp another
      // print
      leaveGroup ();
      return this;
    }

    /**
     * Returns the corrected display item for this state. As the currentItem has not yet advanced
     * we perform a readAHead lookup when populating elements.
     */
    public int getCurrentDisplayItem ()
    {
      return getCurrentDataItem () + 1;
    }
  }

  /**
   * The groupHeader has been printed. If there are more groups defined, activate them
   * and print their header. If no more groups can be activated, start printing the items.
   * Transition: PreGroupHeader or PreItemHeader
   * (this thing changes the currentGroup, but the other behaviour is
   * like StartGroup)
   */
  protected class PostGroupHeader extends ReportState
  {
    public PostGroupHeader (ReportState reportstate)
    {
      super (reportstate);
    }

    /**
     * Checks whether there are more groups the work on.
     *
     * @returns true, if the currentGroupIndex is smaller than the defined groups - 1
     */
    protected boolean hasMoreGroups ()
    {
      return getCurrentGroupIndex () < (getReport ().getGroupCount () - 1);
    }

    /**
     * If there are more groups, activate the next PreGroupHeader state, else activate
     * the PreItemGroup state.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      if (hasMoreGroups())
      {
        // There are more groups defined.
        // Activate the next group and proceed to print it's header.
        return new PreGroupHeader (this);
      }
      else
      {
        // Prepare to print Items.
        return new PreItemGroup (this);
      }
    }

    /**
     * Returns the corrected display item for this state. As the currentItem has not yet advanced
     * we perform a readAHead lookup when populating elements.
     */
    public int getCurrentDisplayItem ()
    {
      return getCurrentDataItem () + 1;
    }
  }

  /**
   * Prepare to print the items. This state fires the itemStarted-Event and advances to
   * the InItemGroup state.
   */
  protected class PreItemGroup extends ReportState
  {
    public PreItemGroup (ReportState reportstate)
    {
      super (reportstate);
    }

    public ReportState advance (ReportProcessor rpc)
    {
      // Inform everybody, that now items will be processed.
      ReportEvent event = new ReportEvent (report, this);
      fireItemsStartedEvent (event);
      return new InItemGroup (this);
    }

    /**
     * Returns the corrected display item for this state. As the currentItem has not yet advanced
     * we perform a readAHead lookup when populating elements.
     */
    public int getCurrentDisplayItem ()
    {
      return getCurrentDataItem () + 1;
    }
  }

  /**
   * Prints the itemBand. Before the band is printed, the items are advanced and the next
   * data row gets activated. Before any row has been read, the currentItem state is BEFORE_FIRST_ITEM,
   * comparable to ResultSet.isBeforeFirst () in java.sql.ResultSet.
   * After the item has advanced and before the band is printed, the elements are populated and an
   * itemsAdvanced-Event is fired.
   * <p>
   * If the activated Item is the last item in its group, the next state will be an PostItemGroupHeader.
   * In the other case, the current state remains this ItemsAdvanced state.
   */
  protected class InItemGroup extends ReportState
  {
    public InItemGroup (ReportState reportstate)
    {
      super (reportstate);
    }

    public ReportState advance (ReportProcessor rpc)
    {
      JFreeReport report = getReport ();
      ItemBand itemBand = report.getItemBand ();

      // If there is enough space to print the itemband, advance the items, populate
      // the band and print it. If there was not enough space, the engine will return
      // here after the pagebreak.
      if (rpc.isSpaceFor (itemBand))
      {
        advanceItem();

        int currItem = getCurrentDataItem ();
        int currGroup = getCurrentGroupIndex ();

        itemBand.populateElements (this);
        ReportEvent event = new ReportEvent (report, this);
        fireItemsAdvancedEvent (event);

        rpc.printItemBand (itemBand);

        if (report.isLastItemInHigherGroups (currItem, currGroup))
        {
          return new PostItemGroup (this);
        }
      }
      return this;
    }
  }

  /**
   * The only purpose for this state is to fire the itemsFinished event. After that task is done,
   * a PreGroupFooter-State gets active.
   */
  protected class PostItemGroup extends ReportState
  {
    public PostItemGroup (ReportState reportstate)
    {
      super (reportstate);
    }

    /**
     * Just inform everybody that the itemband is no longer printed. Next state will be
     * PreGroupFooter.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      ReportEvent event = new ReportEvent (report, this);
      fireItemsFinishedEvent (event);
      return new PreGroupFooter (this);
    }
  }

  /**
   * If there is not enough space to print the footer, the footer returns itself to
   * wait for the pageBreak. After the groupFinished Event has been fired and the footer
   * is printed, the PostGroupFooter gets active.
   */
  protected class PreGroupFooter extends ReportState
  {
    public PreGroupFooter (ReportState reportstate)
    {
      super (reportstate);
    }

    public ReportState advance (ReportProcessor rpc)
    {
      Group group = (Group) report.getGroup (getCurrentGroupIndex ());
      FunctionCollection functions = getFunctions ();

      GroupFooter footer = group.getFooter ();
      if (rpc.isSpaceFor (footer))
      {
        // There is a header and enough space to print it. The finishGroup event is
        // fired and PostGroupFooter activated after all work is done.
        footer.populateElements (this);
        ReportEvent event = new ReportEvent (report, this);
        fireGroupFinishedEvent (event);

        rpc.printGroupFooter (footer);
        return new PostGroupFooter (this);
      }
      else
      {
        // There is not enough space to print the footer. Wait for the pageBreak and
        // return later.
        return this;
      }
    }
  }

  /**
   * In this state the active group is closed. After that the next state gets activated:
   * <p>
   * If there is no more data and no more open groups, finish the report and activate the
   * PreReportFooter state. If there is no more data but there are open groups, close them by
   * activating the next PreGroupFooter state.
   * <p>
   * If there is more data, check whether there are any open groups. If there is no parent group
   * or the parent group is not finished, open the next sub group by activating PreGroupHeader.
   * If there is a parent group and this parent is finished, close the parent by activating
   * the PreGroupFooter state.
   */
  protected class PostGroupFooter extends ReportState
  {
    public PostGroupFooter (ReportState reportstate)
    {
      super (reportstate);
    }

    // is there a next row to read?
    private boolean hasMoreData ()
    {
      return (getCurrentDataItem () < getReport ().getData ().getRowCount () - 1);
    }

    /**
     * Are there more groups active?
     */
    private boolean isLastGroup ()
    {
      return getCurrentGroupIndex() == BEFORE_FIRST_GROUP;
    }

    public ReportState advance (ReportProcessor rpc)
    {
      // leave the current group and activate the parent group.
      // if this was the last active group, the group index is now BEFORE_FIRST_GROUP
      leaveGroup();

      if (isLastGroup ())
      {
        // group finished, but there is more data - start a new group...
        if (hasMoreData ())
        {
          return new PreGroupHeader (this);
        }
        else
        {
          return new PreReportFooter (this);
        }
      }
      else
      {
        // There are more groups active
        if (hasMoreData ())
        {
          // we have more data to work on
          // If the group is done, print the GroupFooter of the parent
          Group group = report.getGroup (getCurrentGroupIndex());
          if (group.isLastItemInGroup (report.getData (), getCurrentDataItem ()))
          {
            // Parent is finished, print the footer
            return new PreGroupFooter (this);
          }
          else
          {
            // more data in parent group, print the next header
            return new PreGroupHeader (this);
          }
        }
        else
        {
          // no more data, print the footer of the parent group
          return new PreGroupFooter (this);
        }
      }
    }
  }

  /**
   * At least the report has been finished. There is no more data to print, so just print
   * the reportHeader and advance to the state Finish. Before printing the header fire the
   * reportFinished event.
   */
  protected class PreReportFooter extends ReportState
  {
    public PreReportFooter (ReportState reportstate)
    {
      super (reportstate);
    }

    public ReportState advance (ReportProcessor rpc)
    {
      FunctionCollection functions = getFunctions ();
      JFreeReport report = getReport ();
      ReportFooter reportFooter = report.getReportFooter ();

      if (rpc.isSpaceFor (reportFooter))
      {
        reportFooter.populateElements (this);
        ReportEvent event = new ReportEvent (report, this);
        fireReportFinishedEvent (event);
        rpc.printReportFooter (reportFooter);
        return new Finish (this);
      }
      else
      {
        return this;
      }
    }
  }

  /**
   * The report is done. No advance will be done, every call to advance will return this
   * Finish-State.
   */
  protected class Finish extends ReportState
  {
    public Finish (ReportState reportstate)
    {
      super (reportstate);
    }

    public ReportState advance (ReportProcessor rpc)
    {
      rpc.setPageDone ();
      return this;
    }

    /**
     * @returns true, as this report is done and will no longer advance.
     */
    public boolean isFinish ()
    {
      return true;
    }

  }

  /** The report that the state belongs to. */
  private JFreeReport report;

  /** The current item. */
  private int currentItem;

  /** The page that this state applies to. */
  private int _currentPage;

  /** The current group. */
  private int currentGroupIndex;

  /** The functions. */
  private FunctionCollection functions;

  /** Band with an pending page break */
  private Band pband;
  private Vector reportListeners;

  public abstract ReportState advance (ReportProcessor prc);
  public static final int BEFORE_FIRST_ROW = -1;
  public static final int BEFORE_FIRST_GROUP = -1;
  public static final int FIRST_PAGE = 1;

  /**
   * Constructs a ReportState for the specified report.
   *
   * @param report The report.
   */
  protected ReportState (JFreeReport report)
  {
    Log.debug (this.getClass().getName ());
    reportListeners = new Vector ();
    setReport (report);
    setCurrentItem (BEFORE_FIRST_ROW);
    setCurrentPage (FIRST_PAGE);
    setCurrentGroupIndex (BEFORE_FIRST_GROUP);
    setFunctions (report.getFunctions ());
  }

  /**
   * Constructs a ReportState from an existing ReportState.
   *
   * @param clone The existing state.
   */
  protected ReportState (ReportState clone)
  {
    Log.debug (this.getClass().getName ());
    reportListeners = new Vector ();
    setReport (clone.getReport ());
    setCurrentItem (clone.getCurrentDataItem ());
    setCurrentPage (clone.getCurrentPage ());
    setCurrentGroupIndex (clone.getCurrentGroupIndex ());
    setFunctions (clone.getFunctions ());
  }

  /**
   * Returns the report this state is assigned to.
   */
  public JFreeReport getReport ()
  {
    return report;
  }

  /**
   * Adds an reportListener to this state. ReportListeners are not cloned. This is an
   * internal function in the current project state.
   */
  public void addReportListener (ReportListener l)
  {
    reportListeners.add (l);
  }

  /**
   * removes an reportListener to this state. ReportListeners are not cloned. This is an
   * internal function in the current project state.
   */
  public void removeReportListener (ReportListener l)
  {
    reportListeners.remove (l);
  }

  /**
   * defines the report for this state. if the report is null, a NullPointerException is thrown.
   *
   * @param report the report for this state
   * @throws NullPointerException if the given report is null
   */
  private void setReport (JFreeReport report)
  {
    if (report == null)
    {
      throw new NullPointerException ("An State without an report is not allowed");
    }
    this.report = report;
  }

  /**
   * Returns the current item (that is, the current row of the data in the TableModel).
   * @return The current item index (corresponds to a row in the TableModel).
   */
  public int getCurrentDataItem ()
  {
    return this.currentItem;
  }

  /**
   * Returns the current item that sould be displayed for this state. Before any item
   * has advanced, dataItem is -1. This function automaticly corrects the currentRow according
   * to the current state of the report (if the header is processed, add +1).
   *
   */
  public int getCurrentDisplayItem ()
  {
    return this.currentItem;
  }

  /**
   * Sets the current item index (corresponds to a row in the TableModel).
   * This element is -1 before a row is read.
   * @param itemIndex The new item index.
   */
  public void setCurrentItem (int itemIndex)
  {
    this.currentItem = itemIndex;
  }

  /**
   * Returns the current page.
   * @return The page that this state refers to.
   */
  public int getCurrentPage ()
  {
    return _currentPage;
  }

  /**
   * Sets the current page.
   * @param page The new page number.
   */
  public void setCurrentPage (int page)
  {
    if (page < 0)
      throw new IllegalArgumentException ("Page must be >= 0");
    _currentPage = page;
  }

  /**
   * Returns the current group index.
   * @return The current group index.
   */
  public int getCurrentGroupIndex ()
  {
    return currentGroupIndex;
  }

  /**
   * Sets the current group index (zero is the item group).
   * @param index The new group index.
   */
  public void setCurrentGroupIndex (int index)
  {
    if (index < -1)
      throw new IllegalArgumentException ("GroupIndex must be >= 0 or BEFORE_FIRST_GROUP");
    this.currentGroupIndex = index;
  }

  /**
   * Returns the function collection.
   */
  public FunctionCollection getFunctions ()
  {
    return this.functions;
  }

  /**
   * Sets the function collection. The functions get cloned before they
   * are assigned to this state.
   */
  public void setFunctions (FunctionCollection pfunctions)
  {
    if (pfunctions == null)
    {
      throw new NullPointerException ("Empty function collection?");
    }
    else
    {
      functions = (FunctionCollection) pfunctions.clone ();
      addReportListener(functions);
    }
  }

  /**
   * Clones the report state.
   */
  public Object clone ()
  {

    ReportState result = null;

    try
    {
      result = (ReportState) super.clone ();
      result.setFunctions (getFunctions ());
    }
    catch (CloneNotSupportedException e)
    {
      // this should never happen...
      System.err.println ("ReportState: clone not supported");
    }
    return result;
  }

  /**
   * This is a helper function used to detect infinite loops on report
   * processing. Returns true, if the report did proceed over at least one
   * element.
   */
  public boolean isProceeding (ReportState oldstate)
  {
    if ((getCurrentGroupIndex () != oldstate.getCurrentGroupIndex ()) ||
            (getCurrentDataItem () >= oldstate.getCurrentDataItem ()) ||
            (getCurrentPage () != oldstate.getCurrentPage ()) ||
            (pband != oldstate.pband) ||
            (oldstate.getClass ().equals (getClass ())))
    {
      return true;
    }
    return false;
  }

  /**
   * Activate an pagebreak on next printing for this Band. This is
   * a helper function used to detect infinite loops on report processing.
   */
  public void setPendingPageBreak (Band band)
  {
    pband = band;
  }

  /**
   * This is a helper function used to detect infinite loops on report
   * processing. Returns true, if the report will do a page break on the
   * next band processing. This is
   * a helper function used to detect infinite loops on report processing.
   */
  public boolean isPendingPageBreak (Band band)
  {
    if (pband == band)
    {
      pband = null;
      return true;
    }
    return false;
  }

  /**
   * Advances the current page by one.
   */
  public void nextPage ()
  {
    setCurrentPage (getCurrentPage () + 1);
  }

  /**
   * Tests whether this state is a defined starting point for report generation.
   */
  public boolean isStart ()
  {
    return false;
  }

  /**
   * Tests whether this state is a defined ending point for report generation.
   */
  public boolean isFinish ()
  {
    return false;
  }

  /**
   * Activate the next group.
   */
  public void enterGroup ()
  {
    setCurrentGroupIndex (getCurrentGroupIndex() + 1);
  }

  /**
   * Deactivate the current group.
   */
  public void leaveGroup ()
  {
    setCurrentGroupIndex (getCurrentGroupIndex() - 1);
  }

  /**
   * Advances the active data row to the next line.
   */
  public void advanceItem ()
  {
    setCurrentItem(getCurrentDataItem() + 1);
  }

  public void fireReportStartedEvent (ReportEvent event)
  {
    for (int i = 0; i < reportListeners.size (); i++)
    {
      ReportListener l = (ReportListener) reportListeners.elementAt (i);
      l.reportStarted (event);
    }
  }

  public void fireReportFinishedEvent (ReportEvent event)
  {
    for (int i = 0; i < reportListeners.size (); i++)
    {
      ReportListener l = (ReportListener) reportListeners.elementAt (i);
      l.reportFinished (event);
    }
  }

  public void firePageStartedEvent (ReportEvent event)
  {
    for (int i = 0; i < reportListeners.size (); i++)
    {
      ReportListener l = (ReportListener) reportListeners.elementAt (i);
      l.pageStarted (event);
    }
  }

  public void firePageFinishedEvent (ReportEvent event)
  {
    for (int i = 0; i < reportListeners.size (); i++)
    {
      ReportListener l = (ReportListener) reportListeners.elementAt (i);
      l.pageFinished (event);
    }
  }

  public void fireGroupStartedEvent (ReportEvent event)
  {
    for (int i = 0; i < reportListeners.size (); i++)
    {
      ReportListener l = (ReportListener) reportListeners.elementAt (i);
      l.groupStarted (event);
    }
  }

  public void fireGroupFinishedEvent (ReportEvent event)
  {
    for (int i = 0; i < reportListeners.size (); i++)
    {
      ReportListener l = (ReportListener) reportListeners.elementAt (i);
      l.groupFinished (event);
    }
  }

  public void fireItemsStartedEvent (ReportEvent event)
  {
    for (int i = 0; i < reportListeners.size (); i++)
    {
      ReportListener l = (ReportListener) reportListeners.elementAt (i);
      l.itemsStarted (event);
    }
  }

  public void fireItemsFinishedEvent (ReportEvent event)
  {
    for (int i = 0; i < reportListeners.size (); i++)
    {
      ReportListener l = (ReportListener) reportListeners.elementAt (i);
      l.itemsFinished (event);
    }
  }

  public void fireItemsAdvancedEvent (ReportEvent event)
  {
    for (int i = 0; i < reportListeners.size (); i++)
    {
      ReportListener l = (ReportListener) reportListeners.elementAt (i);
      l.itemsAdvanced (event);
    }
  }

}
