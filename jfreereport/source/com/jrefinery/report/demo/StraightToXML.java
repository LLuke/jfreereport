/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------
 * StraightToPDF.java
 * ------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: StraightToXML.java,v 1.1 2003/01/07 17:34:26 taqua Exp $
 *
 * Changes
 * -------
 * 13-Dec-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.xml.XMLProcessor;

import javax.swing.table.TableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;

/**
 * A demonstration that shows how to generate a report and save it to PDF without displaying
 * the print preview or the PDF save-as dialog.
 *
 * @author David Gilbert
 */
public class StraightToXML
{

  /**
   * Creates a new demo application.
   *
   * @param filename  the output filename.
   */
  public StraightToXML(String filename)
  {
    URL in  = getClass().getResource("/com/jrefinery/report/demo/OpenSourceDemo.xml");
    JFreeReport report = parseReport(in);
    TableModel data = new OpenSourceProjects();
    report.setData(data);
    saveXML(report, filename);
  }

  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL  the template location.
   *
   * @return a report.
   */
  private JFreeReport parseReport(URL templateURL)
  {
    JFreeReport result = null;
    ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      result = generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      System.out.println(e.toString());
    }
    return result;
  }

  /**
   * Saves a report to PDF format.
   *
   * @param report  the report.
   * @param fileName target file name.
   *
   * @return true or false.
   */
  public boolean saveXML(JFreeReport report, String fileName)
  {
    Writer out = null;
    try
    {
      out = new BufferedWriter(new FileWriter(new File(fileName)));

      XMLProcessor xprc = new XMLProcessor(report);
      xprc.setWriter(out);
      xprc.processReport();
      return true;
    }
    catch (Exception e)
    {
      System.err.println("Writing PDF failed.");
      System.err.println(e.toString());
      return false;
    }
    finally
    {
      try
      {
        out.close();
      }
      catch (Exception e)
      {
        System.err.println("Saving PDF failed.");
        System.err.println(e.toString());
      }
    }
  }

  /**
   * Demo starting point.
   *
   * @param args  ignored.
   */
  public static void main(String args[])
  {
    StraightToXML demo = new StraightToXML(System.getProperty("user.home") + "/test99.xml");
    System.exit(0);
  }

}