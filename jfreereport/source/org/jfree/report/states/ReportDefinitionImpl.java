/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * ReportDefinitionImpl.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ReportDefinitionImpl.java,v 1.15 2005/09/20 19:53:05 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.08.2003 : Initial version
 *
 */

package org.jfree.report.states;

import java.util.ResourceBundle;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.DataRow;
import org.jfree.report.Group;
import org.jfree.report.GroupList;
import org.jfree.report.ItemBand;
import org.jfree.report.PageDefinition;
import org.jfree.report.PageFooter;
import org.jfree.report.PageHeader;
import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportFooter;
import org.jfree.report.ReportHeader;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.Watermark;
import org.jfree.report.NoDataBand;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.report.util.ReportProperties;

/**
 * A report definition. This the working copy of the JFreeReport object. This object is
 * not serializable, as it is used internally. This implementation is not intended to be
 * known outside. Whatever you planned to do with it - dont do it!
 * <p/>
 * Its only pupose is to be used and manipulated in the report states, there is no reason
 * to do it outside.
 *
 * @author Thomas Morgner.
 */
public class ReportDefinitionImpl implements ReportDefinition
{
  /**
   * An ordered list of report groups (each group defines its own header and footer).
   */
  private GroupList groups;

  /**
   * The report header band (if not null, printed once at the start of the report).
   */
  private ReportHeader reportHeader;

  /**
   * The report footer band (if not null, printed once at the end of the report).
   */
  private ReportFooter reportFooter;

  /**
   * The page header band (if not null, printed at the start of every page).
   */
  private PageHeader pageHeader;

  /**
   * The page footer band (if not null, printed at the end of every page).
   */
  private PageFooter pageFooter;

  /**
   * The item band - used once for each row of data.
   */
  private ItemBand itemBand;

  /**
   * The watermark acts a global page background. 
   */
  private Watermark watermark;

  /**
   * The watermark acts a global page background.
   */
  private NoDataBand noDataBand;

  /**
   * Storage for arbitrary properties that a user can assign to the report.
   */
  private ReportProperties properties;

  /**
   * The report configuration.
   */
  private final ModifiableConfiguration reportConfiguration;

  /**
   * The stylesheet collection of this report definition.
   */
  private StyleSheetCollection styleSheetCollection;

  /**
   * The datarow connector used to feed all elements.
   */
  private DataRowConnector dataRowConnector;

  /**
   * The page definition defines the output area.
   */
  private PageDefinition pageDefinition;

  /**
   * The ResourceBundleFactory holds internationalization information.
   */
  private ResourceBundleFactory resourceBundleFactory;

  /**
   * Creates a report definition from a report object.
   *
   * @param report the report.
   * @throws CloneNotSupportedException if there is a problem cloning.
   */
  public ReportDefinitionImpl (final ReportDefinition report)
          throws CloneNotSupportedException
  {
    groups = new UnmodifiableGroupList((GroupList) report.getGroups().clone());
    properties = (ReportProperties) report.getProperties().clone();
    reportFooter = (ReportFooter) report.getReportFooter().clone();
    reportHeader = (ReportHeader) report.getReportHeader().clone();
    pageFooter = (PageFooter) report.getPageFooter().clone();
    pageHeader = (PageHeader) report.getPageHeader().clone();
    itemBand = (ItemBand) report.getItemBand().clone();
    watermark = (Watermark) report.getWatermark().clone();
    noDataBand = (NoDataBand) report.getNoDataBand().clone();
    reportConfiguration = report.getReportConfiguration();
    pageDefinition = (PageDefinition) report.getPageDefinition().clone();
    styleSheetCollection = (StyleSheetCollection) report.getStyleSheetCollection().clone();
    resourceBundleFactory = report.getResourceBundleFactory();
    dataRowConnector = new DataRowConnector();

    this.groups.setReportDefinition(this);
    this.reportHeader.setReportDefinition(this);
    this.reportFooter.setReportDefinition(this);
    this.pageHeader.setReportDefinition(this);
    this.pageFooter.setReportDefinition(this);
    this.itemBand.setReportDefinition(this);
    this.watermark.setReportDefinition(this);
  }

  /**
   * Returns the list of groups for the report.
   *
   * @return The list of groups.
   */
  public GroupList getGroups ()
  {
    return groups;
  }

  /**
   * Returns the report header.
   *
   * @return The report header.
   */
  public ReportHeader getReportHeader ()
  {
    return reportHeader;
  }

  /**
   * Returns the report footer.
   *
   * @return The report footer.
   */
  public ReportFooter getReportFooter ()
  {
    return reportFooter;
  }

  /**
   * Returns the page header.
   *
   * @return The page header.
   */
  public PageHeader getPageHeader ()
  {
    return pageHeader;
  }

  /**
   * Returns the page footer.
   *
   * @return The page footer.
   */
  public PageFooter getPageFooter ()
  {
    return pageFooter;
  }

  /**
   * Returns the item band.
   *
   * @return The item band.
   */
  public ItemBand getItemBand ()
  {
    return itemBand;
  }

  /**
   * Returns the "no-data" band, which is displayed if there is no data
   * available.
   *
   * @return The no-data band.
   */
  public NoDataBand getNoDataBand()
  {
    return noDataBand;
  }

  /**
   * Returns the report properties.
   *
   * @return The report properties.
   */
  public ReportProperties getProperties ()
  {
    return properties;
  }

  /**
   * Returns the report configuration.
   *
   * @return The report configuration.
   */
  public ModifiableConfiguration getReportConfiguration ()
  {
    return reportConfiguration;
  }

  /**
   * Returns the number of groups in this report. <P> Every report has at least one group
   * defined.
   *
   * @return the group count.
   */
  public int getGroupCount ()
  {
    return groups.size();
  }

  /**
   * Returns the group at the specified index or null, if there is no such group.
   *
   * @param count the group index.
   * @return the requested group.
   *
   * @throws IllegalArgumentException  if the count is negative.
   * @throws IndexOutOfBoundsException if the count is greater than the number of defined
   *                                   groups.
   */
  public Group getGroup (final int count)
  {
    if (count < 0)
    {
      throw new IllegalArgumentException("GroupCount must not be negative");
    }

    if (count >= groups.size())
    {
      throw new IndexOutOfBoundsException("No such group defined. " + count + " vs. "
              + groups.size());
    }

    return groups.get(count);
  }

  /**
   * Creates and returns a copy of this object.
   *
   * @return a clone of this instance.
   *
   * @throws CloneNotSupportedException if the object's class does not support the
   *                                    <code>Cloneable</code> interface. Subclasses that
   *                                    override the <code>clone</code> method can also
   *                                    throw this exception to indicate that an instance
   *                                    cannot be cloned.
   * @see java.lang.Cloneable
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final ReportDefinitionImpl report = (ReportDefinitionImpl) super.clone();
    report.groups = (GroupList) groups.clone();
    report.itemBand = (ItemBand) itemBand.clone();
    report.pageFooter = (PageFooter) pageFooter.clone();
    report.pageHeader = (PageHeader) pageHeader.clone();
    report.properties = (ReportProperties) properties.clone();
    report.reportFooter = (ReportFooter) reportFooter.clone();
    report.reportHeader = (ReportHeader) reportHeader.clone();
    report.watermark = (Watermark) watermark.clone();
    // pagedefinition is not! cloned ...
    report.pageDefinition = pageDefinition;
    report.styleSheetCollection = (StyleSheetCollection) styleSheetCollection.clone();
    report.dataRowConnector = new DataRowConnector();


    report.groups.setReportDefinition(report);
    report.reportHeader.setReportDefinition(report);
    report.reportFooter.setReportDefinition(report);
    report.pageHeader.setReportDefinition(report);
    report.pageFooter.setReportDefinition(report);
    report.itemBand.setReportDefinition(report);
    report.watermark.setReportDefinition(report);
    return report;
  }

  /**
   * Returns the stylesheet collection of this report definition. The stylesheet
   * collection is fixed for the report definition and all elements of the report. When a
   * band or group is added to the report it will get registered with this stylesheet
   * collection and cannot be used in an different report.
   *
   * @return the stylesheet collection of the report, never null.
   */
  public StyleSheetCollection getStyleSheetCollection ()
  {
    return styleSheetCollection;
  }

  /**
   * Returns the datarow connector used to feed all elements. This instance is not the one
   * used to feed the functions, so elements will always show the old values and never an
   * preview.
   *
   * @return the datarow connector.
   */
  public DataRowConnector getDataRowConnector ()
  {
    return dataRowConnector;
  }

  public Watermark getWatermark ()
  {
    return watermark;
  }

  public DataRow getDataRow ()
  {
    return dataRowConnector;
  }

  public PageDefinition getPageDefinition ()
  {
    return pageDefinition;
  }

  public ResourceBundleFactory getResourceBundleFactory ()
  {
    return resourceBundleFactory;
  }

  public ResourceBundle getResourceBundle (final String identifier)
  {
    return getResourceBundleFactory().getResourceBundle(identifier);
  }
}
