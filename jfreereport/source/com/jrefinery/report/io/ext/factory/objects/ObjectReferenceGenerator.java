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
 * -----------------------------
 * ObjectReferenceGenerator.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ObjectReferenceGenerator.java,v 1.6 2003/05/02 12:40:10 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.objects;

import java.net.URL;

import javax.swing.table.TableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.io.ext.factory.datasource.DefaultDataSourceFactory;
import com.jrefinery.report.io.ext.factory.templates.TemplateClassFactory;
import com.jrefinery.report.targets.support.ReportProcessorUtil;
import org.jfree.xml.factory.objects.ClassFactoryCollector;

/**
 * An application that creates a report documenting the object references.
 * 
 * @author Thomas Morgner
 */
public class ObjectReferenceGenerator
{
  /** The report definition. */
  private static final String REFERENCE_REPORT =
    "/com/jrefinery/report/io/ext/factory/objects/ObjectReferenceReport.xml";

  public static TableModel createData ()
  {
    ClassFactoryCollector cc = new ClassFactoryCollector();
    cc.addFactory(new DefaultClassFactory());
    cc.addFactory(new DefaultDataSourceFactory());
    cc.addFactory(new TemplateClassFactory());

    ObjectReferenceTableModel model = new ObjectReferenceTableModel(cc);
    return model;
  }
  /**
   * Starting point for the application.
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
                                                   + "/object-reference.html");
      ReportProcessorUtil.createPDF(report, 
                                    System.getProperty("user.home") + "/object-reference.pdf");
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
