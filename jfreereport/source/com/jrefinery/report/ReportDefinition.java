/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ---------------------
 * ReportDefinition.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportDefinition.java,v 1.4 2003/06/15 19:05:41 taqua Exp $
 *
 * Changes
 * -------
 * 05.04.2003 : Initial version
 * 
 */

package com.jrefinery.report;

import java.util.List;

import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.style.StyleSheetCollection;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.report.util.ReportProperties;

/**
 * A report definition. This the working copy of the JFreeReport object. This object
 * is not serializable, as it is used internally.
 * 
 * @author Thomas Morgner.
 */
public class ReportDefinition implements Cloneable
{
  /** An ordered list of report groups (each group defines its own header and footer). */
  private GroupList groups;

  /** The report header band (if not null, printed once at the start of the report). */
  private ReportHeader reportHeader;

  /** The report footer band (if not null, printed once at the end of the report). */
  private ReportFooter reportFooter;

  /** The page header band (if not null, printed at the start of every page). */
  private PageHeader pageHeader;

  /** The page footer band (if not null, printed at the end of every page). */
  private PageFooter pageFooter;

  /** The item band - used once for each row of data. */
  private ItemBand itemBand;

  /** Storage for arbitrary properties that a user can assign to the report. */
  private ReportProperties properties;

  /** The report configuration. */
  private ReportConfiguration reportConfiguration;

  /** The StyleSheet collection can be used to access stylesheets by their name. */
  private StyleSheetCollection styleSheetCollection;
  
  /**
   * Creates a report definition from a report object.
   * 
   * @param report  the report.
   * 
   * @throws CloneNotSupportedException if there is a problem cloning.
   */
  public ReportDefinition(JFreeReport report) throws CloneNotSupportedException
  {
    groups = new UnmodifiableGroupList((GroupList) report.getGroups().clone());
    properties = (ReportProperties) report.getProperties().clone();
    reportFooter = (ReportFooter) report.getReportFooter().clone();
    reportHeader = (ReportHeader) report.getReportHeader().clone();
    pageFooter = (PageFooter) report.getPageFooter().clone();
    pageHeader = (PageHeader) report.getPageHeader().clone();
    itemBand = (ItemBand) report.getItemBand().clone();
    reportConfiguration = report.getReportConfiguration();
    styleSheetCollection = new StyleSheetCollection();
    collectStyles();
  }

  /**
   * Returns the list of groups for the report.
   * 
   * @return The list of groups.
   */
  public GroupList getGroups()
  {
    return groups;
  }

  /**
   * Returns the report header.
   * 
   * @return The report header.
   */
  public ReportHeader getReportHeader()
  {
    return reportHeader;
  }

  /**
   * Returns the report footer.
   * 
   * @return The report footer.
   */
  public ReportFooter getReportFooter()
  {
    return reportFooter;
  }

  /**
   * Returns the page header.
   * 
   * @return The page header.
   */
  public PageHeader getPageHeader()
  {
    return pageHeader;
  }

  /**
   * Returns the page footer.
   * 
   * @return The page footer.
   */
  public PageFooter getPageFooter()
  {
    return pageFooter;
  }

  /**
   * Returns the item band.
   * 
   * @return The item band.
   */
  public ItemBand getItemBand()
  {
    return itemBand;
  }

  /**
   * Returns the report properties.
   * 
   * @return The report properties.
   */
  public ReportProperties getProperties()
  {
    return properties;
  }

  /**
   * Returns the report configuration.
   * 
   * @return The report configuration.
   */
  public ReportConfiguration getReportConfiguration()
  {
    return reportConfiguration;
  }

  /**
   * Returns the number of groups in this report.
   * <P>
   * Every report has at least one group defined.
   *
   * @return the group count.
   */
  public int getGroupCount ()
  {
    return groups.size ();
  }

  /**
   * Returns the group at the specified index or null, if there is no such group.
   *
   * @param count  the group index.
   *
   * @return the requested group.
   *
   * @throws IllegalArgumentException if the count is negative.
   * @throws IndexOutOfBoundsException if the count is greater than the number of defined groups.
   */
  public Group getGroup (int count)
  {
    if (count < 0)
    {
      throw new IllegalArgumentException ("GroupCount must not be negative");
    }

    if (count >= groups.size ())
    {
      throw new IndexOutOfBoundsException ("No such group defined. " + count + " vs. "
                                           + groups.size ());
    }

    return groups.get (count);
  }

  /**
   * Creates and returns a copy of this object.
   *
   * @return     a clone of this instance.
   * @exception  CloneNotSupportedException  if the object's class does not
   *               support the <code>Cloneable</code> interface. Subclasses
   *               that override the <code>clone</code> method can also
   *               throw this exception to indicate that an instance cannot
   *               be cloned.
   * @see Cloneable
   */
  public Object clone() throws CloneNotSupportedException
  {
    ReportDefinition report = (ReportDefinition) super.clone();
    report.groups = (GroupList) groups.clone ();
    report.itemBand = (ItemBand) itemBand.clone ();
    report.pageFooter = (PageFooter) pageFooter.clone ();
    report.pageHeader = (PageHeader) pageHeader.clone ();
    report.properties = (ReportProperties) properties.clone ();
    report.reportFooter = (ReportFooter) reportFooter.clone ();
    report.reportHeader = (ReportHeader) reportHeader.clone ();
    report.styleSheetCollection = new StyleSheetCollection();
    report.updateStyles();
    return report;
  }

  /**
   * Returns the style sheet collection assigned with this report definition.
   * The style sheet collection can be used to access inherited stylesheets by
   * their name.
   *
   * @return the StyleSheetCollection of this report definition.
   */
  public StyleSheetCollection getStyleSheetCollection()
  {
    return styleSheetCollection;
  }

  /**
   * Updates all styles from all the bands in the report after cloning.
   */
  protected void updateStyles ()
  {
    updateStylesFromBand(getReportHeader());
    updateStylesFromBand(getReportFooter());
    updateStylesFromBand(getPageHeader());
    updateStylesFromBand(getPageFooter());
    updateStylesFromBand(getItemBand());
    for (int i = 0; i < getGroupCount(); i++)
    {
      Group g = getGroup(i);
      updateStylesFromBand(g.getHeader());
      updateStylesFromBand(g.getFooter());
    }

  }

  /**
   * Collects styles from all the bands in the report.
   */
  protected void collectStyles ()
  {
    collectStylesFromBand(getReportHeader());
    collectStylesFromBand(getReportFooter());
    collectStylesFromBand(getPageHeader());
    collectStylesFromBand(getPageFooter());
    collectStylesFromBand(getItemBand());
    for (int i = 0; i < getGroupCount(); i++)
    {
      Group g = getGroup(i);
      collectStylesFromBand(g.getHeader());
      collectStylesFromBand(g.getFooter());
    }

  }

  /**
   * Collects the styles from a band.
   *
   * @param band  the band.
   */
  private void updateStylesFromBand (Band band)
  {
    styleSheetCollection.updateStyleSheet(band.getStyle());
    styleSheetCollection.updateStyleSheet(band.getBandDefaults());

    Element[] elements = band.getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      if (elements[i] instanceof Band)
      {
        updateStylesFromBand((Band) elements[i]);
      }
      else
      {
        styleSheetCollection.updateStyleSheet(elements[i].getStyle());
      }
    }

  }

  /**
   * Collects the styles from a band.
   *
   * @param band  the band.
   */
  private void collectStylesFromBand (Band band)
  {
    ElementStyleSheet bandDefaults = band.getBandDefaults();

    List parents = bandDefaults.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      ElementStyleSheet es = (ElementStyleSheet) parents.get(i);
      addCollectableStyleSheet(es);
    }

    collectStylesFromElement(band);

    Element[] elements = band.getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      if (elements[i] instanceof Band)
      {
        collectStylesFromBand((Band) elements[i]);
      }
      else
      {
        collectStylesFromElement(elements[i]);
      }
    }

  }

  /**
   * Collects the styles from an element.
   *
   * @param element  the element.
   */
  private void collectStylesFromElement (Element element)
  {
    ElementStyleSheet elementSheet = element.getStyle();

    List parents = elementSheet.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      ElementStyleSheet es = (ElementStyleSheet) parents.get(i);
      addCollectableStyleSheet(es);
    }
  }

  /**
   * Adds a defined stylesheet to the styles collection. If the stylesheet
   * is one of the default stylesheets, then it is not collected.
   *
   * @param es  the element style sheet.
   */
  private void addCollectableStyleSheet (ElementStyleSheet es)
  {
    List parents = es.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      ElementStyleSheet parentsheet = (ElementStyleSheet) parents.get(i);
      addCollectableStyleSheet(parentsheet);
    }

    styleSheetCollection.updateStyleSheet(es);
  }
}
