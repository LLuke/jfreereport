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
 * DemoImageServlet.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DemoImageServlet.java,v 1.2 2005/08/09 15:44:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.servletdemo;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.table.TableModel;

import com.keypoint.PngEncoder;
import org.jfree.report.demo.swingicons.SwingIconsDemoTableModel;

public class DemoImageServlet extends HttpServlet
{
  private TableModel model;

  public DemoImageServlet ()
  {
  }

  public void init ()
          throws ServletException
  {
    try
    {
      final URL base = getServletContext().getResource("/WEB-INF/lib/jlfgr-1_0.jar");
      model = new SwingIconsDemoTableModel(base);
    }
    catch (MalformedURLException e)
    {
      log("Failed to initialized, images will be unavailable.", e);
    }
  }

  protected void doGet (final HttpServletRequest request,
                        final HttpServletResponse httpServletResponse)
          throws ServletException, IOException
  {
    if (model == null)
    {
      httpServletResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
      return;
    }
    try
    {
      final String parameter = request.getParameter("entry");
      final int row = Integer.parseInt(parameter);
      final Image image = (Image) model.getValueAt(row, 2);
      final PngEncoder encoder = new PngEncoder(image, true);
      final byte[] data = encoder.pngEncode();

      httpServletResponse.setHeader("Content-Type", "image/png");
      httpServletResponse.setHeader("Content-Length", String.valueOf(data));
      httpServletResponse.getOutputStream().write(data);
      httpServletResponse.flushBuffer();
    }
    catch(Exception e)
    {
      httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
  }
}
