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
import com.jrefinery.report.demo.IconTableModel;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.PDFOutputTarget;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class JFreeReportServlet extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    Log("in processRequest..." + getClass());

    URL rptFormat = getClass().getResource("/com/jrefinery/report/demo/first.xml");
    JFreeReport thisRpt = null;
    try
    {
      ReportGenerator rg = ReportGenerator.getInstance();
      Log(" (rg) -> " + rg);
      thisRpt = rg.parseReport(rptFormat);
    }
    catch (Exception e)
    {
      Log(e.toString());
      e.printStackTrace();
    }
    Log(" thisRpt -> " + thisRpt);
    if (null != thisRpt)
    {
      thisRpt.setData(readData()); //NOTE: NULL data cannot be set into the report.
      response.setHeader("Content-Type", "application/pdf");

      response.setHeader("Content-Disposition", "attachment; filename=\"" + "unknown.pdf" + "\"");
      //above line if enabled will pop-Out the browsers "File Download" dialog
      //with the standard options: "Open from current location"/ "Save to disk"

      ServletOutputStream out = response.getOutputStream();

      try
      {
        PDFOutputTarget target = new PDFOutputTarget(out, new PageFormat(), true);
        target.setProperty(PDFOutputTarget.TITLE, "Title");
        target.setProperty(PDFOutputTarget.AUTHOR, "Author");
        target.open();
        thisRpt.processReport(target);
        target.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  private void Log(String s)
  {
    System.out.println(new StringBuffer("JFreeReportServlet::").append(s));
  }

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
      Log("Unable to find jlfgr-1_0.jar\n" +
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
      Log("Unable to load the ICONS");
    }
    return result;
  }

  private Image getImage(ZipFile file, ZipEntry entry) //copied from First.java
  {
    Image result = null;
    try
    {
      InputStream in = new BufferedInputStream(file.getInputStream(entry));
      byte[] bytes = new byte[(int) entry.getSize()];
      int count = in.read(bytes);
      ImageIcon temp = new ImageIcon(bytes);
      result = temp.getImage();
    }
    catch (IOException e)
    {
      System.out.println(e.toString());
    }
    return result;
  }

  private String getCategory(String fullName) //copied from First.java
  {
    int start = fullName.indexOf("/") + 1;
    int end = fullName.lastIndexOf("/");
    return fullName.substring(start, end);
  }

  private String getName(String fullName) //copied from First.java
  {
    int start = fullName.lastIndexOf("/") + 1;
    int end = fullName.indexOf(".");
    return fullName.substring(start, end);
  }
}
