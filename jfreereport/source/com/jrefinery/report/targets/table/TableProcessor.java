/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * -------------------
 * TableProcessor.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableProcessor.java,v 1.18 2003/06/27 14:25:24 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */
package com.jrefinery.report.targets.table;

import java.awt.print.PageFormat;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.ReportEventException;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.states.FinishState;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.states.ReportStateProgress;
import com.jrefinery.report.states.StartState;
import com.jrefinery.report.util.ReportConfiguration;

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
  /** the function name used for the created tablewriter. */
  private static final String TABLE_WRITER = TableProcessor.class.getName() + "$table-writer";

  /** the report that should be processed. */
  private JFreeReport report;

  /** the strict layout flag. */
  private boolean strictLayout;

  /** Storage for the output target properties. */
  private Properties properties;

  /** The tablewriter function. */
  private TableWriter tableWriter;

  /**
   * Creates a new TableProcessor. The TableProcessor creates a private copy
   * of the supplied report.
   *
   * @param report the report that should be processed.
   *
   * @throws ReportProcessingException if the report initialization failed
   * @throws FunctionInitializeException if the table writer initialization failed.
   */
  public TableProcessor(final JFreeReport report)
      throws ReportProcessingException, FunctionInitializeException
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

    properties = new Properties();

    tableWriter = new TableWriter();
    tableWriter.setName(TABLE_WRITER);
    this.report.addFunction(tableWriter);

    // initialize with the report default.
    strictLayout = report.getReportConfiguration().isStrictTableLayout();
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
   * Returns the strict layout flag.
   *
   * @see TableGrid#isStrict
   * @return true, if a stricter layouting algorithm should be used, false otherwise.
   */
  public boolean isStrictLayout()
  {
    return strictLayout;
  }

  /**
   * Defines the strict layout flag.
   *
   * @see TableGrid#isStrict
   * @param strictLayout true, if a stricter layouting algorithm should be used, false otherwise.
   */
  public void setStrictLayout(final boolean strictLayout)
  {
    this.strictLayout = strictLayout;
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
   * @throws CloneNotSupportedException if there is a cloning problem.
   */
  private ReportState repaginate() throws ReportProcessingException, CloneNotSupportedException
  {
    // apply the configuration ...
    configure();

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
    state.setProperty(JFreeReportConstants.REPORT_PREPARERUN_PROPERTY, Boolean.TRUE);

    // the pageformat is added to the report properties, PageFormat is not serializable,
    // so a repaginated report is no longer serializable.
    //
    // The pageformat will cause trouble in later versions, when printing over
    // multiple pages gets implemented. This property will be replaced by a more
    // suitable alternative.
    final PageFormat p = report.getDefaultPageFormat();
    state.setProperty(JFreeReportConstants.REPORT_PAGEFORMAT_PROPERTY, p.clone());

    // now change the writer function to be a dummy writer. We don't want any
    // output in the prepare runs.
    final TableWriter w = (TableWriter) state.getDataRow().get(TABLE_WRITER);
    w.setProducer(createProducer(true));
    w.getProducer().configure(getProperties());

    // now process all function levels.
    // there is at least one level defined, as we added the CSVWriter
    // to the report.
    final Iterator it = startState.getLevels();
    if (it.hasNext() == false)
    {
      throw new IllegalStateException("No functions defined, invalid implementation.");
    }

    boolean hasNext;
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
      final boolean failOnError
          = (level == -1) && getReport().getReportConfiguration().isStrictErrorHandling();
      while (!state.isFinish())
      {
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
    } while (hasNext == true);

    // root of evilness here ... pagecount should not be handled specially ...
    // The pagecount should not be added as report property, there are functions to
    // do this.
    /*
    state.setProperty(JFreeReportConstants.REPORT_PAGECOUNT_PROPERTY,
                      new Integer(state.getCurrentPage() - 1));
    */
    state.setProperty(JFreeReportConstants.REPORT_PREPARERUN_PROPERTY, Boolean.FALSE);

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

  /**
   * Processes the report. The generated output is written using the defined
   * writer, the report is repaginated before the final writing.
   *
   * @throws ReportProcessingException if the report processing failed.
   */
  public void processReport() throws ReportProcessingException
  {
    try
    {
      ReportState state = repaginate();

      final TableWriter w = (TableWriter) state.getDataRow().get(TABLE_WRITER);
      w.setProducer(createProducer(false));
      w.getProducer().configure(getProperties());

      w.setMaxWidth((float) getReport().getDefaultPageFormat().getImageableWidth());

      final boolean failOnError =
          getReport().getReportConfiguration().isStrictErrorHandling();
      ReportStateProgress progress = null;
      while (!state.isFinish())
      {
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
    catch (CloneNotSupportedException cne)
    {
      throw new ReportProcessingException("StateCopy was not supported");
    }
  }

  /**
   * Creates a TableProducer. The TableProducer is responsible to create the table.
   *
   * @param dummy true, if dummy mode is enabled, and no writing should be done, false otherwise.
   * @return the created table producer, never null.
   */
  protected abstract TableProducer createProducer(boolean dummy);

  /**
   * Defines a property for this output target. Properties are the standard way of configuring
   * an output target.
   *
   * @param property  the name of the property to set (<code>null</code> not permitted).
   * @param value  the value of the property.  If the value is <code>null</code>, the property is
   * removed from the output target.
   */
  public void setProperty(final String property, final Object value)
  {
    if (property == null)
    {
      throw new NullPointerException();
    }

    if (value == null)
    {
      properties.remove(property);
    }
    else
    {
      properties.put(property, value);
    }
  }

  /**
   * Queries the property named with <code>property</code>. If the property is not found, <code>
   * null</code> is returned.
   *
   * @param property the name of the property to be queried
   *
   * @return the value stored under the given property name
   *
   * @throws java.lang.NullPointerException if <code>property</code> is null
   */
  public Object getProperty(final String property)
  {
    return getProperty(property, null);
  }

  /**
   * Queries the property named with <code>property</code>. If the property is not found, the
   * default value is returned.
   *
   * @param property the name of the property to be queried
   * @param defaultValue the defaultvalue returned if there is no such property
   *
   * @return the value stored under the given property name
   *
   * @throws NullPointerException if <code>property</code> is null
   */
  public Object getProperty(final String property, final Object defaultValue)
  {
    if (property == null)
    {
      throw new NullPointerException();
    }

    final Object retval = properties.get(property);
    if (retval == null)
    {
      return defaultValue;
    }
    return retval;
  }

  /**
   * Returns an enumeration of the property names.
   *
   * @return the enumeration.
   */
  protected Iterator getPropertyNames()
  {
    return properties.keySet().iterator();
  }

  /**
   * Gets the internal properties storage.
   *
   * @return the internal properties storage.
   */
  protected Properties getProperties()
  {
    return properties;
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
    final ReportConfiguration rc = getReport().getReportConfiguration();
    final Enumeration enum = rc.getConfigProperties();
    final String prefix = getReportConfigurationPrefix();

    while (enum.hasMoreElements())
    {
      final String key = (String) enum.nextElement();
      if (key.startsWith(prefix) == false)
      {
        continue;
      }
      final String propKey = key.substring(prefix.length());
      if (getProperties().containsKey(propKey))
      {
        continue;
      }
      final Object value = rc.getConfigProperty(key);
      setProperty(propKey, value);
    }

    getTableWriter().setMaxWidth((float) getReport().getDefaultPageFormat().getImageableWidth());
  }
}
