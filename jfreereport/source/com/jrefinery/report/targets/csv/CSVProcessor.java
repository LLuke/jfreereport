/**
 * Date: Jan 18, 2003
 * Time: 8:59:32 PM
 *
 * $Id: CSVProcessor.java,v 1.3 2003/02/02 23:43:51 taqua Exp $
 */
package com.jrefinery.report.targets.csv;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.states.FinishState;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.states.StartState;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.NullOutputStream;

import java.awt.print.PageFormat;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

public class CSVProcessor
{
  public static final String CSV_SEPARATOR = "com.jrefinery.report.targets.csv.separator";
  private static final String CSV_WRITER = "csv-writer";

  private Writer writer;
  private JFreeReport report;

  public CSVProcessor(JFreeReport report)
      throws ReportProcessingException, FunctionInitializeException
  {
    this (report, report.getReportConfiguration().getConfigProperty(CSV_SEPARATOR, ","));
  }

  public CSVProcessor(JFreeReport report, String separator)
      throws ReportProcessingException, FunctionInitializeException
  {
    if (report == null) throw new NullPointerException();
    try
    {
      this.report = (JFreeReport) report.clone();
    }
    catch (CloneNotSupportedException cne)
    {
      throw new ReportProcessingException("Initial Clone of Report failed");
    }

    CSVWriter lm = new CSVWriter();
    lm.setName(CSV_WRITER);
    lm.setSeparator(separator);
    this.report.addFunction(lm);
  }

  public JFreeReport getReport()
  {
    return report;
  }

  public Writer getWriter()
  {
    return writer;
  }

  public void setWriter(Writer writer)
  {
    this.writer = writer;
  }

  /**
   * Processes the entire report and records the state at the end of every page.
   *
   * @return a list of report states (one for the beginning of each page in the report).
   *
   * @throws com.jrefinery.report.ReportProcessingException if there was a problem processing the report.
   */
  private ReportState repaginate() throws ReportProcessingException, CloneNotSupportedException
  {
    StartState startState = new StartState(getReport());
    ReportState state = startState;
    ReportState retval = null;

    // PrepareRuns, part 1: resolve the function dependencies by running the report
    // until all function levels are completed.
    JFreeReport report = state.getReport();

    // all prepare runs have this property set, test details with getLevel()
    state.setProperty(JFreeReportConstants.REPORT_PREPARERUN_PROPERTY, Boolean.TRUE);
    PageFormat p = report.getDefaultPageFormat();
    state.setProperty(JFreeReportConstants.REPORT_PAGEFORMAT_PROPERTY, p.clone());

    // now set a dummy writer into the xml-processor.
    CSVWriter w = (CSVWriter) state.getDataRow().get(CSV_WRITER);
    w.setWriter(new OutputStreamWriter(new NullOutputStream()));

    Iterator it = startState.getLevels();
    boolean hasNext;
    int level = 0;
    if (it.hasNext())
    {
      level = ((Integer) it.next()).intValue();
    }

    do
    {
      Log.debug(new Log.SimpleMessage("Processing Level ", new Integer(level)));
      if (level == -1)
      {
        retval = state.copyState();
      }
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

    state.setProperty(JFreeReportConstants.REPORT_PAGECOUNT_PROPERTY,
                      new Integer(state.getCurrentPage() - 1));
    state.setProperty(JFreeReportConstants.REPORT_PREPARERUN_PROPERTY, Boolean.FALSE);

    // part 3: (done by processing the ReportStateList:) Print the report
    StartState sretval = (StartState) retval;
    if (sretval == null)
    {
      throw new IllegalStateException("There was no valid pagination done.");
    }
    sretval.resetState();
    return sretval;
  }

  public void processReport() throws ReportProcessingException
  {
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
