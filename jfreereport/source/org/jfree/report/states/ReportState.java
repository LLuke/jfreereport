/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ReportState.java
 * ----------------
 * (C)opyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ReportState.java,v 1.6 2003/11/07 15:31:40 taqua Exp $
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
 * 17-May-2002 : Fixed the ReportPropertyBug by adding a new state (PreReportHeaderState).
 *               ReportState.StartState.advance has to be executed before the first page is printed.
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

package org.jfree.report.states;

import java.util.Iterator;
import java.util.List;

import org.jfree.report.Band;
import org.jfree.report.DataRow;
import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.LayoutEvent;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.LevelledExpressionList;
import org.jfree.report.util.ReportProperties;
import org.jfree.report.util.ReportPropertiesList;
import org.jfree.util.ObjectUtils;

/**
 * Captures state information for a report while it is in the process of being displayed or
 * printed.  JFreeReport uses a state transition diagram to track progress through the report
 * generation.
 * <p>
 * The report processing will usually pass through many states for each page generated.  We
 * record the report state at the end of each page, so that we can jump directly to the start of
 * any page without having to regenerate all the preceding pages.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public abstract class ReportState implements Cloneable
{
  /** The report that the state belongs to. */
  private ReportDefinitionImpl report;

  /** The number of rows in the tablemodel. */
  private final int numberOfRows;

  /** The current item (row in the TableModel). */
  private int currentItem;

  /** The page that this state applies to. */
  private int currentPage;

  /** The current group. */
  private int currentGroupIndex;

  /** The report properties. */
  private final ReportProperties reportProperties;

  /** The data row. */
  private DataRowBackend dataRow;

  /** The data row preview. */
  private DataRowPreview dataRowPreview;

  /** A row number that is 'before' the first row. */
  public static final int BEFORE_FIRST_ROW = -1;

  /** A group number that is 'before' the first group. */
  public static final int BEFORE_FIRST_GROUP = -1;

  /** The first page. */
  public static final int BEFORE_FIRST_PAGE = 0;

  /** The ancestor hash code. */
  private int ancestorHashcode;

  /**
   * Constructs a new state for the specified report.  The report (which encapsulates a reference
   * to the data, plus definitions for the required layout) is cloned to isolate the reporting
   * engine from changes to the report that occur after report processing begins.
   * <p>
   * This constructor is protected, it is intended to be used by subclasses only.
   *
   * @param reportPar  the report.
   * @throws CloneNotSupportedException if the initial cloning of the report definition fails.
   */
  protected ReportState(final JFreeReport reportPar) throws CloneNotSupportedException
  {
    setReportDefinition(new ReportDefinitionImpl(reportPar));
    numberOfRows = reportPar.getData().getRowCount();
    reportProperties = getReport().getProperties();

    final LevelledExpressionList functions = new LevelledExpressionList(reportPar.getExpressions(),
        reportPar.getFunctions());

    final DataRowBackend dr = new DataRowBackend();
    dr.setTablemodel(reportPar.getData());
    dr.setFunctions(functions);
    dr.setReportProperties(new ReportPropertiesList(reportProperties));
    getReportDefinition().getDataRowConnector().setDataRowBackend(dr);
    this.dataRow = dr;

    // we have no clone-ancestor, so forget everyting
    setAncestorHashcode(this.hashCode());

    // reset the current data row ...
    resetState();
  }

  /**
   * Resets the state, so that the datarow points to the first row.
   */
  protected void resetState()
  {
    setCurrentItem(BEFORE_FIRST_ROW);
    setCurrentPage(BEFORE_FIRST_PAGE);
    setCurrentGroupIndex(BEFORE_FIRST_GROUP);
    getDataRowBackend().setCurrentRow(getCurrentDisplayItem());
  }

  /**
   * Constructs a ReportState from an existing ReportState and optionally resets
   * the state.
   *
   * @param clone the base report state from which to initialize.
   * @param reset true, if the state should be reseted, false otherwise.
   */
  protected ReportState(final ReportState clone, final boolean reset)
  {
    setReportDefinition(clone.getReportDefinition());
    reportProperties = clone.reportProperties;
    numberOfRows = clone.getNumberOfRows();
    dataRowPreview = clone.dataRowPreview;
    this.dataRow = clone.getDataRowBackend();

    if (reset)
    {
      resetState();
    }
    else
    {
      setCurrentItem(clone.getCurrentDataItem());
      setCurrentPage(clone.getCurrentPage());
      setCurrentGroupIndex(clone.getCurrentGroupIndex());
      getDataRowBackend().setCurrentRow(getCurrentDisplayItem());
    }
    // we have no clone-ancestor, so forget everything
    setAncestorHashcode(this.hashCode());
  }

  /**
   * Constructs a ReportState from an existing ReportState.
   *
   * @param clone  the existing state.
   */
  protected ReportState(final ReportState clone)
  {
    this(clone, false);
  }

  /**
   * Implements a singleton datarow preview.
   * 
   * @return the preview instance of the datarow.
   */
  protected DataRowPreview getDataRowPreview()
  {
    if (dataRowPreview == null)
    {
      dataRowPreview = new DataRowPreview(getDataRowBackend());
    }
    dataRowPreview.update(getDataRowBackend());
    return dataRowPreview;
  }

  /**
   * Returns the number of rows.
   *
   * @return The number of row.
   */
  public int getNumberOfRows()
  {
    return numberOfRows;
  }

  /**
   * Returns the current data row.
   *
   * @return  the current data row.
   */
  public DataRow getDataRow()
  {
    return dataRow.getDataRow();
  }

  /**
   * Returns the data row backend.
   *
   * @return the data row backend.
   */
  protected DataRowBackend getDataRowBackend()
  {
    return dataRow;
  }

  /**
   * Returns the report this state is assigned to.
   *
   * @return the report.
   */
  public ReportDefinition getReport()
  {
    return report;
  }

  /**
   * The advance method performs a transition from the current report state to the next report
   * state.  Each transition will usually involve some processing of the report.
   *
   * @return the next report state.
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  public abstract ReportState advance() throws ReportProcessingException;

  /**
   * Sets the report for this state.
   *
   * @param report  the report (null not permitted).
   *
   * @throws NullPointerException if the given report is null
   */
  private void setReportDefinition(final ReportDefinitionImpl report)
  {
    if (report == null)
    {
      throw new NullPointerException("A ReportState without a report is not allowed");
    }
    this.report = report;
  }

  /**
   * Returns the report definition implementation used as backend when generating
   * the report. Don't play with it!
   * 
   * @return the internal report definition.
   */
  protected ReportDefinitionImpl getReportDefinition()
  {
    return this.report;
  }

  /**
   * Returns the current item (that is, the current row of the data in the TableModel).
   *
   * @return The current row index.
   */
  public int getCurrentDataItem()
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
  public final int getCurrentDisplayItem()
  {
    if (isPrefetchState())
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
  public boolean isPrefetchState()
  {
    return false;
  }

  /**
   * Sets the current item index (corresponds to a row in the TableModel).
   * This element is -1 before a row is read.
   *
   * @param itemIndex The new item index.
   */
  public void setCurrentItem(final int itemIndex)
  {
    this.currentItem = itemIndex;
  }

  /**
   * Returns the page number.
   *
   * @return the page number.
   */
  public int getCurrentPage()
  {
    return this.currentPage;
  }

  /**
   * Sets the current page number.
   *
   * @param page The new page number.
   */
  public void setCurrentPage(final int page)
  {
    if (page < 0)
    {
      throw new IllegalArgumentException("Page must be >= 0");
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
  public int getCurrentGroupIndex()
  {
    return currentGroupIndex;
  }

  /**
   * Sets the current group index (zero is the item group).
   *
   * @param index The new group index.
   */
  public void setCurrentGroupIndex(final int index)
  {
    if (index < -1)
    {
      throw new IllegalArgumentException("GroupIndex must be >= 0 or BEFORE_FIRST_GROUP");
    }
    this.currentGroupIndex = index;
  }

  /**
   * Returns the function collection.
   *
   * @return the functions.
   */
  protected final LevelledExpressionList getFunctions()
  {
    if (dataRow.getFunctions().getDataRow() != dataRow.getDataRow())
    {
      throw new IllegalStateException("Paranoia: Unconnected or invalid function datarow");
    }
    return dataRow.getFunctions();
  }

  /**
   * Returns the value of a property with the specified name.
   *
   * @param key  the property name.
   *
   * @return the property value.
   */
  public Object getProperty(final String key)
  {
    return reportProperties.get(key);
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
  public Object getProperty(final String key, final Object def)
  {
    return reportProperties.get(key, def);
  }

  /**
   * Sets a property.
   *
   * @param key  the property name.
   * @param o  the property value.
   */
  public void setProperty(final String key, final Object o)
  {
    reportProperties.put(key, o);
  }

  /**
   * Returns the report properties.
   *
   * @return the report properties.
   */
  public ReportProperties getProperties()
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
  public boolean isPrepareRun()
  {
    final Boolean bool = (Boolean) getProperty(JFreeReport.REPORT_PREPARERUN_PROPERTY,
        Boolean.FALSE);
    return bool.booleanValue();
  }

  /**
   * Creates a shallow clone. Handle with care. This is a relativly cheap operation,
   * so we have a copy to check whether some progress was made, without having to
   * pay for the complete (deep) cloning.
   * <p>
   * Don't use that function to store/copy a report state for a longer storage time.
   * The next few advances may render the reportstate copy invalid.
   *
   * @param progress  a carrier for the result.
   *
   * @return a progress object of this state.
   */
  public ReportStateProgress createStateProgress(ReportStateProgress progress)
  {
    if (progress == null)
    {
      progress = new ReportStateProgress();
    }
    progress.setCurrentDataItem(getCurrentDataItem());
    progress.setCurrentGroupIndex(getCurrentGroupIndex());
    progress.setCurrentPage(getCurrentPage());
    progress.setStateClass(this.getClass());
    return progress;
  }

  /**
   * Clones the report state.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException if there is a cloning problem.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final ReportState result = (ReportState) super.clone();
    result.report = (ReportDefinitionImpl) report.clone();
    result.dataRow = (DataRowBackend) dataRow.clone();
    result.report.getDataRowConnector().setDataRowBackend(result.dataRow);
    return result;
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
  public boolean isProceeding(final ReportStateProgress oldstate)
  {
    // a state is proceeding if it changed its group
    if (getCurrentGroupIndex() != oldstate.getCurrentGroupIndex())
    {
      return true;
    }
    // a state is proceeding if it changed the current row in the datamodel
    if (getCurrentDataItem() > oldstate.getCurrentDataItem())
    {
      return true;
    }
    // a state proceeds if it is an other class than the old state
    if (this.getClass().equals(oldstate.getStateClass()) == false)
    {
//      Log.debug (new StateProceedMessage(this, oldstate,
//                                         "State did proceed: In Group: "));
      return true;
    }
//    Log.debug (new StateProceedMessage(this, oldstate,
//                                       "State did not proceed: In Group: "));
    return false;
  }

//  /**
//   * LogHelper. The Message is created when the toString() method is called.
//   * If logging is disabled, no toString() gets called and no resources are wasted.
//   */
//  private static class StateProceedMessage
//  {
//    /** The current state. */
//    private ReportState currentState;
//
//    /** The old state. */
//    private ReportStateProgress oldState;
//
//    /** The message. */
//    private String message;
//
//    /**
//     * Creates a new message.
//     *
//     * @param currentState  the current state.
//     * @param oldState  the old state.
//     * @param message  the message.
//     */
//    public StateProceedMessage(final ReportState currentState, final ReportStateProgress oldState,
//                               final String message)
//    {
//      this.currentState = currentState;
//      this.oldState = oldState;
//      this.message = message;
//    }
//
//    /**
//     * Returns a string representation of the object.
//     *
//     * @return the string.
//     */
//    public String toString()
//    {
//      return message + currentState.getCurrentGroupIndex() + ", DataItem: "
//          + currentState.getCurrentDataItem() + ",Page: "
//          + currentState.getCurrentPage() + " Class: "
//          + currentState.getClass() + "\n"
//          + "Old State: " + oldState.getCurrentGroupIndex() + ", DataItem: "
//          + oldState.getCurrentDataItem() + ",Page: "
//          + oldState.getCurrentPage() + " Class: "
//          + oldState.getClass() + "\n";
//    }
//  }

  /**
   * Advances the current page by one.
   */
  public void nextPage()
  {
    setCurrentPage(getCurrentPage() + 1);
  }

  /**
   * Tests whether this state is a defined starting point for report generation.
   *
   * @return false (subclasses may override).
   */
  public boolean isStart()
  {
    return false;
  }

  /**
   * Tests whether this state is a defined ending point for report generation.
   *
   * @return false (subclasses may override).
   */
  public boolean isFinish()
  {
    return false;
  }

  /**
   * Activates the next group by incrementing the current group index.  The outer-most group is
   * given an index of zero, and this increases for each subgroup that is defined.
   */
  public void enterGroup()
  {
    setCurrentGroupIndex(getCurrentGroupIndex() + 1);
  }

  /**
   * Deactivates the current group by decrementing the current group index.
   */
  public void leaveGroup()
  {
    setCurrentGroupIndex(getCurrentGroupIndex() - 1);
  }

  /**
   * Advances the active data row to the next line.
   */
  public void advanceItem()
  {
    setCurrentItem(getCurrentDataItem() + 1);
    getDataRowBackend().setCurrentRow(getCurrentDisplayItem());
  }

  /**
   * Fires a 'report-started' event.
   */
  public void fireReportInitializedEvent()
  {
    getFunctions().reportInitialized(new ReportEvent(this, ReportEvent.REPORT_INITIALIZED));
  }

  /**
   * Fires a 'report-started' event.
   */
  public void fireReportStartedEvent()
  {
    getFunctions().reportStarted(new ReportEvent(this, ReportEvent.REPORT_STARTED));
  }

  /**
   * Fires a 'prepare' event.
   *
   * @param type the event type of the event that should be prepared.
   */
  public void firePrepareEvent(final int type)
  {
    getFunctions().firePrepareEvent(new ReportEvent(this, (ReportEvent.PREPARE_EVENT | type)));
  }

  /**
   * Fires a 'report-finished' event.
   */
  public void fireReportFinishedEvent()
  {
    getFunctions().reportFinished(new ReportEvent(this, ReportEvent.REPORT_FINISHED));
  }

  /**
   * Fires a 'report-finished' event.
   */
  public void fireReportDoneEvent()
  {
    getFunctions().reportDone(new ReportEvent(this, ReportEvent.REPORT_DONE));
  }

  /**
   * Fires a 'page-started' event.
   *
   * @param baseEvent the type of the base event which caused the page start to be
   * triggered.
   */
  public void firePageStartedEvent(final int baseEvent)
  {
    getFunctions().pageStarted(new ReportEvent(this, ReportEvent.PAGE_STARTED | baseEvent));
  }

  /**
   * Fires a '<code>page-finished</code>' event.  The <code>pageFinished(...)</code> method is
   * called for every report function.
   */
  public void firePageFinishedEvent()
  {
    getFunctions().pageFinished(new ReportEvent(this, ReportEvent.PAGE_FINISHED));
  }

  /**
   * Fires a '<code>page-canceled</code>' event.  The <code>pageCanceled(...)</code> method is
   * called for every report function that implements the PageListener interface.
   */
  public void firePageCanceledEvent()
  {
    getFunctions().pageCanceled(new ReportEvent(this, ReportEvent.PAGE_CANCELED));
  }

  /**
   * Fires a '<code>group-started</code>' event.  The <code>groupStarted(...)</code> method is
   * called for every report function.
   */
  public void fireGroupStartedEvent()
  {
    getFunctions().groupStarted(new ReportEvent(this, ReportEvent.GROUP_STARTED));
  }

  /**
   * Fires a 'group-finished' event.
   */
  public void fireGroupFinishedEvent()
  {
    getFunctions().groupFinished(new ReportEvent(this, ReportEvent.GROUP_FINISHED));
  }

  /**
   * Fires an 'items-started' event.
   */
  public void fireItemsStartedEvent()
  {
    getFunctions().itemsStarted(new ReportEvent(this, ReportEvent.ITEMS_STARTED));
  }

  /**
   * Fires an 'items-finished' event.
   */
  public void fireItemsFinishedEvent()
  {
    getFunctions().itemsFinished(new ReportEvent(this, ReportEvent.ITEMS_FINISHED));
  }

  /**
   * Fires an 'items-advanced' event.
   */
  public void fireItemsAdvancedEvent()
  {
    getFunctions().itemsAdvanced(new ReportEvent(this, ReportEvent.ITEMS_ADVANCED));
  }

  /**
   * Fires an 'layout-complete' event.
   *
   * @param band the band, that completed layouting.
   * @param type the type of the base event fireing the laout complete event.
   */
  public void fireLayoutCompleteEvent(final Band band, final int type)
  {
    getFunctions().layoutComplete(new LayoutEvent(this, band, LayoutEvent.LAYOUT_EVENT | type));
  }

  /**
   * Returns the current function level.
   *
   * @return the function level.
   */
  public int getLevel()
  {
    return getFunctions().getLevel();
  }

  /**
   * Returns an iterator over the function levels.
   *
   * @return an iterator.
   */
  public Iterator getLevels()
  {
    return getFunctions().getLevelsDescending();
  }

  /**
   * Returns the ancestor hash code.
   *
   * @return the ancestor hash code.
   */
  public int getAncestorHashcode()
  {
    return ancestorHashcode;
  }

  /**
   * Sets the ancestor hash code.
   *
   * @param ancestorHashcode  the ancestor hash code.
   */
  protected void setAncestorHashcode(final int ancestorHashcode)
  {
    this.ancestorHashcode = ancestorHashcode;
  }

  /**
   * Returns <code>true</code> if a state is an ancestor of this state, and <code>false</code>
   * otherwise.
   *
   * @param state  the state.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean isAncestor(final ReportState state)
  {
    return (state.getAncestorHashcode() == getAncestorHashcode());
  }

  /**
   * Returns the errors that occured during the last event dispatching. This list
   * gets cleared when the next event gets dispatched.
   *
   * @return the error list.
   */
  public List getErrors()
  {
    return getFunctions().getErrors();
  }

  /**
   * Checks, whether the last report event caused errors.
   *
   * @return true, if there were errors recorded, false otherwise.
   */
  public boolean isErrorOccured()
  {
    return getFunctions().hasErrors();
  }

  /**
   * Returns true if this is the last item in the group, and false otherwise.
   *
   * @param g the group that should be checked.
   * @param currentDataRow  the current data row.
   * @param nextDataRow   the next data row, or null, if this is the last datarow.
   *
   * @return A flag indicating whether or not the current item is the last in its group.
   */
  public static boolean isLastItemInGroup
      (final Group g, final DataRowBackend currentDataRow, final DataRowBackend nextDataRow)
  {
    // return true if this is the last row in the model.
    if (currentDataRow.isLastRow() || nextDataRow == null)
    {
      return true;
    }
    else
    {
      // compare item and item+1, if any field differs, then item==last in group
      final String[] fieldsCached = g.getFieldsArray();
      for (int i = 0; i < fieldsCached.length; i++)
      {
        final String field = fieldsCached[i];
        final int column1 = currentDataRow.findColumn(field);
        if (column1 == -1)
        {
//          Log.debug ("Unable to find column in base dataset: " + field);
          continue;
        }
        final int column2 = nextDataRow.findColumn(field);
        if (column2 == -1)
        {
//          Log.debug ("Unable to find column in next dataset: " + field);
          continue;
        }

        final Object item1 = currentDataRow.get(column1);
        final Object item2 = nextDataRow.get(column2);
        if (ObjectUtils.equal(item1, item2) == false)
        {
          return true;
        }
      }
      return false;
    }
  }
}
