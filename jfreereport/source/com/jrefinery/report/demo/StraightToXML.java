/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------
 * StraightToXML.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StraightToXML.java,v 1.9 2003/06/19 18:44:09 taqua Exp $
 *
 * Changes
 * -------
 * ??-Jan-2002 : Version 1 (TM);
 *
 */

package com.jrefinery.report.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import javax.swing.table.TableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.xml.XMLProcessor;
import com.jrefinery.report.util.Log;
import org.jfree.xml.ParseException;

/**
 * A demonstration that shows how to generate a report and save it to XML without displaying
 * any print preview first.
 *
 * @author Thomas Morgner
 */
public class StraightToXML
{

  /**
   * Creates a new demo application.
   *
   * @param filename  the output filename.
   * @throws ParseException if the report could not be parsed.
   */
  public StraightToXML(String filename) throws ParseException
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
   * @throws ParseException if the report could not be parsed.
   */
  private JFreeReport parseReport(URL templateURL) throws ParseException
  {
    ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      return generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      throw new ParseException("Failed to parse the report", e);
    }
  }

  /**
   * Saves a report to XML format.
   *
   * @param report  the report.
   * @param fileName  target file name.
   *
   * @return <code>true</code> if the export succeeded, and <code>false</code> otherwise.
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
        if (out != null)
        {
          out.close();
        }
      }
      catch (Exception e)
      {
        System.err.println("Saving XML failed.");
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
    try
    {
      StraightToXML demo = new StraightToXML(System.getProperty("user.home") + "/test99.xml");
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.error ("Failed to run demo", e);
      System.exit (1);
    }
  }

}