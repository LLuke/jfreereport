/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * -----------------------
 * DefaultPageableReportServletWorker.java
 * -----------------------
 *
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: DefaultPageableReportServletWorker.java,v 1.2 2003/03/01 14:55:33 taqua Exp $
 *
 * Changes
 * -------
 * 24-Jan-2003: Initial version
 */
package com.jrefinery.report.ext.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportInitialisationException;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.util.Log;

import javax.servlet.http.HttpSession;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.net.URL;

/**
 * A report servlet worker, which is able to load report from a given URL and
 * to assign a provided tablemodel to the report. This servlet worker should be
 * used to process report for the pageable output targets.
 * <p>
 * This implementation should handle most reporting cases. If your report needs
 * extra initializations, override <code>createReport</code>.
 */
public class DefaultPageableReportServletWorker
    extends AbstractPageableReportServletWorker
{
  /** the source url for the xml report definition. */
  private URL reportDefinition;
  /** the table model, that should be used when loading the report. */
  private TableModel data;

  /**
   * Creates a default implementation for the pageable report servlet worker. This
   * implementation loads the report from the given URL and assignes the given
   * tablemodel to the generated report definition.
   *
   * @param report the url of the report definition.
   * @param data the tablemodel that should be used for the reporting.
   * @param session the current session, or null, if no session handling should be used.
   */
  public DefaultPageableReportServletWorker(HttpSession session, URL report, TableModel data)
  {
    super(session);
    if (report == null) throw new NullPointerException();
    if (data == null) throw new NullPointerException();
    this.reportDefinition = report;
    this.data = data;
  }

  /**
   * Parses the report and returns the fully initialized report. A data model is
   * already assigned to the report.
   *
   * @return the created report.
   * @throws ReportInitialisationException if the report creation failed.
   */
  protected JFreeReport createReport() throws ReportInitialisationException
  {
    try
    {
      JFreeReport report = parseReport(reportDefinition);
      report.setData(data);
      return report;
    }
    catch (Exception e)
    {
      throw new ReportInitialisationException("Unable to create the report");
    }
  }

  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL  the template location.
   *
   * @return a report.
   */
  private JFreeReport parseReport(URL templateURL)
    throws IOException
  {
    JFreeReport result = null;
    ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
        result = generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      Log.debug ("Cause: ", e);
      throw new IOException("Failed to parse the report");
    }
    return result;
  }
}
