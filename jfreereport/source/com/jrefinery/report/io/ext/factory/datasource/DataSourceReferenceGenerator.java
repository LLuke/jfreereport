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
 * -------------------------------
 * StyleKeyReferenceGenerator.java
 * -------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataSourceReferenceGenerator.java,v 1.1 2003/06/10 18:14:27 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.datasource;

import java.net.URL;

import javax.swing.table.TableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.support.ReportProcessorUtil;

/**
 * An application that generates a report that provides style key reference information.
 * 
 * @author Thomas Morgner.
 */
public class DataSourceReferenceGenerator
{
  /** The report definition file. */    
  private static final String REFERENCE_REPORT =
    "/com/jrefinery/report/io/ext/factory/datasource/DataSourceReferenceReport.xml";

  public static TableModel createData ()
  {
    DataSourceCollector cc = new DataSourceCollector();
    cc.addFactory(new DefaultDataSourceFactory());

    DataSourceReferenceTableModel model = new DataSourceReferenceTableModel(cc);
    return model;
  }

  /**
   * The starting point for the application.
   * 
   * @param args  ignored.
   */
  public static void main (String [] args)
  {
    ReportGenerator gen = ReportGenerator.getInstance();
    URL reportURL = gen.getClass().getResource(REFERENCE_REPORT);
    if (reportURL == null)
    {
      System.err.println ("The report was not found in the classpath");
      System.err.println ("File: " + REFERENCE_REPORT);
      System.exit(1);
      return;
    }

    JFreeReport report;
    try
    {
      report = gen.parseReport(reportURL);
    }
    catch (Exception e)
    {
      System.err.println ("The report could not be parsed.");
      System.err.println ("File: " + REFERENCE_REPORT);
      e.printStackTrace(System.err);
      System.exit(1);
      return;
    }
    report.setData(createData());
    try
    {
      ReportProcessorUtil.createStreamHTML(report, System.getProperty("user.home") 
                                                   + "/datasource-reference.html");
      ReportProcessorUtil.createPDF(report, System.getProperty("user.home") 
                                            + "/datasource-reference.pdf");
    }
    catch (Exception e)
    {
      System.err.println ("The report processing failed.");
      System.err.println ("File: " + REFERENCE_REPORT);
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

}
