/**
 * Date: Jan 14, 2003
 * Time: 2:32:00 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.excel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.util.NullOutputStream;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.states.StartState;
import com.jrefinery.report.states.FinishState;
import com.jrefinery.report.function.FunctionInitializeException;

import java.io.OutputStream;
import java.awt.print.PageFormat;
import java.util.Iterator;

public class ExcelProcessor
{
  private static final String EXCEL_WRITER = "excel-writer";

  private OutputStream outputStream;
  private JFreeReport report;

  public ExcelProcessor (JFreeReport report)
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

    ExcelWriter lm = new ExcelWriter();
    lm.setName(EXCEL_WRITER);
    this.report.addFunction(lm);
  }

  public JFreeReport getReport()
  {
    return report;
  }

  public OutputStream getOutputStream()
  {
    return outputStream;
  }

  public void setOutputStream(OutputStream outputStream)
  {
    this.outputStream = outputStream;
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
    ExcelWriter w = (ExcelWriter) state.getDataRow().get(EXCEL_WRITER);
    w.setOutputStream(new NullOutputStream());

    Iterator it = startState.getLevels();
    boolean hasNext = true;
    int level = 0;
    if (it.hasNext())
    {
      level = ((Integer) it.next()).intValue();
    }

    do
    {
      Log.debug (new Log.SimpleMessage("Processing Level " , new Integer(level)));
      if (level == -1)
      {
        retval = state;
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
        state = new StartState((FinishState) state, level);
      }
    }
    while (hasNext == true);

    // root of evilness here ... pagecount should not be handled specially ...

    state.setProperty(JFreeReportConstants.REPORT_PAGECOUNT_PROPERTY,
                      new Integer(state.getCurrentPage() - 1));
    state.setProperty(JFreeReportConstants.REPORT_PREPARERUN_PROPERTY, Boolean.FALSE);

    // part 3: (done by processing the ReportStateList:) Print the report
    return retval;
  }

  public void processReport () throws ReportProcessingException
  {
    try
    {
      ReportState state = repaginate();

      ExcelWriter w = (ExcelWriter) state.getDataRow().get(EXCEL_WRITER);
      w.setOutputStream(getOutputStream());
      w.setMaxWidth((float) getReport().getDefaultPageFormat().getImageableWidth());

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
