/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * -----------------
 * CSVProcessor.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVProcessor.java,v 1.6 2003/02/09 18:43:05 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial Version
 * 22-Jan-2003 : The separator is now configurable
 * 04-Feb-2003 : Consistency checks
 * 09-Feb-2003 : Documentation
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 * 
 */

package com.jrefinery.report.targets.csv;

import java.awt.print.PageFormat;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.states.FinishState;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.states.StartState;
import com.jrefinery.report.util.NullOutputStream;

/**
 * The <code>CSVProcessor</code> coordinates the writing process for the raw CSV output.
 * <p>
 * A {@link CSVWriter} is added to the private copy of the report to handle the output process.
 *
 * @author Thomas Morgner
 */
public class CSVProcessor
{
  /** A key for accessing the separator string in the {@link ReportConfiguration}. */
  public static final String CSV_SEPARATOR = "com.jrefinery.report.targets.csv.separator";
  
  /** A key for accessing the 'print data row names' flag in the {@link ReportConfiguration}. */
  public static final String CSV_DATAROWNAME 
      = "com.jrefinery.report.targets.csv.write-datarow-names";

  /** The default name for the csv writer function used by this processor. */
  private static final String CSV_WRITER = "com.jrefinery.report.targets.csv.csv-writer";

  /** The character stream writer to be used by the {@link CSVWriter} function. */
  private Writer writer;
  
  /** The report to be processed. */
  private JFreeReport report;

  /**
   * Creates a new <code>CSVProcessor</code>. The processor will use a comma (",") to separate
   * the column values, unless defined otherwise in the report configuration.
   * The processor creates a private copy of the clone, so that no change to
   * the original report will influence the report processing. DataRow names
   * are not written.
   *
   * @param report  the report to be processed.
   * 
   * @throws ReportProcessingException if the report initialisation failed.
   * @throws FunctionInitializeException if the writer initialisation failed.
   */
  public CSVProcessor(JFreeReport report)
      throws ReportProcessingException, FunctionInitializeException
  {
    this (report, report.getReportConfiguration().getConfigProperty(CSV_SEPARATOR, ","));
  }

  /**
   *
   * Creates a new CSVProcessor. The processor will use the specified separator,
   * the report configuration is not queried for a separator.
   * The processor creates a private copy of the clone, so that no change to
   * the original report will influence the report processing. DataRowNames
   * are not written.
   *
   * @param report the report to be processed.
   * @param separator the separator string to mark column boundaries.
   * 
   * @throws ReportProcessingException if the report initialisation failed.
   * @throws FunctionInitializeException if the writer initialisation failed.
   */
  public CSVProcessor(JFreeReport report, String separator)
      throws ReportProcessingException, FunctionInitializeException
  {
    this (report, separator, false);
  }

  /**
   * Creates a new CSVProcessor. The processor will use the specified separator,
   * the report configuration is not queried for a separator.
   * The processor creates a private copy of the clone, so that no change to
   * the original report will influence the report processing. The first row
   * will contain the datarow names.
   *
   * @param report  the report to be processed.
   * @param separator the separator string to mark column boundaries.
   * @param writeDataRowNames  controls whether or not the data row names are output.
   * 
   * @throws ReportProcessingException if the report initialisation failed.
   * @throws FunctionInitializeException if the writer initialization failed.
   */
  public CSVProcessor(JFreeReport report, String separator, boolean writeDataRowNames)
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

    // Add the writer function as highest priority function to the report.
    // this function is executed as last function, after all other function values
    // have been calculated.
    CSVWriter lm = new CSVWriter();
    lm.setName(CSV_WRITER);
    lm.setSeparator(separator);
    lm.setWriteDataRowNames(writeDataRowNames);
    this.report.addFunction(lm);
  }

  /**
   * Gets the local copy of the report. This report is initialized to handle the
   * report writing, changes to the report can have funny results, so be carefull,
   * when using the report object.
   *
   * @return the local copy of the report.
   */
  protected JFreeReport getReport()
  {
    return report;
  }

  /**
   * Returns the writer used in this Processor.
   *
   * @return the writer
   */
  public Writer getWriter()
  {
    return writer;
  }

  /**
   * Defines the writer which should be used to write the contents of the report.
   *
   * @param writer the writer.
   */
  public void setWriter(Writer writer)
  {
    this.writer = writer;
  }

  /**
   * Processes the entire report and records the state at the end of every page.
   *
   * @return a list of report states (one for the beginning of each page in the report).
   *
   * @throws ReportProcessingException if there was a problem processing the report.
   * @throws CloneNotSupportedException if there is a problem cloning.
   */
  private ReportState repaginate() throws ReportProcessingException, CloneNotSupportedException
  {
    // every report processing starts with an StartState.
    StartState startState = new StartState(getReport());
    ReportState state = startState;
    ReportState retval = null;
    JFreeReport report = state.getReport();

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
    PageFormat p = report.getDefaultPageFormat();
    state.setProperty(JFreeReportConstants.REPORT_PAGEFORMAT_PROPERTY, p.clone());

    // now change the writer function to be a dummy writer. We don't want any
    // output in the prepare runs.
    CSVWriter w = (CSVWriter) state.getDataRow().get(CSV_WRITER);
    w.setWriter(new OutputStreamWriter(new NullOutputStream()));

    // now process all function levels.
    // there is at least one level defined, as we added the CSVWriter
    // to the report.
    Iterator it = startState.getLevels();
    if (it.hasNext() == false)
    {
      throw new IllegalStateException("No functions defined, invalid implementation.");
    }

    boolean hasNext;
    int level = ((Integer) it.next()).intValue();
    // outer loop: process all function levels
    do
    {
      // if the current level is the output-level, then save the report state.
      // The state is used later to restart the report processing.
      if (level == -1)
      {
        retval = state.copyState();
      }

      // inner loop: process the complete report, calculate the function values
      // for the current level. Higher level functions are not available in the
      // dataRow.
      while (!state.isFinish())
      {
        ReportState oldstate = state;
        state = oldstate.copyState().advance();
        if (!state.isFinish())
        {
          // if the report processing is stalled, throw an exception; an infinite
          // loop would be caused.
          if (!state.isProceeding(oldstate))
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
          throw new IllegalStateException("The processing did not produce an finish state");
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
    StartState sretval = (StartState) retval;
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
   * @throws IllegalStateException if there is no writer defined.
   */
  public void processReport() throws ReportProcessingException
  {
    if (writer == null) 
    {
      throw new IllegalStateException("No writer defined");
    }
    try
    {
      ReportState state = repaginate();

      CSVWriter w = (CSVWriter) state.getDataRow().get(CSV_WRITER);
      w.setWriter(getWriter());

      while (!state.isFinish())
      {
        ReportState oldstate = state;
        state = oldstate.copyState().advance();
        if (!state.isFinish())
        {
          if (!state.isProceeding(oldstate))
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
}
