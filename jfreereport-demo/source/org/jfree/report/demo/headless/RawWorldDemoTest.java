/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * RawWorldDemoTest.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.demo.headless;

import java.net.URL;

import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.TableReportDataFactory;
import org.jfree.report.demo.world.CountryDataTableModel;
import org.jfree.report.flow.DefaultReportJob;
import org.jfree.report.flow.raw.RawReportProcessor;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 21.02.2006, 14:11:22
 *
 * @author Thomas Morgner
 */
public class RawWorldDemoTest
{
  private RawWorldDemoTest()
  {
  }


  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    try
    {
      RawWorldDemoTest.processReport
          ("/org/jfree/report/demo/world/world.xml");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private static void processReport(String file)
      throws ResourceException,
      ReportDataFactoryException, DataSourceException, ReportProcessingException
  {
    URL url = org.jfree.report.demo.headless.RawWorldDemoTest.class.getResource(file);
    ResourceManager manager = new ResourceManager();
    manager.registerDefaults();
    Resource res = manager.createDirectly(url, JFreeReport.class);
    final JFreeReport resource = (JFreeReport) res.getResource();

    DefaultReportJob job = new DefaultReportJob(resource);
    final TableReportDataFactory dataFactory =
            new TableReportDataFactory("default", new CountryDataTableModel());
    job.setDataFactory(dataFactory);

    final RawReportProcessor rp = new RawReportProcessor();
    rp.processReport(job);
    job.close();

  }
}
