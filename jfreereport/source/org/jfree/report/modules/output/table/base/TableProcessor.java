/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * -------------------
 * TableProcessor.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: TableProcessor.java,v 1.16 2005/02/04 19:22:57 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 * 23-Jul-2003 : BugFix: Did not use global properties to read the configuration
 */
package org.jfree.report.modules.output.table.base;

import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportEventException;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.report.event.RepaginationListener;
import org.jfree.report.event.RepaginationState;
import org.jfree.report.modules.output.meta.MetaBandProducer;
import org.jfree.report.states.FinishState;
import org.jfree.report.states.ReportState;
import org.jfree.report.states.ReportStateProgress;
import org.jfree.report.states.StartState;

/**
 * The TableProcessor is the abstract base class for all table based output targets.
 * It handles the initialisation of the report writer and starts and manages the
 * report process.
 * <p>
 * Implementing classes should supply a table producer by implementing the createProducer
 * method.
 * <p>
 * Like all other report processors, this implementation is not synchronized.
 *
 * @author Thomas Morgner
 */
public abstract class TableProcessor
{
  /** A compile time constant to define how many events should be fired during the processing. */
  private static final int MAX_EVENTS_PER_RUN = 400;

  /** Disable strict layout by default. */
  public static final String STRICT_LAYOUT_DEFAULT = "false";

  /** The local property name for strict layout. */
  public static final String STRICT_LAYOUT = "StrictLayout";

  /** the function name used for the created tablewriter. */
  private static final String TABLE_WRITER = TableProcessor.class.getName() + "$table-writer";

  /** Literal text for the 'title' property name. */
  public static final String TITLE = "Title";

  /** Literal text for the 'author' property name. */
  public static final String AUTHOR = "Author";


  /** the report that should be processed. */
  private JFreeReport report;

  /** The tablewriter function. */
  private TableWriter tableWriter;
  /** 
   * A flag that controls, whether this processor should monitor the 
   * thread's interrupted state. 
   */
  private boolean handleInterruptedState;

  /** Storage for listener references. */
  private ArrayList listeners;
  /** The listeners as object array for faster access. */
  private Object[] listenersCache;

  private transient LayoutCreator layoutCreator;

  public static final String GLOBAL_LAYOUT = "GlobalLayout";
  public static final String GLOBAL_LAYOUT_DEFAULT = "false";

  /**
   * Creates a new TableProcessor. The TableProcessor creates a private copy
   * of the supplied report.
   *
   * @param report the report that should be processed.
   *
   * @throws ReportProcessingException if the report initialization failed
   */
  public TableProcessor(final JFreeReport report)
      throws ReportProcessingException
  {
    if (report == null)
    {
      throw new NullPointerException();
    }

    try
    {
      this.report = (JFreeReport) report.clone();
    }
    catch (CloneNotSupportedException cne)
    {
      throw new ReportProcessingException("Initial Clone of Report failed");
    }
    tableWriter = new TableWriter(createMetaBandProducer());
    tableWriter.setName(TABLE_WRITER);

    this.report.addExpression(tableWriter);

    // initialize with the report default.
  }

  protected abstract MetaBandProducer createMetaBandProducer();

  /**
   * returns true, if the TableWriter should perform a stricter layout translation.
   * When set to true, all element bounds are used to create the table. This could result
   * in a complex layout, more suitable for printing. If set to false, only the starting
   * bounds (the left and the upper border) are used to create the layout. This will result
   * is lesser cells and rows, the layout will be better suitable for later processing.
   *
   * @return true, if strict layouting rules should be applied, false otherwise.
   */
  public boolean isStrictLayout()
  {
    return report.getReportConfiguration().getConfigProperty
            (getReportConfigurationPrefix() + "." + STRICT_LAYOUT, "false").equals("true");
  }

  /**
   * Defines whether strict layouting rules should be used for the TableLayouter.
   *
   * @param strictLayout set to true, to use strict layouting rules, false otherwise.
   *
   * @see TableProcessor#isStrictLayout
   */
  public void setStrictLayout(final boolean strictLayout)
  {
    report.getReportConfiguration().setConfigProperty
            (getReportConfigurationPrefix() + "." + STRICT_LAYOUT,
                    String.valueOf(strictLayout));
  }

  /**
   * Returns the tablewriter function used in to create the report contents.
   *
   * @return the tablewriter function of the current report.
   */
  protected TableWriter getTableWriter()
  {
    return tableWriter;
  }

  /**
   * Returns the private copy of the used report. The report is initialized for the
   * report writing, so handle this instance with care.
   *
   * @return the local report.
   */
  protected JFreeReport getReport()
  {
    return report;
  }

  /**
   * Processes the entire report and records the state at the end of every page.
   *
   * @return a list of report states (one for the beginning of each page in the report).
   *
   * @throws ReportProcessingException if there was a problem processing the report.
   */
  private ReportState repaginate()
      throws ReportProcessingException
  {
    // apply the configuration ...
    configure();

    try
    {
      final StartState startState = new StartState(getReport());
      ReportState state = startState;
      ReportState retval = null;

      // the report processing can be splitted into 2 separate processes.
      // The first is the ReportPreparation; all function values are resolved and
      // a dummy run is done to calculate the final layout. This dummy run is
      // also necessary to resolve functions which use or depend on the PageCount.

      // the second process is the printing of the report, this is done in the
      // processReport() method.

      // during a prepare run the REPORT_PREPARERUN_PROPERTY is set to true.
      state.setProperty(JFreeReport.REPORT_PREPARERUN_PROPERTY, Boolean.TRUE);

      // the pageformat is added to the report properties, PageFormat is not serializable,
      // so a repaginated report is no longer serializable.
      //
      // The pageformat will cause trouble in later versions, when printing over
      // multiple pages gets implemented. This property will be replaced by a more
      // suitable alternative.
//      final PageFormat p = report.getDefaultPageFormat();
//      state.setProperty(JFreeReport.REPORT_PAGEFORMAT_PROPERTY, p.clone());

      // now change the writer function to be a dummy writer. We don't want any
      // output in the prepare runs.
      final TableWriter w = (TableWriter) state.getDataRow().get(TABLE_WRITER);
      layoutCreator = createLayoutCreator();
      w.setTableCreator(layoutCreator);

      // now process all function levels.
      // there is at least one level defined, as we added the CSVWriter
      // to the report.
      final Iterator it = startState.getLevels();
      if (it.hasNext() == false)
      {
        throw new IllegalStateException("No functions defined, invalid implementation.");
      }

      final int eventTrigger = state.getNumberOfRows() / MAX_EVENTS_PER_RUN;

      boolean hasNext;
      final RepaginationState stateEvent = new RepaginationState(this, 0, 0, 0, 0, false);
      ReportStateProgress progress = null;
      int level = ((Integer) it.next()).intValue();
      // outer loop: process all function levels
      do
      {
        // if the current level is the output-level, then save the report state.
        // The state is used later to restart the report processing.
        if (level == -1)
        {
          retval = (ReportState) state.clone();
        }

        // inner loop: process the complete report, calculate the function values
        // for the current level. Higher level functions are not available in the
        // dataRow.
        int lastRow = -1;
        int eventCount = 0;
        final boolean failOnError
            = (level == -1) && getReport().getReportConfiguration().isStrictErrorHandling();
        while (!state.isFinish())
        {
          checkInterrupted();

          if (lastRow != state.getCurrentDisplayItem())
          {
            lastRow = state.getCurrentDisplayItem();
            if (eventCount == 0)
            {
              stateEvent.reuse(level, state.getCurrentPage(), state.getCurrentDataItem(),
                  state.getNumberOfRows(), true);
              fireStateUpdate(stateEvent);
              eventCount += 1;
            }
            else
            {
              if (eventCount == eventTrigger)
              {
                eventCount = 0;
              }
              else
              {
                eventCount += 1;
              }
            }
          }

          progress = state.createStateProgress(progress);
          state = state.advance();
          if (failOnError)
          {
            if (state.isErrorOccured() == true)
            {
              throw new ReportEventException("Failed to dispatch an event.", state.getErrors());
            }
          }

          if (!state.isFinish())
          {
            // if the report processing is stalled, throw an exception; an infinite
            // loop would be caused.
            if (!state.isProceeding(progress))
            {
              throw new ReportProcessingException("State did not proceed, bailing out!");
            }
          }
        }

        // if there is an other level to process, then use the finish state to
        // create a new start state, which will continue the report processing on
        // the next higher level.
        hasNext = it.hasNext();
        if (hasNext)
        {
          level = ((Integer) it.next()).intValue();
          if (state instanceof FinishState)
          {
            state = new StartState((FinishState) state, level);
          }
          else
          {
            throw new IllegalStateException("Repaginate did not produce an finish state");
          }
        }
      }
      while (hasNext == true);

      state.setProperty(JFreeReport.REPORT_PREPARERUN_PROPERTY, Boolean.FALSE);

      // finally prepeare the returned start state.
      final StartState sretval = (StartState) retval;
      if (sretval == null)
      {
        throw new IllegalStateException("There was no valid pagination done.");
      }
      // reset the state, so that the datarow points to the first row of the tablemodel.
      sretval.resetState();
      return sretval;
    }
//    catch (FunctionInitializeException fne)
//    {
//      throw new ReportProcessingException("Unable to initialize the functions/expressions.", fne);
//    }
    catch (CloneNotSupportedException cne)
    {
      throw new ReportProcessingException("Unable to initialize the report, clone error", cne);
    }
  }

  /**
   * Creates the LayoutProcessor for the current report export. This default
   * implementation simply returns a LayoutCreator instance.
   *
   * @return
   */
  protected LayoutCreator createLayoutCreator()
  {
    return new DefaultLayoutCreator (getReportConfigurationPrefix());
  }

  protected abstract TableCreator createContentCreator();

  /**
   * Returns the previously created LayoutCreator. Calling this method
   * is only valid during the report processing.
   *
   * @return the layout creator
   * @throws IllegalStateException if there is no layout creator.
   */
  protected LayoutCreator getLayoutCreator()
  {
    if (layoutCreator == null)
    {
      throw new IllegalStateException("No layout created detected.");
    }
    return layoutCreator;
  }

  /**
   * Processes the report. The generated output is written using the defined
   * writer, the report is repaginated before the final writing.
   *
   * @throws ReportProcessingException if the report processing failed.
   */
  public synchronized void processReport() throws ReportProcessingException
  {
    ReportState state = repaginate();

    final TableWriter w = (TableWriter) state.getDataRow().get(TABLE_WRITER);
    w.setTableCreator(createContentCreator());

    w.setMaxWidth(StrictGeomUtility.toInternalValue
            (getReport().getPageDefinition().getWidth()));
    final RepaginationState stateEvent = new RepaginationState(this, 0, 0, 0, 0, false);

    final int maxRows = state.getNumberOfRows();
    int lastRow = -1;
    int eventCount = 0;
    final int eventTrigger = maxRows / MAX_EVENTS_PER_RUN;

    final boolean failOnError =
        getReport().getReportConfiguration().isStrictErrorHandling();
    ReportStateProgress progress = null;
    while (!state.isFinish())
    {
      checkInterrupted();

      if (lastRow != state.getCurrentDisplayItem())
      {
        lastRow = state.getCurrentDisplayItem();
        if (eventCount == 0)
        {
          stateEvent.reuse(TableWriter.OUTPUT_LEVEL, state.getCurrentPage(),
              state.getCurrentDataItem(), state.getNumberOfRows(), true);
          fireStateUpdate(stateEvent);
          eventCount += 1;
        }
        else
        {
          if (eventCount == eventTrigger)
          {
            eventCount = 0;
          }
          else
          {
            eventCount += 1;
          }
        }
      }

      progress = state.createStateProgress(progress);
      state = state.advance();
      if (failOnError && state.isErrorOccured() == true)
      {
        throw new ReportEventException("Failed to dispatch an event.", state.getErrors());
      }
      if (!state.isFinish())
      {
        if (!state.isProceeding(progress))
        {
          throw new ReportProcessingException("State did not proceed, bailing out!");
        }
      }
    }
  }

  /**
   * Gets the report configuration prefix for that processor. This prefix defines
   * how to map the property names into the global report configuration.
   *
   * @return the report configuration prefix.
   */
  protected abstract String getReportConfigurationPrefix();

  /**
   * Copies all report configuration properties which match the configuration
   * prefix of this table processor into the property set of this processor.
   */
  protected void configure()
  {
    getTableWriter().setMaxWidth(StrictGeomUtility.toInternalValue
            (getReport().getPageDefinition().getWidth()));
  }

  /**
   * Adds a repagination listener. This listener will be informed of
   * pagination events.
   *
   * @param l  the listener.
   */
  public void addRepaginationListener(final RepaginationListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      listeners = new ArrayList(5);
    }
    listenersCache = null;
    listeners.add(l);
  }

  /**
   * Removes a repagination listener.
   *
   * @param l  the listener.
   */
  public void removeRepaginationListener(final RepaginationListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      return;
    }
    listenersCache = null;
    listeners.remove(l);
  }

  /**
   * Sends a repagination update to all registered listeners.
   *
   * @param state  the state.
   */
  protected void fireStateUpdate(final RepaginationState state)
  {
    if (listeners == null)
    {
      return;
    }
    if (listenersCache == null)
    {
      listenersCache = listeners.toArray();
    }
    for (int i = 0; i < listenersCache.length; i++)
    {
      final RepaginationListener l = (RepaginationListener) listenersCache[i];
      l.repaginationUpdate(state);
    }
  }

  /**
   * Checks, whether the current thread is interrupted.
   *
   * @throws org.jfree.report.ReportInterruptedException if the thread is interrupted to
   * abort the report processing.
   */
  protected void checkInterrupted () throws ReportInterruptedException
  {
    if (isHandleInterruptedState() && Thread.interrupted())
    {
      throw new ReportInterruptedException("Current thread is interrupted. Returning.");
    }
  }


  /**
   * Returns whether the processor should check the threads interrupted state.
   * If this is set to true and the thread was interrupted, then the report processing
   * is aborted.
   *
   * @return true, if the processor should check the current thread state, false otherwise.
   */
  public boolean isHandleInterruptedState()
  {
    return handleInterruptedState;
  }

  /**
   * Defines, whether the processor should check the threads interrupted state.
   * If this is set to true and the thread was interrupted, then the report processing
   * is aborted.
   *
   * @param handleInterruptedState true, if the processor should check the current thread state,
   *                               false otherwise.
   */
  public void setHandleInterruptedState(final boolean handleInterruptedState)
  {
    this.handleInterruptedState = handleInterruptedState;
  }

}
