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
 * ----------------
 * ReportState.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morger;
 *
 * $Id: ReportState.java,v 1.33 2002/09/11 20:23:28 taqua Exp $
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
 * 17-May-2002 : Fixed the ReportPropertyBug by adding a new state (PreReportHeader).
 *               ReportState.Start.advance has to be executed before the first page is printed.
 * 26-May-2002 : Moved ReportProperties into the state so that different runs do not affect
 *               each other. Added Property isPrepareRun() to signal whether the report is
 *               currently repaginated.
 * 11-May-2002 : A bug in the ReportPropertyHandling is fixed.
 * 24-Jun-2002 : Populate Elements must not be called before Function values are calculated.
 * 28-Jul-2002 : Added datarow support, the report is now cloned on start
 * 21-Aug-2002 : isProceeding() was buggy, did not test the reportstate correctly (returned always
 *               true)
 * 28-Aug-2002 : Downport to JDK1.2.2, added many this-references for innerclasses ...
 */

package com.jrefinery.report;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportProperties;

import java.util.Date;

/**
 * Captures state information for a report while it is in the process of being displayed or
 * printed.  In most cases, we are interested in the report state at the end of a page, so that
 * we can begin the next page in the right manner.
 *
 * @author DG
 */
public abstract class ReportState implements JFreeReportConstants, Cloneable
{
  /**
   * Initial state for an report. Prints the report header and proceeds to PostProcessHeader-State.
   * <p>
   * alias PreReportHeader<br>
   * advances to PostReportHeader<br>
   * before the print, a reportStarted event gets fired.
   */
  public static class Start extends ReportState
  {
    /**
     * Default constructor and the only constructor to create a state without cloning another.
     *
     * @param report  the report.
     */
    public Start (JFreeReport report)
    {
      super (report);
    }

    /**
     * Advances from the 'start' state to the 'pre-report-header' state.
     * <p>
     * Initialises the 'report.date' property, and fires a 'report-started' event.
     *
     * @param rpc  the report processor.
     *
     * @return the next state ('pre-report-header').
     */
    public ReportState advance (ReportProcessor rpc)
    {
      JFreeReport report = this.getReport ();
      this.setCurrentPage (1);

      // A PropertyHandler should set the properties.
      this.setProperty (JFreeReport.REPORT_DATE_PROPERTY, new Date ());

      // Initialize the report before any band (and especially before the pageheader)
      // is printed.
      ReportEvent event = new ReportEvent (this);
      this.fireReportStartedEvent (event);

      return new PreReportHeader (this);
    }

    /**
     * Returns <code>true</code> because this *is* the start state.
     *
     * @return true
     */
    public boolean isStart ()
    {
      return true;
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

    /**
     * Sets the function collection. The functions get cloned before they
     * are assigned to this state.
     *
     * @param functions  the functions.
     */
    public void setFunctions (FunctionCollection functions)
    {
      if (functions == null)
      {
        throw new NullPointerException ("Empty function collection?");
      }
      super.setFunctions (functions.getCopy ());
    }
  }

  /**
   * Initial state for an report. Prints the report header and proceeds to PostProcessHeader-State.
   * <p>
   * alias PreReportHeader<br>
   * advances to PostReportHeader<br>
   * before the print, a reportStarted event gets fired.
   */
  protected static class PreReportHeader extends ReportState
  {
    /**
     * Default constructor and the only constructor to create a state without cloning another.
     *
     * @param previousState  the previous report state.
     */
    public PreReportHeader (ReportState previousState)
    {
      super (previousState);
    }

    /**
     * Advances from this state to the next.  In the process, the report header is printed.
     *
     * @param rpc  the report processor.
     *
     * @return the next state ('post-report-header').
     */
    public ReportState advance (ReportProcessor rpc)
    {
      JFreeReport report = this.getReport ();

      ReportHeader reportHeader = report.getReportHeader ();
      this.getDataRowConnector ().setDataRowBackend (this.getDataRowBackend ());
      rpc.printReportHeader (reportHeader);
      return new PostReportHeader (this);
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

  /**
   * The report header has been printed. Proceed to the first group.
   */
  protected static class PostReportHeader extends ReportState
  {
    /**
     * Creates a 'post-report-header' state.
     *
     * @param reportstate the previous report state.
     */
    public PostReportHeader (ReportState reportstate)
    {
      super (reportstate);
    }

    /**
     * This state does nothing and advances directly to the first PreGroupHeader.
     *
     * @param rpc  the report processor.
     *
     * @return the next state.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      return new PreGroupHeader (this);
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

  /**
   * Processes an groupheader. If there is not enough space to print the header,
   * no transition is done, else a PostGroupHeader-State gets activated.
   * Before the print, a groupStartEvent gets fired.
   * <p>
   * Before the group is printed, the group is activated and the currentGroup state
   * is adjusted using the enterGroup() function.
   */
  protected static class PreGroupHeader extends ReportState
  {
    /** Flag indicating whether or not the page break has been done. */
    private boolean handledPagebreak;

    /**
     * Creates a new 'pre-group-header' state.
     *
     * @param previous  the previous state.
     */
    public PreGroupHeader (ReportState previous)
    {
      super (previous);
    }

    /**
     * Advances from this state to the next.
     *
     * @param rpc  the report processor.
     *
     * @return  the next state.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      this.enterGroup ();

      Group group = (Group) this.getReport ().getGroup (this.getCurrentGroupIndex ());

      // if there is no header, fire the event and proceed to PostGroupHeader

      GroupHeader header = group.getHeader ();

      if (handledPagebreak == false && header.hasPageBreakBeforePrint ()
                                    && ((this.getCurrentGroupIndex() == 1
                                    && this.getCurrentPage() == 1) == false))
      {
        handledPagebreak = true;
        rpc.setPageDone ();
      }
      else
      {
      // there is a header, test if there is enough space to print it
        if (rpc.isSpaceFor (header))
        {
          // enough space, fire the events and proceed to PostGroupHeader
          ReportEvent event = new ReportEvent (this);
          this.fireGroupStartedEvent (event);
          this.getDataRowConnector ().setDataRowBackend (this.getDataRowBackend ());
          rpc.printGroupHeader (header);
          return new PostGroupHeader (this);
        }
        else
        {
          handledPagebreak = true;
        }
      }

      // Not enough space to print the header. Undo the GroupChange and wait until the
      // pagebreak has been done. After this the engine may return here to attemp another
      // print
      this.leaveGroup ();
      return this;
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

  /**
   * The groupHeader has been printed. If there are more groups defined, activate them
   * and print their header. If no more groups can be activated, start printing the items.
   * Transition: PreGroupHeader or PreItemHeader
   * (this thing changes the currentGroup, but the other behaviour is
   * like StartGroup)
   */
  protected static class PostGroupHeader extends ReportState
  {

    /**
     * Creates a new 'post-group-header' state.
     *
     * @param previous  the previous state.
     */
    public PostGroupHeader (ReportState previous)
    {
      super (previous);
    }

    /**
     * Checks whether there are more groups the work on.
     *
     * @return true, if the currentGroupIndex is smaller than the defined groups - 1
     */
    protected boolean hasMoreGroups ()
    {
      return this.getCurrentGroupIndex () < (this.getReport ().getGroupCount () - 1);
    }

    /**
     * Advances from this state to the next.
     * <p>
     * If there are more groups, activate the next PreGroupHeader state, else activate
     * the PreItemGroup state.
     *
     * @param rpc  the report processor.
     *
     * @return the next state.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      if (hasMoreGroups ())
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
     *
     * @return true.
     */
    public boolean isPrefetchState ()
    {
      return true;
    }
  }

  /**
   * Prepare to print the items. This state fires the itemStarted-Event and advances to
   * the InItemGroup state.
   */
  protected static class PreItemGroup extends ReportState
  {

    /**
     * Creates a new 'pre-item-group' state.
     *
     * @param previous  the previous state.
     */
    public PreItemGroup (ReportState previous)
    {
      super (previous);
    }

    /**
     * Advances to the next state.
     *
     * @param rpc  the report processor.
     *
     * @return the next state.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      // Inform everybody, that now items will be processed.

      ReportEvent event = new ReportEvent (this);
      this.fireItemsStartedEvent (event);
      if (this.getReport().getData().getRowCount() == 0)
      {
        return new PostItemGroup(this);
      }
      return new InItemGroup (this);
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
  protected static class InItemGroup extends ReportState
  {
    /**
     * Creates a new 'in-item-group' state.
     *
     * @param previous  the previous state.
     */
    public InItemGroup (ReportState previous)
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
  protected static class PostItemGroup extends ReportState
  {
    /**
     * Creates a new 'post-item-group' state.
     *
     * @param previous  the previous state.
     */
    public PostItemGroup (ReportState previous)
    {
      super (previous);
    }

    /**
     * Advances from this state to the next.
     * <p>
     * Just inform everybody that the itemband is no longer printed. Next state will be
     * PreGroupFooter.
     *
     * @param rpc  the report processor.
     *
     * @return the next report state.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      ReportEvent event = new ReportEvent (this);
      this.fireItemsFinishedEvent (event);
      return new PreGroupFooter (this);
    }
  }

  /**
   * If there is not enough space to print the footer, the footer returns itself to
   * wait for the pageBreak. After the groupFinished Event has been fired and the footer
   * is printed, the PostGroupFooter gets active.
   */
  protected static class PreGroupFooter extends ReportState
  {
    /**
     * Creates a new 'pre-group-footer' report state.
     *
     * @param previous  the previous report state.
     */
    public PreGroupFooter (ReportState previous)
    {
      super (previous);
    }

    /**
     * Advances from this state to the next.
     *
     * @param rpc  the report processor.
     *
     * @return the next report state.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      Group group = (Group) this.getReport ().getGroup (this.getCurrentGroupIndex ());
      FunctionCollection functions = this.getFunctions ();

      GroupFooter footer = group.getFooter ();
      if (rpc.isSpaceFor (footer))
      {
        // There is a header and enough space to print it. The finishGroup event is
        // fired and PostGroupFooter activated after all work is done.
        ReportEvent event = new ReportEvent (this);
        this.fireGroupFinishedEvent (event);
        this.getDataRowConnector ().setDataRowBackend (this.getDataRowBackend ());
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
  protected static class PostGroupFooter extends ReportState
  {
    /**
     * Creates a new 'post-group-footer' report state.
     *
     * @param previous  the previous report state.
     */
    public PostGroupFooter (ReportState previous)
    {
      super (previous);
    }

    /**
     * Is there a next row to read?
     *
     * @return true, if there is at least one more row to read.
     */
    private boolean hasMoreData ()
    {
      return (this.getCurrentDataItem () < this.getReport ().getData ().getRowCount () - 1);
    }

    /**
     * Are there more groups active?
     *
     * @return true if this is the last group.
     */
    private boolean isLastGroup ()
    {
      return this.getCurrentGroupIndex () == BEFORE_FIRST_GROUP;
    }

    /**
     * Advances from this state to the next.
     *
     * @param rpc  the report processor.
     *
     * @return the next report state.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      // leave the current group and activate the parent group.
      // if this was the last active group, the group index is now BEFORE_FIRST_GROUP
      this.leaveGroup ();

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
          Group group = this.getReport ().getGroup (this.getCurrentGroupIndex ());
          if (group.isLastItemInGroup (this.getDataRowBackend (),
                                       this.getDataRowBackend ().previewNextRow ()))
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
  protected static class PreReportFooter extends ReportState
  {
    /**
     * Creates a 'pre-report-footer' report state.
     *
     * @param previous  the previous report state.
     */
    public PreReportFooter (ReportState previous)
    {
      super (previous);
    }

    /**
     * Advances from this state to the next.
     *
     * @param rpc  the report processor.
     *
     * @return the next report state.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      FunctionCollection functions = this.getFunctions ();
      JFreeReport report = this.getReport ();
      ReportFooter reportFooter = report.getReportFooter ();

      if (rpc.isSpaceFor (reportFooter))
      {
        ReportEvent event = new ReportEvent (this);
        this.fireReportFinishedEvent (event);
        this.getDataRowConnector ().setDataRowBackend (this.getDataRowBackend ());
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
  protected static class Finish extends ReportState
  {
    /**
     * Creates a new 'finish' report state.
     *
     * @param previous  the previous state.
     */
    public Finish (ReportState previous)
    {
      super (previous);
    }

    /**
     * Advances from this state to the next.  Since this is the 'finish' state, this method just
     * returns itself.
     *
     * @param rpc  the report processor.
     *
     * @return this state.
     */
    public ReportState advance (ReportProcessor rpc)
    {
      rpc.setPageDone ();
      return this;
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

  /** The report that the state belongs to. */
  private JFreeReport report;

  /** The current item. */
  private int currentItem;

  /** The page that this state applies to. */
  private int currentPage;

  /** The current group. */
  private int currentGroupIndex;

  /** The functions. */
  private FunctionCollection functions;

  /** The report properties */
  private ReportProperties reportProperties;

  /** The data row. */
  private DataRowBackend dataRow;

  /** The data row connector. */
  private DataRowConnector dataRowConnector;

  /** A row number that is 'before' the first row. */
  public static final int BEFORE_FIRST_ROW = -1;

  /** A group number that is 'before' the first group. */
  public static final int BEFORE_FIRST_GROUP = -1;

  /** The first page. */
  public static final int FIRST_PAGE = 1;

  /**
   * Constructs a ReportState for the specified report.
   *
   * @param reportPar The report.
   */
  protected ReportState (JFreeReport reportPar)
  {
    try
    {
      setReport ((JFreeReport) reportPar.clone());
    }
    catch (CloneNotSupportedException cne)
    {
      Log.debug ("CloneError: ", cne);
      throw new IllegalArgumentException("IllegalReport connected, cloning not supported");
    }
    reportProperties = getReport().getProperties ();

    setCurrentItem (BEFORE_FIRST_ROW);
    setCurrentPage (FIRST_PAGE);
    setCurrentGroupIndex (BEFORE_FIRST_GROUP);

    DataRowConnector dc = new DataRowConnector ();
    dc.connectDataSources (getReport ());

    FunctionCollection functions = getReport().getFunctions().getCopy();
    getReport().setFunctions(functions);
    setFunctions (functions);
    functions.connectDataRow(dc);

    // create a readonly copy of the expression collection used in this report.
    // replace the original expressioncollection in JFreeReport(Clone) with this ReadOnly version
    ExpressionCollection expressions = getReport().getExpressions().getCopy();
    getReport().setExpressions(expressions);
    expressions.connectDataRow(dc);
    setDataRowConnector (dc);

    DataRowBackend dr = new DataRowBackend ();
    dr.setTablemodel (getReport ().getData ());
    dr.setFunctions (getFunctions ());
    dr.setExpressions(getReport().getExpressions());
    dr.setCurrentRow (getCurrentDisplayItem ());
    setDataRowBackend (dr);
  }

  /**
   * Returns the data row connector for the report state.
   * <p>
   * The connector is used as frontend for all datarow users
   *
   * @return the data row connector.
   */
  protected DataRowConnector getDataRowConnector ()
  {
    return dataRowConnector;
  }

  /**
   * Sets the data row connector.
   *
   * @param dataRowConnector  the data row connector.
   */
  private void setDataRowConnector (DataRowConnector dataRowConnector)
  {
    this.dataRowConnector = dataRowConnector;
  }

  /**
   * Returns the current data row.
   *
   * @return  the current data row.
   */
  public DataRow getDataRow ()
  {
    return getDataRowConnector ();
  }

  /**
   * Constructs a ReportState from an existing ReportState.
   *
   * @param clone The existing state.
   */
  protected ReportState (ReportState clone)
  {
    setReport (clone.getReport ());
    reportProperties = clone.reportProperties;
    setCurrentItem (clone.getCurrentDataItem ());
    setCurrentPage (clone.getCurrentPage ());
    setCurrentGroupIndex (clone.getCurrentGroupIndex ());
    setFunctions (clone.getFunctions ());
    setDataRowConnector (clone.getDataRowConnector ());

    setDataRowBackend (clone.getDataRowBackend ());
    getDataRowBackend ().setCurrentRow (getCurrentDisplayItem ());
  }

  /**
   * Returns the data row backend.
   *
   * @return the data row backend.
   */
  protected DataRowBackend getDataRowBackend ()
  {
    return dataRow;
  }

  /**
   * Sets the data row backend.
   *
   * @param dataRow  the data row backend.
   */
  private void setDataRowBackend (DataRowBackend dataRow)
  {
    this.dataRow = dataRow;
  }

  /**
   * Returns the report this state is assigned to.
   *
   * @return the report.
   */
  public JFreeReport getReport ()
  {
    return report;
  }

  /**
   * The advance method performs a transition from the current report state to the next report
   * state.  Each transition will usually involve some processing of the report.
   *
   * @param prc  the report processor.
   *
   * @return the next report state.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  public abstract ReportState advance (ReportProcessor prc) throws ReportProcessingException;

  /**
   * Sets the report for this state.
   *
   * @param report  the report (null not permitted).
   *
   * @throws NullPointerException if the given report is null
   */
  private void setReport (JFreeReport report)
  {
    if (report == null)
    {
      throw new NullPointerException ("A ReportState without a report is not allowed");
    }
    this.report = report;
  }

  /**
   * Returns the current item (that is, the current row of the data in the TableModel).
   *
   * @return The current row index.
   */
  public int getCurrentDataItem ()
  {
    return this.currentItem;
  }

  /**
   * Returns the current item that should be displayed for this state. Before any item
   * has advanced, dataItem is -1. This function automatically corrects the currentRow according
   * to the current state of the report (if the header is processed, add +1).
   *
   * @return the row to use for obtaining data from the TableModel.
   */
  public final int getCurrentDisplayItem ()
  {
    if (isPrefetchState ())
    {
      return this.currentItem + 1;
    }
    else
    {
      return this.currentItem;
    }
  }

  /**
   * Returns the 'prefetch' state for this report state (defaults to false).
   * <p>
   * Some states will override this method and return true...in this case, any access to the
   * report's TableModel will look to the current row + 1.
   *
   * @return always false (subclasses may override).
   */
  public boolean isPrefetchState ()
  {
    return false;
  }

  /**
   * Sets the current item index (corresponds to a row in the TableModel).
   * This element is -1 before a row is read.
   *
   * @param itemIndex The new item index.
   */
  public void setCurrentItem (int itemIndex)
  {
    this.currentItem = itemIndex;
  }

  /**
   * Returns the page number.
   *
   * @return the page number.
   */
  public int getCurrentPage ()
  {
    return this.currentPage;
  }

  /**
   * Sets the current page number.
   *
   * @param page The new page number.
   */
  public void setCurrentPage (int page)
  {
    if (page < 0)
    {
      throw new IllegalArgumentException ("Page must be >= 0");
    }
    this.currentPage = page;
  }

  /**
   * Returns the current group index.
   * <p>
   * This starts at zero for the item group, and increases for each higher level group.
   *
   * @return The current group index.
   */
  public int getCurrentGroupIndex ()
  {
    return currentGroupIndex;
  }

  /**
   * Sets the current group index (zero is the item group).
   *
   * @param index The new group index.
   */
  public void setCurrentGroupIndex (int index)
  {
    if (index < -1)
    {
      throw new IllegalArgumentException ("GroupIndex must be >= 0 or BEFORE_FIRST_GROUP");
    }
    this.currentGroupIndex = index;
  }

  /**
   * Returns the function collection.
   *
   * @return the functions.
   */
  public final FunctionCollection getFunctions ()
  {
    return this.functions;
  }

  /**
   * Sets the function collection. The functions no longer get cloned before they
   * are assigned to this state.
   *
   * @param functions  the functions.
   */
  public void setFunctions (FunctionCollection functions)
  {
    if (functions == null)
    {
      throw new NullPointerException ("Empty function collection?");
    }
    this.functions = functions;
  }

  /**
   * Returns the value of a property with the specified name.
   *
   * @param key  the property name.
   *
   * @return the property value.
   */
  public Object getProperty (String key)
  {
    return reportProperties.get (key);
  }

  /**
   * Returns a property with the specified name.  If no property with the specified name is found,
   * returns def.
   *
   * @param key  the property name.
   * @param def  the default value.
   *
   * @return the property value.
   */
  public Object getProperty (String key, Object def)
  {
    return reportProperties.get (key, def);
  }

  /**
   * Sets a property.
   *
   * @param key  the property name.
   * @param o  the property value.
   */
  public void setProperty (String key, Object o)
  {
    reportProperties.put (key, o);
  }

  /**
   * Returns the report properties.
   *
   * @return the report properties.
   */
  public ReportProperties getProperties ()
  {
    return reportProperties;
  }

  /**
   * Returns a flag indicating whether this is the 'prepare' run.
   * <p>
   * This run is used to do repagination, and is only done when the pageformat changes.
   *
   * @return true, if this is a prepare run of the report engine.
   */
  public boolean isPrepareRun ()
  {
    Boolean bool = (Boolean) getProperty (JFreeReportConstants.REPORT_PREPARERUN_PROPERTY,
                                          Boolean.FALSE);
    return bool.booleanValue ();
  }

  /**
   * Clones the report state.
   *
   * @return a clone.
   */
  public Object clone ()
  {
    try
    {
      ReportState result = (ReportState) super.clone ();
      FunctionCollection functions = getFunctions ().getCopy ();
      result.setFunctions (functions);
      result.dataRow = (DataRowBackend) dataRow.clone ();
      result.dataRow.setFunctions (functions);
      return result;
    }
    catch (CloneNotSupportedException cne)
    {
      return null;
    }
  }


  /**
   * This is a helper function used to detect infinite loops on report
   * processing. Returns true, if the report did proceed over at least one element.
   * <p>
   * If this method returns false, we need to bail out of processing the report because there is
   * some problem.
   *
   * @param oldstate the previous state.
   *
   * @return true if some progress has been made, false otherwise.
   */
  public boolean isProceeding (ReportState oldstate)
  {
    if (getCurrentGroupIndex () != oldstate.getCurrentGroupIndex ())
    {
      return true;
    }
    if (getCurrentDataItem () > oldstate.getCurrentDataItem ())
    {
      return true;
    }
    if (getCurrentPage () != oldstate.getCurrentPage ()
        && this.getClass().equals(oldstate.getClass()))
            ///*|| (oldstate.getClass ().equals (getClass ()))*/)
    {
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
   *
   * @return false (subclasses may override).
   */
  public boolean isStart ()
  {
    return false;
  }

  /**
   * Tests whether this state is a defined ending point for report generation.
   *
   * @return false (subclasses may override).
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
    setCurrentGroupIndex (getCurrentGroupIndex () + 1);
  }

  /**
   * Deactivate the current group.
   */
  public void leaveGroup ()
  {
    setCurrentGroupIndex (getCurrentGroupIndex () - 1);
  }

  /**
   * Advances the active data row to the next line.
   */
  public void advanceItem ()
  {
    setCurrentItem (getCurrentDataItem () + 1);
    getDataRowBackend ().setCurrentRow (getCurrentDisplayItem ());
  }

  /**
   * Fires a 'report-started' event.
   *
   * @param event the report event.
   */
  public void fireReportStartedEvent (ReportEvent event)
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.reportStarted (event);
    getReport().getExpressions().reportStarted(event);
  }

  /**
   * Fires a 'report-finished' event.
   *
   * @param event the report event.
   */
  public void fireReportFinishedEvent (ReportEvent event)
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.reportFinished (event);
    getReport().getExpressions().reportFinished(event);
  }

  /**
   * Fires a 'page-started' event.
   *
   * @param event the report event.
   */
  public void firePageStartedEvent (ReportEvent event)
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.pageStarted (event);
    getReport().getExpressions().pageStarted(event);
  }

  /**
   * Fires a 'page-finished' event.
   *
   * @param event the report event.
   */
  public void firePageFinishedEvent (ReportEvent event)
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.pageFinished (event);
    getReport().getExpressions().pageFinished(event);
  }

  /**
   * Fires a 'group-started' event.
   *
   * @param event the report event.
   */
  public void fireGroupStartedEvent (ReportEvent event)
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.groupStarted (event);
    getReport().getExpressions().groupStarted(event);
  }

  /**
   * Fires a 'group-finished' event.
   *
   * @param event the report event.
   */
  public void fireGroupFinishedEvent (ReportEvent event)
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.groupFinished (event);
    getReport().getExpressions().groupFinished(event);
  }

  /**
   * Fires an 'items-started' event.
   *
   * @param event the report event.
   */
  public void fireItemsStartedEvent (ReportEvent event)
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.itemsStarted (event);
    getReport().getExpressions().itemsStarted(event);
  }

  /**
   * Fires an 'items-finished' event.
   *
   * @param event the report event.
   */
  public void fireItemsFinishedEvent (ReportEvent event)
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.itemsFinished (event);
    getReport().getExpressions().itemsFinished(event);
  }

  /**
   * Fires an 'items-advanced' event.
   *
   * @param event the report event.
   */
  public void fireItemsAdvancedEvent (ReportEvent event)
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.itemsAdvanced (event);
    getReport().getExpressions().itemsAdvanced(event);
  }

}
