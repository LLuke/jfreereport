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
 * -------------------------
 * DefaultTableReportServletWorker.java
 * -------------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: DefaultTableReportServletWorker.java,v 1.1 2003/01/25 02:56:17 taqua Exp $
 *
 * Changes
 * -------
 * 24-Jan-2003 : Initial version
 *
 */
package com.jrefinery.report.ext.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportInitialisationException;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.util.Log;

import javax.swing.table.TableModel;
import java.io.IOException;
import java.net.URL;

public class DefaultTableReportServletWorker extends AbstractTableReportServletWorker
{
  private URL url;
  private TableModel model;

  public DefaultTableReportServletWorker(URL url, TableModel model)
  {
    super();
    this.url = url;
    this.model = model;
  }

  /**
   * parses the report and returns the fully initialized report.
   * @return
   */
  protected JFreeReport createReport()
      throws ReportInitialisationException
  {
    try
    {
      JFreeReport report = parseReport(url);
      report.setData(model);
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
