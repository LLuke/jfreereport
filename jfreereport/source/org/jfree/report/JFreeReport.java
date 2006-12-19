/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
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
 * $Id: JFreeReport.java,v 1.39 2006/12/09 21:19:04 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report;

import java.awt.print.PageFormat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.table.TableModel;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.layouting.input.style.CSSPageRule;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.input.style.StyleSheetUtility;
import org.jfree.report.flow.ReportStructureRoot;
import org.jfree.report.structure.ReportDefinition;
import org.jfree.report.util.ReportParameters;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Configuration;

/**
 * A JFreeReport instance is used as report template to define the visual layout
 * of a report and to collect all data sources for the reporting. Possible data
 * sources are the {@link TableModel}, {@link org.jfree.report.expressions.Expression}s
 * or {@link ReportProperties}.
 * <p/>
 * New since 0.9: Report properties contain data. They do not contain processing
 * objects (like the outputtarget) or attribute values. Report properties should
 * only contains things, which are intended for printing.
 * <p/>
 * The report data source is no longer part of the report definition. It is an
 * extra object passed over to the report processor or generated using a report
 * data factory.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class JFreeReport extends ReportDefinition
    implements Serializable, ReportStructureRoot
{
  /**
   * The report configuration.
   */
  private ModifiableConfiguration reportConfiguration;

  private ArrayList styleSheets;
  private StyleSheet pageFormatStyleSheet;
  private CSSPageRule pageRule;

  private ReportParameters parameters;

  private ReportDataFactory dataFactory;

  private ResourceManager resourceManager;
  private ResourceKey baseResource;

  /**
   * The default constructor. Creates an empty but fully initialized report.
   */
  public JFreeReport()
  {
    setType("report");
    this.reportConfiguration = new HierarchicalConfiguration
        (JFreeReportBoot.getInstance().getGlobalConfig());

    this.styleSheets = new ArrayList();
    this.parameters = new ReportParameters();
    this.dataFactory = new EmptyReportDataFactory();

    this.pageFormatStyleSheet = new StyleSheet();
    this.pageRule = new CSSPageRule(pageFormatStyleSheet, null, null, null);

    setQuery("default");
  }

  /**
   * Returns the report configuration.
   * <p/>
   * The report configuration is automatically set up when the report is first
   * created, and uses the global JFreeReport configuration as its parent.
   *
   * @return the report configuration.
   */
  public Configuration getConfiguration()
  {
    return reportConfiguration;
  }

  public void addStyleSheet(StyleSheet s)
  {
    if (s == null)
    {
      throw new NullPointerException();
    }
    styleSheets.add(s);
  }

  public void removeStyleSheet(StyleSheet s)
  {
    styleSheets.remove(s);
  }

  public StyleSheet getStyleSheet(int i)
  {
    if (i == 0)
    {
      return pageFormatStyleSheet;
    }
    return (StyleSheet) styleSheets.get(i - 1);
  }

  public int getStyleSheetCount()
  {
    return styleSheets.size() + 1;
  }

  public JFreeReport getRootReport()
  {
    return this;
  }

  public ReportParameters getInputParameters()
  {
    return parameters;
  }

  public ReportDataFactory getDataFactory()
  {
    return dataFactory;
  }

  public void setDataFactory(final ReportDataFactory dataFactory)
  {
    if (dataFactory == null)
    {
      throw new NullPointerException();
    }

    this.dataFactory = dataFactory;
  }

  public ResourceManager getResourceManager()
  {
    if (resourceManager == null)
    {
      resourceManager = new ResourceManager();
      resourceManager.registerDefaults();
    }
    return resourceManager;
  }

  public void setResourceManager(final ResourceManager resourceManager)
  {
    this.resourceManager = resourceManager;
  }

  public ResourceKey getBaseResource()
  {
    return baseResource;
  }

  public void setBaseResource(final ResourceKey baseResource)
  {
    this.baseResource = baseResource;
  }

  public void setPageFormat(final PageFormat format)
  {
    pageRule.clear();
    StyleSheetUtility.updateRuleForPage(pageRule, format);
  }

  public PageFormat getPageFormat()
  {
    return StyleSheetUtility.getPageFormat(pageRule);
  }

  public ModifiableConfiguration getEditableConfiguration()
  {
    return reportConfiguration;
  }

  public Locale getLocale()
  {
    final Locale locale = super.getLocale();
    if (locale == null)
    {
      return Locale.getDefault();
    }
    return locale;
  }
}
