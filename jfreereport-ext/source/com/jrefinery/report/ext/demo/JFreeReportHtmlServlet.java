/**
 * Date: Jan 24, 2003
 * Time: 11:36:44 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.demo;

import com.jrefinery.report.demo.FirstDemoTableModel;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.table.html.HtmlProcessor;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

public class JFreeReportHtmlServlet extends HttpServlet
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

    AbstractTableReportServletWorker worker =
        new DefaultTableReportServletWorker(in, new FirstDemoTableModel());

    // display the content in the browser window (see RFC2183)
    response.setHeader("Content-Disposition", "inline; filename=\"" + "unknown.html" + "\"");
    response.setHeader("Content-Type", "text/html");

    try
    {
      // this throws an exception if the report could not be parsed
      HtmlProcessor processor = new HtmlProcessor(worker.getReport());
      processor.setWriter(response.getWriter());
      worker.setTableProcessor(processor);
    }
    catch (Exception e)
    {
      Log.debug ("Failed to parse the report" , e);
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
      Log.debug ("Failed to create the report", e);
    }
  }
}
