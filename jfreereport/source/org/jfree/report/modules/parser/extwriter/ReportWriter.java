/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: ReportWriter.java,v 1.4 2003/08/25 14:29:33 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportBuilderHints;
import org.jfree.report.modules.parser.ext.ParserConfigHandler;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceFactory;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactory;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollection;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.util.Configuration;
import org.jfree.xml.Parser;
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

  /** The report writer configuration used during writing. */
  private Configuration configuration;

  /**
   * Builds a default configuration from a given report definition object.
   * <p>
   * This will only create a valid definition, if the report properties were
   * filled by the parser and contain the key <code>report.definition.contentbase</code>.
   *
   * @param report the report for which to create the writer configuration.
   * @return the generated configuration.
   */
  public static Configuration createDefaultConfiguration(final JFreeReport report)
  {
    final ReportConfiguration repConf = new ReportConfiguration(report.getReportConfiguration());
    repConf.setConfigProperty
        (Parser.CONTENTBASE_KEY,
            (String) report.getProperty(JFreeReport.REPORT_DEFINITION_CONTENTBASE));
    repConf.setConfigProperty
        (JFreeReport.REPORT_DEFINITION_CONTENTBASE,
            (String) report.getProperty(JFreeReport.REPORT_DEFINITION_CONTENTBASE));
    repConf.setConfigProperty
        (JFreeReport.REPORT_DEFINITION_SOURCE,
            (String) report.getProperty(JFreeReport.REPORT_DEFINITION_SOURCE));
    return repConf;
  }

  /**
   * Creates a new report writer for a report.
   *
   * @param report  the report.
   * @param encoding  the encoding.
   * @param config the write configuration.
   */
  public ReportWriter(final JFreeReport report, final String encoding, final Configuration config)
  {
    if (report == null)
    {
      throw new NullPointerException("Report is null");
    }
    if (encoding == null)
    {
      throw new NullPointerException("Encoding is null.");
    }
    if (config == null)
    {
      throw new NullPointerException("Configuration is null.");
    }
    if (config.getConfigProperty(Parser.CONTENTBASE_KEY) == null)
    {
      throw new IllegalStateException
          ("This report writer configuration does not define a content base.");
    }

    this.report = report;
    this.encoding = encoding;
    this.configuration = config;

    dataSourceCollector = new DataSourceCollector();
    elementFactoryCollector = new ElementFactoryCollector();
    classFactoryCollector = new ClassFactoryCollector();
    classFactoryCollector.addFactory(dataSourceCollector);
    styleKeyFactoryCollector = new StyleKeyFactoryCollector();
    templateCollector = new TemplateCollector();

    loadObjectFactories();
    loadDataSourceFactories();
    loadElementFactories();
    loadStyleKeyFactories();
    loadTemplateFactories();

    // configure all factories with the current report configuration ...
    dataSourceCollector.configure(configuration);
    classFactoryCollector.configure(configuration);
    templateCollector.configure(configuration);
  }

  /**
   * Loads all object factories from the parser hints, if available.
   */
  private void loadObjectFactories()
  {
    final ReportBuilderHints hints = getReport().getReportBuilderHints();
    final List l = (List) 
      hints.getHint(getReport(), ParserConfigHandler.OBJECT_FACTORY_HINT, List.class);
    if (l == null)
    {
      return;
    }
    final ClassFactory[] list = (ClassFactory[]) loadParserHintFactories(l, ClassFactory.class);
    for (int i = 0; i < list.length; i++)
    {
      addClassFactoryFactory(list[i]);
    }
  }

  /**
   * Loads all datasource factories from the parser hints, if available.
   */
  private void loadDataSourceFactories()
  {
    final ReportBuilderHints hints = getReport().getReportBuilderHints();
    final List l = (List) hints.getHint(getReport(),
        ParserConfigHandler.DATASOURCE_FACTORY_HINT, List.class);
    if (l == null)
    {
      return;
    }
    final DataSourceFactory[] list = (DataSourceFactory[])
        loadParserHintFactories(l, DataSourceFactory.class);
    for (int i = 0; i < list.length; i++)
    {
      addDataSourceFactory(list[i]);
    }
  }

  /**
   * Loads all template factories from the parser hints, if available.
   */
  private void loadTemplateFactories()
  {
    final ReportBuilderHints hints = getReport().getReportBuilderHints();
    final List l = (List) hints.getHint(getReport(),
        ParserConfigHandler.TEMPLATE_FACTORY_HINT, List.class);
    if (l == null)
    {
      return;
    }
    final TemplateCollection[] list = (TemplateCollection[])
        loadParserHintFactories(l, TemplateCollection.class);
    for (int i = 0; i < list.length; i++)
    {
      addTemplateCollection(list[i]);
    }
  }

  /**
   * Loads all element factories from the parser hints, if available.
   */
  private void loadElementFactories()
  {
    final ReportBuilderHints hints = getReport().getReportBuilderHints();
    final List l = (List) hints.getHint(getReport(),
        ParserConfigHandler.ELEMENT_FACTORY_HINT, List.class);
    if (l == null)
    {
      return;
    }
    final ElementFactory[] list = 
      (ElementFactory[]) loadParserHintFactories(l, ElementFactory.class);
    for (int i = 0; i < list.length; i++)
    {
      addElementFactory(list[i]);
    }
  }

  /**
   * Loads all style key factories from the parser hints, if available.
   */
  private void loadStyleKeyFactories()
  {
    final ReportBuilderHints hints = getReport().getReportBuilderHints();
    final List l = (List) hints.getHint(getReport(),
        ParserConfigHandler.STYLEKEY_FACTORY_HINT, List.class);
    if (l == null)
    {
      return;
    }
    final StyleKeyFactory[] list = 
      (StyleKeyFactory[]) loadParserHintFactories(l, StyleKeyFactory.class);
    for (int i = 0; i < list.length; i++)
    {
      addStyleKeyFactory(list[i]);
    }
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
   * Loads a set of factories from the given list of class names and checks, whether
   * the referenced classes are assignable from the given factory type.
   *
   * @param hints the list of class names to load
   * @param factoryType the desired factory type.
   * @return the loaded factories as object array.
   */
  private Object[] loadParserHintFactories(final List hints, final Class factoryType)
  {
    final Object[] hintValues = hints.toArray();
    final ArrayList factories = new ArrayList(hintValues.length);
    for (int i = 0; i < hintValues.length; i++)
    {
      if (hintValues[i] instanceof String == false)
      {
        Log.warn(new Log.SimpleMessage
            ("Invalid parser hint type for factory: ", factoryType,
                ": Type found: ", hintValues[i]));
        continue;
      }

      try
      {
        final Class c = getClass().getClassLoader().loadClass((String) hintValues[i]);
        if (factoryType.isAssignableFrom(c) == false)
        {
          Log.warn(new Log.SimpleMessage
              ("Invalid factory type specified: Required ", factoryType,
                  " but found ", c));
          continue;
        }

        final Object o = c.newInstance();
        if (factories.contains(o) == false)
        {
          factories.add(o);
        }
      }
      catch (Exception e)
      {
        Log.warn("Error while applying parser hints: ", e);
      }
    }

    return factories.toArray((Object[]) Array.newInstance(factoryType, factories.size()));
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
    writer.write(w); // we start with indentation level 0
  }

  /**
   * Returns the configuration used to write the report.
   * @return the writer configuration.
   */
  public Configuration getConfiguration()
  {
    return configuration;
  }
}
