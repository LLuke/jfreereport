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
 * ZippedHtmlWorldDemo.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.demo.headless;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.jfree.layouting.modules.output.html.HtmlPrinter;
import org.jfree.layouting.modules.output.html.PageableHtmlOutputProcessor;
import org.jfree.layouting.modules.output.html.SingleRepositoryURLRewriter;
import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportException;
import org.jfree.report.TableReportDataFactory;
import org.jfree.report.demo.world.CountryDataTableModel;
import org.jfree.report.flow.DefaultReportJob;
import org.jfree.report.flow.streaming.StreamingReportProcessor;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.DefaultNameGenerator;
import org.jfree.repository.zipwriter.ZipRepository;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 21.02.2006, 14:11:22
 *
 * @author Thomas Morgner
 */
public class ZippedHtmlWorldDemo
{
  private ZippedHtmlWorldDemo()
  {
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    if (args.length == 0)
    {
      System.err.println("Need the target filename as first parameter.");
      return;
    }

    try
    {
      ZippedHtmlWorldDemo.processReport
          ("/org/jfree/report/demo/world/world.xml", args[0]);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private static void processReport(String sourceFile,
                                    String targetFile)
      throws ResourceException,
      ReportException,
      DataSourceException,
      IOException, ContentIOException
  {
    // Step 1: Get a report object.
    URL url = ZippedHtmlWorldDemo.class.getResource(sourceFile);
    ResourceManager manager = new ResourceManager();
    manager.registerDefaults();
    Resource res = manager.createDirectly(url, JFreeReport.class);
    final JFreeReport resource = (JFreeReport) res.getResource();

    // Step 2: Make a job out of it.
    // (There is no need to redeclare the datasource, if the parsed report
    // contains a valid one.)
    DefaultReportJob job = new DefaultReportJob(resource);
    final TableReportDataFactory dataFactory =
        new TableReportDataFactory("default", new CountryDataTableModel());
    job.setDataFactory(dataFactory);

    // And finally: Print ..
    final FileOutputStream fout = new FileOutputStream(new File(targetFile));
    final ZipRepository fileRepository = new ZipRepository(fout);
    final ContentLocation root = fileRepository.getRoot();

    StreamingReportProcessor sp = new StreamingReportProcessor();
    final PageableHtmlOutputProcessor outputProcessor = new PageableHtmlOutputProcessor(job.getConfiguration());
    final HtmlPrinter printer = outputProcessor.getPrinter();
    printer.setContentWriter(root, new DefaultNameGenerator(root, "index"));
    printer.setDataWriter(root, new DefaultNameGenerator(root, "images"));
    printer.setEncoding("ASCII");
    printer.setUrlRewriter(new SingleRepositoryURLRewriter());
    sp.setOutputProcessor(outputProcessor);
    sp.processReport(job);

    fileRepository.close();
    job.close();
    System.exit(0);
  }
}
