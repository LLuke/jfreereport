/**
 * Date: Jan 18, 2003
 * Time: 7:47:41 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.states.FinishState;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.states.StartState;
import com.jrefinery.report.util.Log;

import java.awt.print.PageFormat;
import java.util.Iterator;

public abstract class TableProcessor
{
  private static final String TABLE_WRITER = "table-writer";

  private JFreeReport report;

  public TableProcessor (JFreeReport report)
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

    TableWriter lm = new TableWriter();
    lm.setName(TABLE_WRITER);
    this.report.addFunction(lm);
  }

  public JFreeReport getReport()
  {
    return report;
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
    TableWriter w = (TableWriter) state.getDataRow().get(TABLE_WRITER);
    w.setProducer(createProducer(true));

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
    StartState sretval = (StartState) retval;
    sretval.resetState();
    return sretval;
  }

  public void processReport () throws ReportProcessingException
  {
    try
    {
      ReportState state = repaginate();

      Log.debug ("CDI " + state.getCurrentDisplayItem());

      TableWriter w = (TableWriter) state.getDataRow().get(TABLE_WRITER);
      w.setProducer(createProducer(false));
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

  public abstract TableProducer createProducer (boolean dummy);

}
