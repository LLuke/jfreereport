/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -----------------
 * ReportWriter.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportWriter.java,v 1.16 2003/06/29 16:59:27 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.parser.ext.writer;

import java.io.IOException;
import java.io.Writer;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceFactory;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactory;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollection;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.factory.objects.ClassFactoryCollector;

/**
 * A report writer.
 *
 * @author Thomas Morgner
 */
public class ReportWriter
{
  /** A data-source collector. */
  private DataSourceCollector dataSourceCollector;

  /** An element factory collector. */
  private ElementFactoryCollector elementFactoryCollector;

  /** A class factory collector. */
  private ClassFactoryCollector classFactoryCollector;

  /** A style-key factory collector. */
  private StyleKeyFactoryCollector styleKeyFactoryCollector;

  /** A template collector. */
  private TemplateCollector templateCollector;

  /** The report. */
  private JFreeReport report;

  /** The encoding. */
  private String encoding;

  /**
   * Creates a new report writer for a report.
   *
   * @param report  the report.
   * @param encoding  the encoding.
   */
  public ReportWriter(final JFreeReport report, final String encoding)
  {
    dataSourceCollector = new DataSourceCollector();
    elementFactoryCollector = new ElementFactoryCollector();
    classFactoryCollector = new ClassFactoryCollector();
    classFactoryCollector.addFactory(dataSourceCollector);
    styleKeyFactoryCollector = new StyleKeyFactoryCollector();
    templateCollector = new TemplateCollector();
    this.report = report;
    this.encoding = encoding;
  }

  /**
   * Returns the encoding.
   *
   * @return The encoding.
   */
  public String getEncoding()
  {
    return encoding;
  }

  /**
   * Adds a data-source factory.
   *
   * @param dsf  the data-source factory.
   */
  public void addDataSourceFactory(final DataSourceFactory dsf)
  {
    dataSourceCollector.addFactory(dsf);
  }

  /**
   * Returns the data-source collector.
   *
   * @return The data-source collector.
   */
  public DataSourceCollector getDataSourceCollector()
  {
    return dataSourceCollector;
  }

  /**
   * Adds an element factory.
   *
   * @param ef  the element factory.
   */
  public void addElementFactory(final ElementFactory ef)
  {
    elementFactoryCollector.addFactory(ef);
  }

  /**
   * Returns the element factory collector.
   *
   * @return The element factory collector.
   */
  public ElementFactoryCollector getElementFactoryCollector()
  {
    return elementFactoryCollector;
  }

  /**
   * Adds a class factory.
   *
   * @param cf  the class factory.
   */
  public void addClassFactoryFactory(final ClassFactory cf)
  {
    classFactoryCollector.addFactory(cf);
  }

  /**
   * Returns the class factory collector.
   *
   * @return The class factory collector.
   */
  public ClassFactoryCollector getClassFactoryCollector()
  {
    return classFactoryCollector;
  }

  /**
   * Adds a style-key factory.
   *
   * @param skf  the style-key factory.
   */
  public void addStyleKeyFactory(final StyleKeyFactory skf)
  {
    styleKeyFactoryCollector.addFactory(skf);
  }

  /**
   * Returns the style-key factory collector.
   *
   * @return The style-key factory collector.
   */
  public StyleKeyFactoryCollector getStyleKeyFactoryCollector()
  {
    return styleKeyFactoryCollector;
  }

  /**
   * Adds a template collection.
   *
   * @param collection  the template collection.
   */
  public void addTemplateCollection(final TemplateCollection collection)
  {
    templateCollector.addTemplateCollection(collection);
  }

  /**
   * Returns the template collector.
   *
   * @return The template collector.
   */
  public TemplateCollector getTemplateCollector()
  {
    return templateCollector;
  }

  /**
   * Returns the report.
   *
   * @return The report.
   */
  public JFreeReport getReport()
  {
    return report;
  }

  /**
   * Writes a report to a character stream writer.
   *
   * @param w  the character stream writer.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write(final Writer w) throws IOException, ReportWriterException
  {
    final ReportDefinitionWriter writer = new ReportDefinitionWriter(this);

    // configure all factories with the current report configuration ...
    dataSourceCollector.configure(report.getReportConfiguration());
    classFactoryCollector.configure(report.getReportConfiguration());
    templateCollector.configure(report.getReportConfiguration());
    writer.write(w); // we start with indentation level 0
  }

}
