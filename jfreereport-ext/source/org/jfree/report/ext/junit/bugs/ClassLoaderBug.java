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
 * ClassLoaderBug.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 16.07.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.bugs;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.report.ext.servletdemo.JFreeReportServlet;

public class ClassLoaderBug extends JFreeReportServlet
{
  public ClassLoaderBug()
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
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    final String baseName = "com.jrefinery.report.resources.JFreeReportResources";
    try
    {
      this.getClass().getClassLoader().loadClass(baseName);
      System.out.println ("OK");
    }
    catch (Exception ioe)
    {
      ioe.printStackTrace();
    }

    try
    {
      ResourceBundle.getBundle(baseName);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    try
    {
      ResourceBundle.getBundle(baseName, Locale.getDefault(), getClass().getClassLoader());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    super.doGet(request, response);
  }

}
