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
 * -----------------------
 * JFreeReportServlet.java
 * -----------------------
 *
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportServlet.java,v 1.9 2005/09/07 11:24:09 taqua Exp $
 *
 * Changes
 * -------
 * 30-Sep-2002: Initial version
 */
package org.jfree.report.ext.servletdemo;

import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * A Sample HttpServlet to show how JFreeReports can be used in a
 * web based environment. POST and GET are handled equal, so it does not
 * matter whether you POST or GET the URL for this servlet.
 *
 * @author Jeevan Sunkersett
 */
public class JFreeReportServlet extends HttpServlet
{
  public JFreeReportServlet ()
  {
  }

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
   * page is returned as PDF file.
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

    final URL in = ObjectUtilities.getResource
            ("/org/jfree/report/demo/swingicons/swing-icons.xml", JFreeReportServlet.class);
    if (in == null)
    {
      throw new ServletException("Missing Resource: /org/jfree/report/demo/swing-icons.xml");
    }

    final URL base = getServletContext().getResource("/WEB-INF/lib/jlfgr-1_0.jar");
    final AbstractPageableReportServletWorker worker =
        new DefaultPageableReportServletWorker(null,
                                               in,
                                               new DemoModelProvider(base));

    try
    {
      // this throws an exception if the report could not be parsed
      worker.getReport();
    }
    catch (Exception e)
    {
      Log.debug("Failed to parse the report", e);
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
      final ServletOutputStream out = response.getOutputStream();
      final PDFOutputTarget target = new PDFOutputTarget(out);
      worker.setOutputTarget(target);
      worker.processReport();
      out.flush();
    }
    catch (Exception e)
    {
      Log.debug("Failed to create the report", e);
    }
  }
}
