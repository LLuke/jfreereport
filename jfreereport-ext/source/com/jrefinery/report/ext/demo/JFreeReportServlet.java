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
 * JFreeReportServlet.java
 * -----------------------
 *
 * Changes
 * -------
 * 30-Sep-2002: Initial version
 */
package com.jrefinery.report.ext.demo;

import com.jrefinery.report.demo.FirstDemoTableModel;
import com.jrefinery.report.targets.pageable.output.PDFOutputTarget;
import com.jrefinery.report.util.Log;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.net.URL;

/**
 * A Sample HttpServlet to show how JFreeReports can be used in a
 * web based environment. POST and GET are handled equal, so it does not
 * matter whether you POST or GET the URL for this servlet.
 *
 * @author Jeevan Sunkersett
 */
public class JFreeReportServlet extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    Log.debug("in processRequest..." + getClass());

    URL in = getClass().getResource("/com/jrefinery/report/demo/first.xml");
    if (in == null)
      throw new NullPointerException();

    AbstractPageableReportServletWorker worker =
        new DefaultPageableReportServletWorker(null,
                                               in,
                                               new FirstDemoTableModel());

    try
    {
      // this throws an exception if the report could not be parsed
      worker.getReport();
    }
    catch (Exception e)
    {
      Log.debug ("Failed to parse the report" , e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    response.setHeader("Content-Type", "application/pdf");

    // display the content in the browser window (see RFC2183)
    response.setHeader("Content-Disposition", "inline; filename=\"first.pdf\"");

    //response.setHeader("Content-Disposition", "attachment; filename=\"" + "unknown.pdf" + "\"");

    //if content disposition is set to "attachment", then browsers shows "File Download"
    // dialog with the standard options: "Open from current location"/ "Save to disk"
    // if Content-Disposition is set to inline, the any RFC compliant browser will open
    // the content in the current window.

    try
    {
      PDFOutputTarget target = new PDFOutputTarget(response.getOutputStream(),
                                                   worker.getReport().getDefaultPageFormat(),
                                                   true);
      target.setProperty(PDFOutputTarget.TITLE, "Title");
      target.setProperty(PDFOutputTarget.AUTHOR, "Author");
      worker.repaginateReport(target);
      worker.processReport();
    }
    catch (Exception e)
    {
      Log.debug ("Failed to create the report", e);
    }
  }
}
