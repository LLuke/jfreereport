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
 * -------------------
 * XMLProcessor.java
 * -------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: XMLProcessor.java,v 1.4 2003/02/04 17:56:33 taqua Exp $
 *
 * Changes
 * -------
 * 07-Jan-2003 : initial version
 */
package com.jrefinery.report.targets.xml;

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

/**
 * The XMLProcessor coordinates the report processing for the XML-Output.
 * This class is responsible to initialize and maintain the XMLWriter, which performs
 * the output process.
 */
public class XMLProcessor
{
  /** */
  private static final String XML_WRITER = "com.jrefinery.report.targets.xml-writer";
  /** */
  private Writer writer;
  /** */
  private JFreeReport report;

  /**
   *
   * @param report
   * @throws ReportProcessingException
   * @throws FunctionInitializeException
   */
  public XMLProcessor (JFreeReport report)
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

    XMLWriter lm = new XMLWriter();
    lm.setName(XML_WRITER);
    this.report.addFunction(lm);
  }

  /**
   * Returns the XMLProcessors local report instance. This instance has the
   * XMLWriter attached and should be used outside of this class.
   *
   * @return the processors report instance.
   */
  protected JFreeReport getReport()
  {
    return report;
  }

  /**
   * Returns the writer, which will receive the generated output.
   *
   * @return the writer
   */
  public Writer getWriter()
  {
    return writer;
  }

  /**
   * Sets the writer, which will receive the generated output.
   * The writer should have the proper encoding set.
   *
   * @param writer that should receive the generated output.
   */
  public void setWriter(Writer writer)
  {
    this.writer = writer;
  }

  /**
   * Processes the entire report and records the state at the end of the report preparation.
   *
   * @return the final ReportState
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
    XMLWriter w = (XMLWriter) state.getDataRow().get(XML_WRITER);
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
      Log.debug (new Log.SimpleMessage("Processing Level " , new Integer(level)));
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
          throw new IllegalStateException("Repaginate did not produce an finish state");
        }
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
    if (sretval == null)
      throw new IllegalStateException("Repaginate did no produce a final state");
    return sretval;
  }

  /**
   * Processes the report in two passes, the first pass calculates function
   * values, the second pass sends the report to the output target.
   * <p>
   * It is possible for the report processing to fail.  A base cause is that the report is
   * designed for a certain page size, but ends up being sent to an output target with a much
   * smaller page size.  If the headers and footers don't leave enough room on the page for at
   * least one row of data to be printed, then no progress is made.  An exception will be thrown
   * if this happens.
   *
   * @throws ReportProcessingException if the report did not proceed and got stuck.
   */
  public void processReport () throws ReportProcessingException
  {
    try
    {
      ReportState state = repaginate();

      XMLWriter w = (XMLWriter) state.getDataRow().get(XML_WRITER);
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
