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

import com.jrefinery.io.FileUtilities;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportStateList;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.states.StartState;
import com.jrefinery.report.demo.IconTableModel;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.PDFOutputTarget;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.ImageIcon;
import javax.swing.table.TableModel;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A Sample HttpServlet to show how JFreeReports can be used in a
 * web based environment. POST and GET are handled equal, so it does not
 * matter whether you POST or GET the URL for this servlet.
 *
 * @author Jeevan Sunkersett
 */
public class JFreeReportJpegServlet extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    doPost(request, response);
  }

  private JFreeReport createReport ()
  {
    URL rptFormat = getClass().getResource("/com/jrefinery/report/demo/first.xml");
    JFreeReport thisRpt = null;
    try
    {
      ReportGenerator rg = ReportGenerator.getInstance();
      Log.debug(" (rg) -> " + rg);
      thisRpt = rg.parseReport(rptFormat);
      thisRpt.setData(readData()); //NOTE: NULL data cannot be set into the report.
    }
    catch (Exception e)
    {
      Log.debug(e.toString());
      e.printStackTrace();
    }
    return thisRpt;
  }

  private ReportStateList getReportSession (HttpServletRequest request, PDFOutputTarget target)
  {
    ReportStateList psl = null;
    HttpSession session = request.getSession(true);
    if (session.isNew())
    {
      try
      {
        psl = JFreeReport.repaginate(target, new StartState (createReport()));
        session.setAttribute("PageStateList", psl);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      psl = (ReportStateList) session.getAttribute("PageStateList");
    }
    return psl;
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    Log.debug("in processRequest..." + getClass());

    response.setHeader("Content-Type", "image/jpeg");
    ServletOutputStream out = response.getOutputStream();

    PDFOutputTarget target = new PDFOutputTarget(out, new PageFormat(), true);
    target.setProperty(PDFOutputTarget.TITLE, "Title");
    target.setProperty(PDFOutputTarget.AUTHOR, "Author");

    ReportStateList psl = getReportSession(request, target);
    HttpSession session = request.getSession(true);
    if (session.isNew())
    {
      try
      {
        psl = JFreeReport.repaginate(target, new StartState (createReport()));
        session.setAttribute("PageStateList", psl);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      psl = (ReportStateList) session.getAttribute("PageStateList");
    }

    int page = 0;
    String param = request.getParameter("page");
    if (param != null)
    {
      try
      {
        page = Integer.parseInt(param);
      }
      catch (Exception e)
      {
        Log.debug ("Invalid page parameter given");
        page = 1;
      }
    }
    try
    {
      target.open();
      ReportState state = psl.get(page);
      JFreeReport.processPage(target, state, true);
      target.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /** creates the tablemodel **/
  private TableModel readData() //copied from First.java
  {
    IconTableModel result = new IconTableModel();
    //find the file on the classpath...
    //File f = new File("e:\\Jeevan\\jlfgr-1_0.jar");	//hardcoded the location of the file.

    // can this cause trouble? If you get a "unable to find ... " message try to hardcode
    // the file as shown above. (? in this case: find a better solution ...)
    File f = FileUtilities.findFileOnClassPath("jlfgr-1_0.jar");
    if (f == null)
    {
      Log.debug("Unable to find jlfgr-1_0.jar\n" +
          "Unable to load the icons.\n" +
          "Please make sure you have the Java Look and Feel Graphics Repository in your classpath.\n" +
          "You may download this jar-file from http://developer.java.sun.com/developer/techDocs/hi/repository.");

      return result;
    }
    try
    {
      ZipFile iconJar = new ZipFile(f);
      Enumeration e = iconJar.entries();
      while (e.hasMoreElements())
      {
        ZipEntry ze = (ZipEntry) e.nextElement();
        String fullName = ze.getName();
        if (fullName.endsWith(".gif"))
        {
          String category = getCategory(fullName);
          String name = getName(fullName);
          Image image = getImage(iconJar, ze);
          Long bytes = new Long(ze.getSize());
          result.addIconEntry(name, category, image, bytes);
        }
      }
    }
    catch (IOException e)
    {
      Log.debug("Unable to load the ICONS");
    }
    return result;
  }

  /** part of: creates the tablemodel **/
  private Image getImage(ZipFile file, ZipEntry entry) //copied from First.java
  {
    Image result = null;
    try
    {
      InputStream in = new BufferedInputStream(file.getInputStream(entry));
      byte[] bytes = new byte[(int) entry.getSize()];
      in.read(bytes);
      ImageIcon temp = new ImageIcon(bytes);
      result = temp.getImage();
    }
    catch (IOException e)
    {
      System.out.println(e.toString());
    }
    return result;
  }

  /** part of: creates the tablemodel **/
  private String getCategory(String fullName) //copied from First.java
  {
    int start = fullName.indexOf("/") + 1;
    int end = fullName.lastIndexOf("/");
    return fullName.substring(start, end);
  }

  /** part of: creates the tablemodel **/
  private String getName(String fullName) //copied from First.java
  {
    int start = fullName.lastIndexOf("/") + 1;
    int end = fullName.indexOf(".");
    return fullName.substring(start, end);
  }
}
