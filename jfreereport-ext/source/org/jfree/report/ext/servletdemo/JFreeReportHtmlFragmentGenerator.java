/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * JFreeReportHtmlFragmentGenerator.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.servletdemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.output.table.html.HtmlProcessor;
import org.jfree.report.modules.output.table.html.StreamHtmlFilesystem;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;

/**
 * A Sample HttpServlet to show how JFreeReports can be used to generate
 * Html content in a webbased environment.
 * <p>
 * POST and GET are handled equal, so it does not
 * matter whether you POST or GET the URL for this servlet.
 *
 * @author Thomas Morgner
 */
public class JFreeReportHtmlFragmentGenerator
{
  public JFreeReportHtmlFragmentGenerator ()
  {
  }

  public String performGenerate (final PageContext pageContext)
      throws ServletException, IOException
  {
    try
    {
      Log.debug("in processRequest..." + getClass());

      final URL in = getClass().getResource("/org/jfree/report/demo/swing-icons.xml");
      if (in == null)
      {
        throw new ServletException("Missing Resource: /org/jfree/report/demo/swing-icons.xml");
      }

      final URL base = pageContext.getServletContext().getResource("/WEB-INF/lib/jlfgr-1_0.jar");
      final TableModel model = new ServletSwingIconsDemoTableModel(base);

      final JFreeReport report = ReportGenerator.getInstance().parseReport(in);
      report.setData(model);

      final ByteArrayOutputStream bo = new ByteArrayOutputStream();

      // this throws an exception if the report could not be parsed
      final HtmlProcessor processor = new HtmlProcessor(report);
      processor.setGenerateBodyFragment(true);
      processor.setEncoding ("UTF-8");
      final StreamHtmlFilesystem filesystem = new StreamHtmlFilesystem(bo);
      processor.setFilesystem(filesystem);
      processor.processReport();
      return new String (bo.toByteArray(), "UTF-8");
    }
    catch (Exception e)
    {
      Log.debug("Failed to parse the report", e);
      throw new ServletException("Unable to process the report.");
    }
  }
}
