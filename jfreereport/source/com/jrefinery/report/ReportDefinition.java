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
 * $Id: ReportDefinition.java,v 1.2 2003/04/09 15:45:47 mungady Exp $
 *
 * Changes
 * -------
 * 05.04.2003 : Initial version
 * 
 */

package com.jrefinery.report;

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
   * Creates and returns a copy of this object.  The precise meaning
   * of "copy" may depend on the class of the object. The general
   * intent is that, for any object <tt>x</tt>, the expression:
   * <blockquote>
   * <pre>
   * x.clone() != x</pre></blockquote>
   * will be true, and that the expression:
   * <blockquote>
   * <pre>
   * x.clone().getClass() == x.getClass()</pre></blockquote>
   * will be <tt>true</tt>, but these are not absolute requirements.
   * While it is typically the case that:
   * <blockquote>
   * <pre>
   * x.clone().equals(x)</pre></blockquote>
   * will be <tt>true</tt>, this is not an absolute requirement.
   * <p>
   * By convention, the returned object should be obtained by calling
   * <tt>super.clone</tt>.  If a class and all of its superclasses (except
   * <tt>Object</tt>) obey this convention, it will be the case that
   * <tt>x.clone().getClass() == x.getClass()</tt>.
   * <p>
   * By convention, the object returned by this method should be independent
   * of this object (which is being cloned).  To achieve this independence,
   * it may be necessary to modify one or more fields of the object returned
   * by <tt>super.clone</tt> before returning it.  Typically, this means
   * copying any mutable objects that comprise the internal "deep structure"
   * of the object being cloned and replacing the references to these
   * objects with references to the copies.  If a class contains only
   * primitive fields or references to immutable objects, then it is usually
   * the case that no fields in the object returned by <tt>super.clone</tt>
   * need to be modified.
   * <p>
   * The method <tt>clone</tt> for class <tt>Object</tt> performs a
   * specific cloning operation. First, if the class of this object does
   * not implement the interface <tt>Cloneable</tt>, then a
   * <tt>CloneNotSupportedException</tt> is thrown. Note that all arrays
   * are considered to implement the interface <tt>Cloneable</tt>.
   * Otherwise, this method creates a new instance of the class of this
   * object and initializes all its fields with exactly the contents of
   * the corresponding fields of this object, as if by assignment; the
   * contents of the fields are not themselves cloned. Thus, this method
   * performs a "shallow copy" of this object, not a "deep copy" operation.
   * <p>
   * The class <tt>Object</tt> does not itself implement the interface
   * <tt>Cloneable</tt>, so calling the <tt>clone</tt> method on an object
   * whose class is <tt>Object</tt> will result in throwing an
   * exception at run time.
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
    return report;
  }
}
