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
 * ----------------
 * JFreeReport.java
 * ----------------
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReport.java,v 1.31 2006/04/18 11:28:39 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 04-Mar-2002 : Major changes to report engine to incorporate functions and different output
 *               targets (DG);
 * 24-Apr-2002 : ItemBand and Groups are Optional Elements, default Elements are created as needed
 * 01-May-2002 : Renamed addProperty to setProperty to create consistent naming among other
 *               property uses.
 * 07-May-2002 : Fixed bug where last row of data is left off the report if it is alone in a
 *               group, reported by Steven Feinstein (DG);
 * 10-May-2002 : Rewrote report-processing. All reportstate-changes are handled by ReportState
 *               Objects. Created AccessorMethods for Properties. (TM)
 * 11-May-2002 : All bands have to be initialized. Null is no longer allowed for pageHeader,
 *               pageFooter, reportHeader, reportFooter, itemBand, functionCollection.
 * 17-May-2002 : Fixed reportPropertyInitialisation and checked if the report is proceeding on
 *               print.
 * 26-May-2002 : Changed repagination behaviour. Reports are repaginated before printed, so that
 *               global initialisations can be done.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 08-Jun-2002 : The defaultPageFormat is now always filled (and used in PreviewFrame)
 * 19-Jun-2002 : more documentation
 * 03-Jul-2002 : Serializable and cloneable, Removed JFreeReportInfo field, it disrupts the
 *               serializable process
 * 26-Jul-2002 : Removed method "isLastItemInHigherGroups()". The same functionality is implemented
 *               in Group.isLastItemInGroup()
 * 05-Dec-2002 : Updated Javadocs (DG);
 * 03-Jan-2003 : More Javadocs (DG);
 * 05-Feb-2003 : Fixed serialisation problem
 */

package org.jfree.report;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.table.TableModel;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.namespace.DefaultNamespaceCollection;
import org.jfree.report.function.Expression;
import org.jfree.report.i18n.DefaultResourceBundleFactory;
import org.jfree.report.i18n.ResourceBundleFactory;
import org.jfree.report.structure.ReportDefinition;
import org.jfree.report.util.ReportParameters;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;

/**
 * A JFreeReport instance is used as report template to define the visual layout
 * of a report and to collect all data sources for the reporting. Possible data
 * sources are the {@link TableModel}, {@link Expression}s or {@link
 * ReportProperties}.
 * <p/>
 * New since 0.9: Report properties contain data. They do not contain processing
 * objects (like the outputtarget) or attribute values. Report properties should
 * only contains things, which are intended for printing.
 * <p>
 * The report data source is no longer part of the report definition. It is an
 * extra object passed over to the report processor or generated using a
 * report data factory.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class JFreeReport extends ReportDefinition implements Serializable
{
  /** The report configuration. */
  private ModifiableConfiguration reportConfiguration;

  private ArrayList styleSheets;

  private ReportParameters parameters;

  /** The resource bundle factory is used when generating localized reports. */
  private ResourceBundleFactory resourceBundleFactory;

  private ReportDataFactory dataFactory;

  private ResourceManager resourceManager;
  private ResourceKey baseResource;

  /** The default constructor. Creates an empty but fully initialized report. */
  public JFreeReport()
  {
    this.reportConfiguration = new HierarchicalConfiguration
            (JFreeReportBoot.getInstance().getGlobalConfig());

    this.styleSheets = new ArrayList();
    this.parameters = new ReportParameters();
    this.resourceBundleFactory = new DefaultResourceBundleFactory();
  }

  /**
   * Redefines the resource bundle factory for the report.
   *
   * @param resourceBundleFactory the new resource bundle factory, never null.
   * @throws NullPointerException if the given ResourceBundleFactory is null.
   */
  public void setResourceBundleFactory
          (final ResourceBundleFactory resourceBundleFactory)
  {
    if (resourceBundleFactory == null)
    {
      throw new NullPointerException("ResourceBundleFactory must not be null");
    }
    this.resourceBundleFactory = resourceBundleFactory;
  }

  public ResourceBundleFactory getResourceBundleFactory()
  {
    return resourceBundleFactory;
  }

  /**
   * Returns the report configuration.
   * <p/>
   * The report configuration is automatically set up when the report is first
   * created, and uses the global JFreeReport configuration as its parent.
   *
   * @return the report configuration.
   */
  public ModifiableConfiguration getConfiguration()
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
    return (StyleSheet) styleSheets.get(i);
  }

  public int getStyleSheetCount()
  {
    return styleSheets.size();
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
    this.dataFactory = dataFactory;
  }

  public ResourceManager getResourceManager()
  {
    if (resourceManager == null)
    {
      if (baseResource == null)
      {
        resourceManager = new ResourceManager();
        resourceManager.registerDefaults();
      }
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
}
