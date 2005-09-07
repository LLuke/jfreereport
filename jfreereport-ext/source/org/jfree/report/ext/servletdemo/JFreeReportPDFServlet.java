package org.jfree.report.ext.servletdemo;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.misc.tablemodel.ResultSetTableModelFactory;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.CloseableTableModel;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 31.08.2005, 23:04:54
 *
 * @author: Thomas Morgner
 */
public class JFreeReportPDFServlet extends HttpServlet
{
  public JFreeReportPDFServlet()
  {
  }

  protected void doGet(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse)
          throws ServletException, IOException
  {
    doPost(httpServletRequest, httpServletResponse);
  }

  protected void doPost(HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse)
          throws ServletException, IOException
  {
    try
    {
      // display the content in the browser window (see RFC2183)
      httpServletResponse.setHeader("Content-Disposition", "inline; filename=\"first.pdf\"");
      httpServletResponse.setHeader("Content-Type", "application/pdf");

      final URL in = ObjectUtilities.getResource
              ("yourreportdefinition.xml", JFreeReportPDFServlet.class);
      final ReportGenerator generator = ReportGenerator.getInstance();
      final JFreeReport report = generator.parseReport(in);

      // get me a valid result set
      // return a scrollable resultset if the database should be responsible for
      // the caching, else we will copy the contents of the result set into a
      // default table model - which may be faster..
      final ResultSet queryResult = queryDB (httpServletRequest);
      final CloseableTableModel data =
              ResultSetTableModelFactory.getInstance().createTableModel(queryResult);
      report.setData(data);

      final ServletOutputStream outputStream = httpServletResponse.getOutputStream();
      final PDFOutputTarget target = new PDFOutputTarget(outputStream);
      target.configure(report.getReportConfiguration());
      target.open();

      final PageableReportProcessor proc = new PageableReportProcessor(report);
      proc.setOutputTarget(target);
      proc.processReport();

      target.close();
      data.close();
      outputStream.close();
    }
    catch(Exception e)
    {
      throw new ServletException(e);
    }
  }

  private ResultSet queryDB(final HttpServletRequest httpServletRequest)
  {
    return null;
  }
}
