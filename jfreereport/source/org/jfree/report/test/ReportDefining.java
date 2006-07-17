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
 * ReportDefining.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ReportDefining.java,v 1.4 2006/07/11 13:24:40 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.test;

import java.net.URL;

import org.jfree.layouting.output.junit.StageOnePageableOutputProcessor;
import org.jfree.layouting.output.streaming.html.HtmlOutputProcessor;
import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.TableReportDataFactory;
import org.jfree.report.flow.FlowControlOperation;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.flow.flowing.FlowReportProcessor;
import org.jfree.report.flow.streaming.StreamingReportProcessor;
import org.jfree.report.function.aggregation.ItemCountFunction;
import org.jfree.report.function.sys.GetValueExpression;
import org.jfree.report.function.sys.GroupByExpression;
import org.jfree.report.structure.ContentElement;
import org.jfree.report.structure.Group;
import org.jfree.report.structure.Node;
import org.jfree.report.structure.Section;
import org.jfree.report.structure.StaticText;
import org.jfree.report.structure.SubReport;
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
public class ReportDefining
{
  private ReportDefining()
  {
  }

  public static void main(String[] args)
          throws ReportDataFactoryException, DataSourceException,
          ResourceKeyCreationException, ResourceCreationException,
          ResourceLoadingException, ReportProcessingException
  {
    JFreeReportBoot.getInstance().start();
    
//    {
//      URL url = ReportDefining.class.getResource("/sample.sqlds");
//      ResourceManager manager = new ResourceManager();
//      manager.registerDefaults();
//      Resource res = manager.createDirectly(url, ReportDataFactory.class);
//    }

    processFlowReport();

//    long startTime = System.currentTimeMillis();
//    final int reportCount = 250;
//    for (int i = 0; i < reportCount; i++)
//    {
//      processReport();
//    }
//    long endTime = System.currentTimeMillis();
//
//    long delta = endTime - startTime;
//    System.out.println("Total-Time for " + reportCount + " Reports: " + delta / 1000f);
//    System.out.println("Time per Report " + (delta / (reportCount * 1000f)));

//
//    JFreeReport report = new JFreeReport();
//    report.setQuery("default");
//
//    report.addNode(new StaticText("Hey look, this is my report header. "));
//    report.addNode(createGroup());
//    report.addNode(new StaticText("Hey look, this is my report footer. "));
//
//    // startup: Create an initial datarow.
//    final TableReportDataFactory dataFactory =
//            new TableReportDataFactory("default", new CountryDataTableModel());
//    dataFactory.addTable("subreport", new WorldDataTableModel());
//    job.setDataFactory(dataFactory);
//
//    final HtmlOutputProcessor out = new HtmlOutputProcessor(System.out);
//    final StreamingReportProcessor rp = new StreamingReportProcessor();
//    rp.setOutputProcessor(out);
//
//    long startTime = System.currentTimeMillis();
//    rp.processReport(job);
//    long endTime = System.currentTimeMillis();
//
//    long delta = endTime - startTime;
//    System.out.println("Time: " + delta / 1000f);
  }

  private static void processReport()
          throws ResourceLoadingException,
          ResourceCreationException, ResourceKeyCreationException,
          ReportDataFactoryException, DataSourceException, ReportProcessingException
  {
    URL url = ReportDefining.class.getResource("/baseline.xml");
    ResourceManager manager = new ResourceManager();
    manager.registerDefaults();
    Resource res = manager.createDirectly(url, JFreeReport.class);
    final JFreeReport resource = (JFreeReport) res.getResource();

//    final JFreeReport resource = new JFreeReport();
//    resource.setQuery("default");
//    resource.addNode(new StaticText ("Blah"));
//
//    final Section sect = new Section();
//    sect.addNode(new StaticText ("bbbb"));
//    resource.addNode(sect);
//    resource.addNode(new StaticText ("Blah2"));

    ReportJob job = new ReportJob(resource);
    final TableReportDataFactory dataFactory =
            new TableReportDataFactory("default", new CountryDataTableModel());
    dataFactory.addTable("subreport", new WorldDataTableModel());
    job.setDataFactory(dataFactory);

//    final OOWriterOutputProcessor out = new OOWriterOutputProcessor();
    final HtmlOutputProcessor out = new HtmlOutputProcessor(System.err);
    //job.setMetaData(out.getMetaData());

    final StreamingReportProcessor rp = new StreamingReportProcessor();
    rp.setOutputProcessor(out);
    rp.processReport(job);
  }


  private static void processFlowReport()
          throws ResourceLoadingException,
          ResourceCreationException, ResourceKeyCreationException,
          ReportDataFactoryException, DataSourceException, ReportProcessingException
  {
    URL url = ReportDefining.class.getResource("/newreport.xml");
    ResourceManager manager = new ResourceManager();
    manager.registerDefaults();
    Resource res = manager.createDirectly(url, JFreeReport.class);
    final JFreeReport resource = (JFreeReport) res.getResource();

    ReportJob job = new ReportJob(resource);
    final TableReportDataFactory dataFactory =
            new TableReportDataFactory("default", new CountryDataTableModel());
    dataFactory.addTable("subreport", new WorldDataTableModel());
    job.setDataFactory(dataFactory);

//    final OOWriterOutputProcessor out = new OOWriterOutputProcessor();
    final StageOnePageableOutputProcessor out = new StageOnePageableOutputProcessor();
    //job.setMetaData(out.getMetaData());

    final FlowReportProcessor rp = new FlowReportProcessor();
    rp.setOutputProcessor(out);
    rp.processReport(job);
  }


  private static Group createGroup()
  {
    GroupByExpression ge = new GroupByExpression();
    ge.setField(0, "Continent");

    Group group = new Group();
    group.setName("Continent Group");
    group.setGroupingExpression(ge);
    group.addVariable("iCountDetail");

    ContentElement countryElementH = new ContentElement();
    countryElementH.setValueExpression(new GetValueExpression("Country"));

    ContentElement countryElementF = new ContentElement();
    countryElementF.setValueExpression(new GetValueExpression("iCountDetail"));

    Section paragraph = new Section();
    paragraph.addExpression(new ItemCountFunction("iCountGroup"));
    paragraph.setType("p");
    paragraph.setNamespace("http://www.w3c.org/xhtml");
    paragraph.addNode(new StaticText("Group header: "));
    paragraph.addNode(countryElementH);
//    paragraph.addNode(createSubReport());
    paragraph.addNode(new StaticText("\n"));
    paragraph.addNode(createDetail());
    paragraph.addNode(new StaticText("Group Footer: "));
    paragraph.addNode(countryElementF);

    group.addNode(paragraph);
    group.setRepeat(true);
    return group;
  }

  private static Node createSubReport()
  {
    SubReport report = new SubReport();
    report.setName("subreport");
    report.setQuery("subreport");

    ContentElement continentElement = new ContentElement();
    continentElement.setValueExpression(new GetValueExpression("WOCountry"));

    ContentElement countryElement = new ContentElement();
    countryElement.setValueExpression(new GetValueExpression("WOPopulation"));

    Section detailSection = new Section();
    detailSection.setType("p");
    detailSection.setNamespace("http://www.w3c.org/xhtml");
    detailSection.addOperationAfter(FlowControlOperation.ADVANCE);
    detailSection.setName("SUBREPORT: Detail Section");
    detailSection.addNode(continentElement);
    detailSection.addNode(new StaticText(" - "));
    detailSection.addNode(countryElement);
    detailSection.addNode(new StaticText("\n"));
    detailSection.setRepeat(true);
    report.addNode(detailSection);

    return report;
  }

  private static Section createDetail()
  {
    ContentElement continentElement = new ContentElement();
    continentElement.setValueExpression(new GetValueExpression("Continent"));

    ContentElement countryElement = new ContentElement();
    countryElement.setValueExpression(new GetValueExpression("Country"));

    // this must always return '1', as the function is only valid within
    // the element's scope. (Stupid example, but shows how it works ...)
    ContentElement iCountElement = new ContentElement();
    iCountElement.setValueExpression(new ItemCountFunction());

    ContentElement iCountGroupElement = new ContentElement();
    iCountGroupElement.setValueExpression
            (new GetValueExpression("iCountGroup"));

    ContentElement iCountDetailElement = new ContentElement();
    iCountDetailElement.setValueExpression
            (new GetValueExpression("iCountDetail"));

    Section detailSection = new Section();
    detailSection.addExpression(new ItemCountFunction("iCountDetail"));
    detailSection.addOperationAfter(FlowControlOperation.ADVANCE);
    detailSection.setName("Detail Section");
    detailSection.addNode(continentElement);
    detailSection.addNode(new StaticText(" - "));
    detailSection.addNode(countryElement);
    detailSection.addNode(new StaticText(" : "));
    detailSection.addNode(iCountElement);
    detailSection.addNode(new StaticText(" : "));
    detailSection.addNode(iCountGroupElement);
    detailSection.addNode(new StaticText(" = "));
    detailSection.addNode(iCountDetailElement);
    detailSection.addNode(new StaticText("\n"));
    detailSection.setRepeat(true);
    return detailSection;
  }
}
