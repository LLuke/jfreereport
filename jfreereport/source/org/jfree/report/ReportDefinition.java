/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ---------------------
 * ReportDefinition.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ReportDefinition.java,v 1.13 2005/09/07 14:23:49 taqua Exp $
 *
 * Changes
 * -------
 * 05.04.2003 : Initial version
 *
 */

package org.jfree.report;

import java.util.ResourceBundle;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.report.util.ReportProperties;

/**
 * A report definition. This the working copy of the JFreeReport object. This object is
 * not serializable, as it is used internally.
 *
 * @author Thomas Morgner.
 */
public interface ReportDefinition extends Cloneable
{
  /**
   * Returns the list of groups for the report.
   *
   * @return The list of groups.
   */
  public GroupList getGroups ();

  /**
   * Returns the report header.
   *
   * @return The report header.
   */
  public ReportHeader getReportHeader ();

  /**
   * Returns the report footer.
   *
   * @return The report footer.
   */
  public ReportFooter getReportFooter ();

  /**
   * Returns the page header.
   *
   * @return The page header.
   */
  public PageHeader getPageHeader ();

  /**
   * Returns the page footer.
   *
   * @return The page footer.
   */
  public PageFooter getPageFooter ();

  /**
   * Returns the item band.
   *
   * @return The item band.
   */
  public ItemBand getItemBand ();

  /**
   * Returns the watermark band.
   *
   * @return The watermark band.
   */
  public Watermark getWatermark ();

  /**
   * Returns the "no-data" band, which is displayed if there is no data available.
   *
   * @return The no-data band.
   */
  public NoDataBand getNoDataBand();

  /**
   * Returns the report properties.
   *
   * @return The report properties.
   */
  public ReportProperties getProperties ();

  /**
   * Returns the report configuration.
   *
   * @return The report configuration.
   */
  public ModifiableConfiguration getReportConfiguration ();

  /**
   * Returns the number of groups in this report. <P> Every report has at least one group
   * defined.
   *
   * @return the group count.
   */
  public int getGroupCount ();

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
  public Group getGroup (int count);

  /**
   * Returns the stylesheet collection of this report definition. The stylesheet
   * collection is fixed for the report definition and all elements of the report. When a
   * band or group is added to the report it will get registered with this stylesheet
   * collection and cannot be used in an different report.
   *
   * @return the stylesheet collection of the report, never null.
   */
  public StyleSheetCollection getStyleSheetCollection ();

  /**
   * Returns the datarow assigned to this report definition. For report instances
   * not yet started, this returns a dummy instance.
   *
   * @return the datarow assigned to the report, never null.
   */
  public DataRow getDataRow ();

  /**
   * Returns the page definition assigned to the report definition. The page
   * definition defines the report area and how the report is subdivided by
   * the child pages.
   *
   * @return the page definition.
   */
  public PageDefinition getPageDefinition ();

  /**
   * Returns a resource bundle assigned with the given identifier. This
   * is a convienience method and therefore equal with
   * <code>getResourceBundleFactory().getResourceBundle(..)</code>.
   *
   * @param identifier the resource bundle identifier
   * @return the resource bundle or null, if no resource bundle could
   * be loaded.
   */
  public ResourceBundle getResourceBundle (String identifier);

  /**
   * Returns the ResourceBundleFactory assigned to this report definition.
   * @return the resourcebundlefactory.
   */
  public ResourceBundleFactory getResourceBundleFactory ();
}
