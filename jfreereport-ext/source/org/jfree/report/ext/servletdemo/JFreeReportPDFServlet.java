package org.jfree.report.ext.servletdemo;

import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.swingicons.SwingIconsDemoTableModel;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget;
import org.jfree.report.modules.parser.base.ReportGenerator;
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
  public void init() throws ServletException
  {
    super.init();
    JFreeReportBoot.getInstance().start();
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
      httpServletResponse.setHeader("Content-Disposition", "inline; filename=\"swingicons.pdf\"");
      httpServletResponse.setHeader("Content-Type", "application/pdf");

      final URL in = ObjectUtilities.getResource
              (DemoConstants.REPORT_DEFINITION, JFreeReportPDFServlet.class);
      final ReportGenerator generator = ReportGenerator.getInstance();
      final JFreeReport report = generator.parseReport(in);

      report.setData(new SwingIconsDemoTableModel());

      final ServletOutputStream outputStream = httpServletResponse.getOutputStream();
      final PDFOutputTarget target = new PDFOutputTarget(outputStream);
      target.configure(report.getReportConfiguration());
      target.open();

      final PageableReportProcessor proc = new PageableReportProcessor(report);
      proc.setOutputTarget(target);
      proc.processReport();

      target.close();
      outputStream.close();
    }
    catch(Exception e)
    {
      throw new ServletException(e);
    }
  }
}
