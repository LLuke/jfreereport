/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------------
 * JFreeReportExcelServlet.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: JFreeReportExcelServlet.java,v 1.3 2003/09/09 10:27:59 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 30-Jun-2003 : Initial version (derived from an posting in the forum)
 *  
 */

package org.jfree.report.ext.servletdemo;

import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.report.modules.output.table.xls.ExcelProcessor;
import org.jfree.report.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * A Sample HttpServlet to show how JFreeReports can be used to generate
 * Excel content in a webbased environment.
 * <p>
 * POST and GET are handled equal, so it does not
 * matter whether you POST or GET the URL for this servlet.
 *
 * @author Thomas Morgner
 */
public class JFreeReportExcelServlet extends HttpServlet
{
  /**
   * Handles the GET method for the servlet. The GET method is mapped to
   * the POST method, both commands are handled equal.
   *
   * @param request the http request object.
   * @param response the http response object.
   * @throws ServletException if an error occured, which could not be handled internaly.
   * @throws IOException if writing the generated contents failed.
   */
  public void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException
  {
    doPost(request, response);
  }

  /**
   * Handles the POST method for the request. This parses the report definition,
   * loads the tablemodel and generates a single page of the report. The generated
   * page is returned as Excel file, with the implicit restriction, that images
   * are not included.
   *
   * @param request the http request object.
   * @param response the http response object.
   * @throws ServletException if an error occured, which could not be handled internaly.
   * @throws IOException if writing the generated contents failed.
   */
  public void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException
  {
    Log.debug("in processRequest..." + getClass());

    final URL in = ObjectUtilities.getResourceRelative
            ("/org/jfree/report/demo/swing-icons.xml", JFreeReportExcelServlet.class);
    if (in == null)
    {
      throw new ServletException("Missing Resource: /org/jfree/report/demo/swing-icons.xml");
    }

    final URL base = getServletContext().getResource("/WEB-INF/lib/jlfgr-1_0.jar");
    Log.debug("Base: " + base);
    final AbstractTableReportServletWorker worker =
        new DefaultTableReportServletWorker(in, new DemoModelProvider(base));

    // display the content in the browser window (see RFC2183)
    // may or may not work for excel content ...
    response.setHeader("Content-Disposition", "inline; filename=\"" + "unknown.xls" + "\"");
    response.setHeader("Content-Type", "application/vnd.ms-excel");

    try
    {
      // this throws an exception if the report could not be parsed
      final ExcelProcessor processor = new ExcelProcessor(worker.getReport());
      processor.setOutputStream(response.getOutputStream());
      worker.setTableProcessor(processor);
    }
    catch (Exception e)
    {
      Log.debug("Failed to parse the report", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    //response.setHeader("Content-Disposition", "attachment; filename=\"" + "unknown.pdf" + "\"");
    //above line if enabled will pop-Out the browsers "File Download" dialog
    //with the standard options: "Open from current location"/ "Save to disk"

    try
    {
      worker.processReport();
    }
    catch (Exception e)
    {
      Log.debug("Failed to create the report", e);
    }
  }

}
