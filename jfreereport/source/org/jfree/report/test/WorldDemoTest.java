/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * WorldDemoTest.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: WorldDemoTest.java,v 1.5 2006/11/24 17:12:13 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.test;

import java.net.URL;

import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.TableReportDataFactory;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.modules.gui.swing.preview.PreviewDialog;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 21.02.2006, 14:11:22
 *
 * @author Thomas Morgner
 */
public class WorldDemoTest
{
  private WorldDemoTest()
  {
  }

  public static void main(String[] args)
      throws
      ResourceKeyCreationException,
      ResourceCreationException,
      ResourceLoadingException,
      ReportProcessingException,
      ReportDataFactoryException,
      DataSourceException
  {
    JFreeReportBoot.getInstance().start();

    WorldDemoTest.processReport("/world.xml");
  }

  private static void processReport(String file)
      throws ResourceLoadingException,
      ResourceCreationException,
      ResourceKeyCreationException,
      ReportProcessingException,
      ReportDataFactoryException,
      DataSourceException
  {
    URL url = WorldDemoTest.class.getResource(file);
    ResourceManager manager = new ResourceManager();
    manager.registerDefaults();
    Resource res = manager.createDirectly(url, JFreeReport.class);
    final JFreeReport resource = (JFreeReport) res.getResource();

    ReportJob job = new ReportJob(resource);
    final TableReportDataFactory dataFactory =
            new TableReportDataFactory("default", new CountryDataTableModel());
    job.setDataFactory(dataFactory);

    PreviewDialog dialog = new PreviewDialog();
    dialog.setModal(true);
    dialog.setReportJob(job);
    dialog.setSize(500, 300);
    dialog.setVisible(true);

    dialog = new PreviewDialog();
    dialog.setModal(true);
    dialog.setReportJob(job);
    dialog.setSize(500, 300);
    dialog.setVisible(true);


    System.exit(0);
  }
}
