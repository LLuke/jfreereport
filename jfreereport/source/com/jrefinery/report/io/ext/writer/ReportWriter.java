/**
 * Date: Jan 13, 2003
 * Time: 12:39:06 PM
 *
 * $Id: ReportWriter.java,v 1.2 2003/01/14 21:10:01 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.io.ext.factory.datasource.DataSourceCollector;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceFactory;
import com.jrefinery.report.io.ext.factory.elements.ElementFactoryCollector;
import com.jrefinery.report.io.ext.factory.elements.ElementFactory;
import com.jrefinery.report.io.ext.factory.objects.ClassFactoryCollector;
import com.jrefinery.report.io.ext.factory.objects.ClassFactory;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyFactoryCollector;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyFactory;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollector;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollection;
import com.jrefinery.report.JFreeReport;

import java.io.Writer;
import java.io.IOException;

public class ReportWriter
{
  private DataSourceCollector dataSourceCollector;
  private ElementFactoryCollector elementFactoryCollector;
  private ClassFactoryCollector classFactoryCollector;
  private StyleKeyFactoryCollector styleKeyFactoryCollector;
  private TemplateCollector templateCollector;
  private JFreeReport report;

  public ReportWriter (JFreeReport report)
  {
    dataSourceCollector = new DataSourceCollector();
    elementFactoryCollector = new ElementFactoryCollector();
    classFactoryCollector = new ClassFactoryCollector();
    classFactoryCollector.addFactory(dataSourceCollector);
    styleKeyFactoryCollector = new StyleKeyFactoryCollector();
    templateCollector = new TemplateCollector();
    this.report = report;
  }

  public void addDataSourceFactory (DataSourceFactory dsf)
  {
    dataSourceCollector.addFactory(dsf);
  }

  public DataSourceCollector getDataSourceCollector()
  {
    return dataSourceCollector;
  }

  public void addElementFactory (ElementFactory dsf)
  {
    elementFactoryCollector.addFactory(dsf);
  }

  public ElementFactoryCollector getElementFactoryCollector()
  {
    return elementFactoryCollector;
  }

  public void addClassFactoryFactory (ClassFactory dsf)
  {
    classFactoryCollector.addFactory(dsf);
  }

  public ClassFactoryCollector getClassFactoryCollector()
  {
    return classFactoryCollector;
  }

  public void addStyleKeyFactory (StyleKeyFactory dsf)
  {
    styleKeyFactoryCollector.addFactory(dsf);
  }

  public StyleKeyFactoryCollector getStyleKeyFactoryCollector()
  {
    return styleKeyFactoryCollector;
  }

  public void addTemplateCollection (TemplateCollection dsf)
  {
    templateCollector.addTemplateCollection(dsf);
  }

  public TemplateCollector getTemplateCollector()
  {
    return templateCollector;
  }

  public JFreeReport getReport()
  {
    return report;
  }

  public void write (Writer w) throws IOException, ReportWriterException
  {
    ReportDefinitionWriter writer = new ReportDefinitionWriter(this);
    writer.write(w);
  }

}
