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
 * ----------------
 * ReportState.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ReportState.java,v 1.20 2003/02/08 19:32:06 taqua Exp $
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

package com.jrefinery.report.states;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.DataRowBackend;
import com.jrefinery.report.DataRowConnector;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.LevelledExpressionList;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportProperties;
import com.jrefinery.report.util.ReportPropertiesList;

import java.util.Iterator;

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
public abstract class ReportState implements JFreeReportConstants, Cloneable
{
  /** The report that the state belongs to. */
  private JFreeReport report;

  /** The current item (row in the TableModel). */
  private int currentItem;

  /** The page that this state applies to. */
  private int currentPage;

  /** The current group. */
  private int currentGroupIndex;

  /** The report properties */
  private ReportProperties reportProperties;

  /** The data row. */
  private DataRowBackend dataRow;

  /** The data row connector. */
  private DataRowConnector dataRowConnector;

  /** The functions. */
  private LevelledExpressionList functions;

  /** A row number that is 'before' the first row. */
  public static final int BEFORE_FIRST_ROW = -1;

  /** A group number that is 'before' the first group. */
  public static final int BEFORE_FIRST_GROUP = -1;

  /** The first page. */
  public static final int FIRST_PAGE = 1;

  /** The ancestor hash code. */
  private int ancestorHashcode;

  /**
   * Constructs a ReportState for the specified report.
   * <p>
   * This constructor is protected, it is intended to be used by subclasses only.
   *
   * @param reportPar  the report.
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

    DataRowConnector dc = new DataRowConnector ();
    DataRowConnector.connectDataSources (getReport (), dc);
    setDataRowConnector(dc);

    LevelledExpressionList functions = new LevelledExpressionList(getReport().getExpressions(),
                                                                getReport().getFunctions());
    setFunctions (functions);
    functions.connectDataRow(dc);

    DataRowBackend dr = new DataRowBackend ();
    dr.setTablemodel (getReport ().getData ());
    dr.setFunctions (getFunctions ());
    dr.setCurrentRow (getCurrentDisplayItem ());
    dr.setReportProperties(new ReportPropertiesList(reportProperties));
    setDataRowBackend (dr);

    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());

    // we have no clone-ancestor, so forget everyting
    setAncestorHashcode(this.hashCode());

    resetState();
  }

  /**
   * Resets the state.
   */
  protected void resetState ()
  {
    setCurrentItem (BEFORE_FIRST_ROW);
    setCurrentPage (FIRST_PAGE);
    setCurrentGroupIndex (BEFORE_FIRST_GROUP);
    getDataRowBackend().setCurrentRow(getCurrentDisplayItem());
  }

  /**
   * Constructs a ReportState from an existing ReportState.
   *
   * @param clone  the existing state.
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
    // we have no clone-ancestor, so forget everything
    setAncestorHashcode(this.hashCode());
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
   * @return the next report state.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  public abstract ReportState advance () throws ReportProcessingException;

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
  protected final LevelledExpressionList getFunctions ()
  {
    return this.functions;
  }

  /**
   * Sets the function collection. The functions no longer get cloned before they
   * are assigned to this state.
   *
   * @param functions  the functions.
   */
  protected void setFunctions (LevelledExpressionList functions)
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
   * Creates a shallow clone. Handle with care.
   *
   * @return a shallow clone of this state.
   * @throws CloneNotSupportedException
   */
  public ReportState copyState () throws CloneNotSupportedException
  {
    return (ReportState) super.clone();
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
      LevelledExpressionList functions = (LevelledExpressionList) getFunctions ().clone();
      result.setFunctions (functions);
      result.report = (JFreeReport) report.clone();
      result.dataRow = (DataRowBackend) dataRow.clone ();
      result.dataRow.setFunctions (functions);
      result.dataRowConnector = new DataRowConnector();
      // disconnect the old datarow from all clones
      DataRowConnector.disconnectDataSources(result.report, dataRowConnector);
      functions.disconnectDataRow(dataRowConnector);
      // and connect connect the new datarow to them
      DataRowConnector.connectDataSources(result.report, result.dataRowConnector);
      functions.connectDataRow(result.dataRowConnector);
      result.dataRowConnector.setDataRowBackend(result.getDataRowBackend());
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
    // a state is proceeding if it changed its group
    if (getCurrentGroupIndex () != oldstate.getCurrentGroupIndex ())
    {
      return true;
    }
    // a state is proceeding if it changed the current row in the datamodel
    if (getCurrentDataItem () > oldstate.getCurrentDataItem ())
    {
      return true;
    }
    // a state proceeds if it is an other class than the old state
    if (this.getClass().equals(oldstate.getClass()) == false)
    {
      /*
      Log.debug (new StateProceedMessage(this, oldstate,
                                         "State did proceed: In Group: "));
                                         */
      return true;
    }
/*
    Log.debug (new StateProceedMessage(this, oldstate,
                                       "State did not proceed: In Group: "));
*/                                       
    return false;
  }

  /**
   * LogHelper. The Message is created when the toString() method is called.
   * If logging is disabled, no toString() gets called and no resources are wasted.
   */
  private static class StateProceedMessage
  {
    /** The current state. */
    private ReportState currentState;

    /** The old state. */
    private ReportState oldState;

    /** The message. */
    private String message;

    /**
     * Creates a new message.
     *
     * @param currentState  the current state.
     * @param oldState  the old state.
     * @param message  the message.
     */
    public StateProceedMessage(ReportState currentState, ReportState oldState, String message)
    {
      this.currentState = currentState;
      this.oldState = oldState;
      this.message = message;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return the string.
     */
    public String toString ()
    {
      return message + currentState.getCurrentGroupIndex() + ", DataItem: "
                     + currentState.getCurrentDataItem() + ",Page: "
                     + currentState.getCurrentPage() + " Class: "
                     + currentState.getClass() + "\n" +
          "Old State: " + oldState.getCurrentGroupIndex() + ", DataItem: "
                     + oldState.getCurrentDataItem() + ",Page: "
                     + oldState.getCurrentPage() + " Class: "
                     + oldState.getClass() + "\n";
    }
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
   * Activates the next group by incrementing the current group index.  The outer-most group is
   * given an index of zero, and this increases for each subgroup that is defined.
   */
  public void enterGroup ()
  {
    setCurrentGroupIndex (getCurrentGroupIndex () + 1);
  }

  /**
   * Deactivates the current group by decrementing the current group index.
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
   */
  public void fireReportStartedEvent ()
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.reportStarted (new ReportEvent(this));
  }

  /**
   * Fires a 'report-finished' event.
   */
  public void fireReportFinishedEvent ()
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.reportFinished (new ReportEvent(this));
  }

  /**
   * Fires a 'page-started' event.
   */
  public void firePageStartedEvent ()
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.pageStarted (new ReportEvent(this));
  }

  /**
   * Fires a '<code>page-finished</code>' event.  The <code>pageFinished(...)</code> method is
   * called for every report function.
   */
  public void firePageFinishedEvent ()
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.pageFinished (new ReportEvent(this));
  }

  /**
   * Fires a '<code>group-started</code>' event.  The <code>groupStarted(...)</code> method is
   * called for every report function.
   */
  public void fireGroupStartedEvent ()
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.groupStarted (new ReportEvent(this));
  }

  /**
   * Fires a 'group-finished' event.
   */
  public void fireGroupFinishedEvent ()
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.groupFinished (new ReportEvent(this));
  }

  /**
   * Fires an 'items-started' event.
   */
  public void fireItemsStartedEvent ()
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.itemsStarted (new ReportEvent(this));
  }

  /**
   * Fires an 'items-finished' event.
   */
  public void fireItemsFinishedEvent ()
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    this.functions.itemsFinished (new ReportEvent(this));
  }

  /**
   * Fires an 'items-advanced' event.
   */
  public void fireItemsAdvancedEvent ()
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    functions.itemsAdvanced (new ReportEvent(this));
  }

  /**
   * Fires an 'layout-complete' event.
   */
  public void fireLayoutCompleteEvent()
  {
    getDataRowConnector ().setDataRowBackend (getDataRowBackend ());
    functions.layoutComplete(new ReportEvent(this));
  }

  /**
   * Returns the current function level.
   *
   * @return the function level.
   */
  public int getLevel ()
  {
    return getFunctions().getLevel();
  }

  /**
   * Returns an iterator over the function levels.
   *
   * @return an iterator.
   */
  public Iterator getLevels ()
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
  protected void setAncestorHashcode(int ancestorHashcode)
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
  public boolean isAncestor (ReportState state)
  {
    return (state.getAncestorHashcode() == getAncestorHashcode());
  }

}
