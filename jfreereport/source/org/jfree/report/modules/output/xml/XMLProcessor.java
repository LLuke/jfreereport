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
 * -----------------
 * XMLProcessor.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: XMLProcessor.java,v 1.17 2003/06/29 16:59:30 taqua Exp $
 *
 * Changes
 * -------
 * 07-Jan-2003 : initial version
 */
package org.jfree.report.modules.output.xml;

import java.awt.print.PageFormat;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportConstants;
import org.jfree.report.ReportEventException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.function.FunctionInitializeException;
import org.jfree.report.states.FinishState;
import org.jfree.report.states.ReportState;
import org.jfree.report.states.ReportStateProgress;
import org.jfree.report.states.StartState;
import org.jfree.report.util.NullOutputStream;

/**
 * The XMLProcessor coordinates the report processing for the XML-Output.
 * This class is responsible to initialize and maintain the XMLWriter, which performs
 * the output process.
 * <p>
 * The XMLProcessor is not intended to produce complex output, it is an educational
 * example. If you want valid xml data enriched with layouting information, then
 * have a look at the HTML-OutputTarget, this target is also able to write XHTMl code.
 *
 * @author Thomas Morgner
 */
public class XMLProcessor
{
  /** the name of the writer function used in the report processing. */
  private static final String XML_WRITER = "org.jfree.report.targets.xml-writer";

  /** the target writer. */
  private Writer writer;

  /** the source report. */
  private JFreeReport report;

  /**
   * Creates a new XMLProcessor. The processor will output the report as simple xml
   * stream.
   *
   * @param report the report that should be processed
   * @throws ReportProcessingException if the report could not be initialized
   * @throws FunctionInitializeException if the writer function could not be initialized.
   */
  public XMLProcessor(final JFreeReport report)
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

    final XMLWriter lm = new XMLWriter();
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
  public void setWriter(final Writer writer)
  {
    this.writer = writer;
  }

  /**
   * Processes the entire report and records the state at the end of the report preparation.
   *
   * @return the final ReportState
   *
   * @throws ReportProcessingException if there was a problem processing the report.
   * @throws CloneNotSupportedException if there is a cloning problem.
   */
  private ReportState repaginate() throws ReportProcessingException, CloneNotSupportedException
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
    final XMLWriter w = (XMLWriter) state.getDataRow().get(XML_WRITER);
    w.setWriter(new OutputStreamWriter(new NullOutputStream()));

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
      final boolean failOnError = (level == -1)
          && getReport().getReportConfiguration().isStrictErrorHandling();
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

      final XMLWriter w = (XMLWriter) state.getDataRow().get(XML_WRITER);
      w.setWriter(getWriter());

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

}
