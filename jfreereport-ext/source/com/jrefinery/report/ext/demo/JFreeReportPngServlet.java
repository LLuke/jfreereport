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
 * JFreeReportPngServlet.java
 * -----------------------
 *
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportPngServlet.java,v 1.3 2003/03/01 14:55:33 taqua Exp $
 *
 * Changes
 * -------
 * 24-Jan-2003: Initial version
 */
package com.jrefinery.report.ext.demo;

import com.jrefinery.report.demo.SwingIconsDemoTableModel;
import com.jrefinery.report.targets.pageable.output.G2OutputTarget;
import com.jrefinery.report.util.Log;
import com.keypoint.PngEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.net.URL;

/**
 * A Sample HttpServlet to show how JFreeReports can be used to generate
 * PNG content in a webbased environment.
 * <p>
 * The servlet expects a <code>page</code> parameter to be set. This parameter
 * must be greater or equal to one and must fit the report's paginated pages.
 * This servlet is not intended to be a stand-alone solution, it should be
 * used in conjunction with a wrapper page, which uses &lt;IMG&gt; tags to link
 * to this servlet.
 * <p>
 * POST and GET are handled equal, so it does not
 * matter whether you POST or GET the URL for this servlet.
 *
 * @author Jeevan Sunkersett
 */
public class JFreeReportPngServlet extends HttpServlet
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
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    doPost(request, response);
  }

  /**
   * Handles the POST method for the request. This processes the "
   * <p>
   * The page parameter is required, must be a valid integer.
   *
   * @param request the http request object.
   * @param response the http response object.
   * @throws ServletException if an error occured, which could not be handled internaly.
   * @throws IOException if writing the generated contents failed.
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {

    BufferedImage image = null;
    try
    {
      URL in = getClass().getResource("/com/jrefinery/report/demo/swing-icons.xml");
      if (in == null)
      {
        throw new ServletException("Missing Resource: /com/jrefinery/report/demo/swing-icons.xml");
      }

      AbstractPageableReportServletWorker worker =
          new DefaultPageableReportServletWorker(request.getSession(true),
                                                 in,
                                                 new SwingIconsDemoTableModel());
      worker.getReport();

      String param = request.getParameter("page");
      if (param == null)
      {
        Log.debug ("Page attribute is missing");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      int page = 0;
      try
      {
        page = Integer.parseInt(param);
      }
      catch (Exception e)
      {
        Log.debug("The page-parameter is invalid", e);
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      PageFormat pageFormat = worker.getReportPageFormat();
      image = createImage(pageFormat);
      Graphics2D g2 = image.createGraphics();
      g2.setPaint(Color.white);
      g2.fillRect(0,0, (int) pageFormat.getWidth(), (int) pageFormat.getHeight());

      G2OutputTarget target = new G2OutputTarget(g2, pageFormat);

      if (page >= worker.getNumberOfPages(target) || page < 0)
      {
        Log.debug("The page-parameter is invalid: " + page + ": " + worker.getNumberOfPages(target));
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      worker.processPage(page);

      response.setHeader("Content-Type", "image/png");
      response.setHeader("Content-Disposition","inline;filename=\"report-page-" + page + ".png\"");

      ServletOutputStream out = response.getOutputStream();
      // from JFreeChart ...
      PngEncoder encoder = new PngEncoder(image, true, 0, 9);
      byte[] data = encoder.pngEncode();
      out.write(data);
      out.flush();
    }
    catch (Exception e)
    {
      Log.debug("Failed to serve the request", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }
  }

  /**
   * Create the empty image for the given page size.
   *
   * @param pf the page format that defines the image bounds.
   * @return the generated image.
   */
  private BufferedImage createImage(PageFormat pf)
  {
    double width = pf.getWidth();
    double height = pf.getHeight();
    //write the report to the temp file
    BufferedImage bi = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_BYTE_INDEXED);
    return bi;
  }

}
