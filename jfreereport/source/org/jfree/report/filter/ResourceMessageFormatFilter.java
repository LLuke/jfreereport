/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * ResourceMessageFormatFilter.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ResourceMessageFormatFilter.java,v 1.2 2006/01/27 16:25:36 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.01.2006 : Initial version
 */
package org.jfree.report.filter;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import org.jfree.report.ReportDefinition;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 24.01.2006, 15:58:32
 *
 * @author Thomas Morgner
 */
public class ResourceMessageFormatFilter
        implements ReportConnectable, Serializable, DataSource
{
  /** The report definition registered to this connectable. */
  private transient ReportDefinition reportDefinition;
  private transient String formatString;
  private transient Locale locale;

  private String formatKey;

  /** the used resource bundle. */
  private String resourceIdentifier;

  /**
   * The message format support translates raw message strings into useable
   * MessageFormat parameters and read the necessary input data from the
   * datarow.
   */
  private MessageFormatSupport messageFormatSupport;

  public ResourceMessageFormatFilter()
  {
    messageFormatSupport = new MessageFormatSupport();
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    if (reportDefinition == null)
    {
      return null;
    }
    final String resourceId;
    if (resourceIdentifier != null)
    {
      resourceId = resourceIdentifier;
    }
    else
    {
      resourceId = reportDefinition.getReportConfiguration().getConfigProperty
              (ResourceBundleFactory.DEFAULT_RESOURCE_BUNDLE_CONFIG_KEY);
    }

    if (resourceId == null)
    {
      return null;
    }

    final ResourceBundleFactory resourceBundleFactory =
            reportDefinition.getResourceBundleFactory();
    final ResourceBundle bundle =
            resourceBundleFactory.getResourceBundle(resourceId);

    // update the format string, if neccessary ...
    final String newFormatString = bundle.getString(formatKey);
    if (ObjectUtilities.equal(newFormatString, formatString) == false)
    {
      messageFormatSupport.setFormatString(newFormatString);
      formatString = newFormatString;
    }
    // update the locale, if neccessary ...
    final Locale newLocale = resourceBundleFactory.getLocale();
    if (ObjectUtilities.equal(newLocale, locale) == false)
    {
      messageFormatSupport.setLocale(resourceBundleFactory.getLocale());
      locale = newLocale;
    }

    return messageFormatSupport.performFormat(reportDefinition.getDataRow());
  }


  /**
   * Returns the name of the used resource bundle.
   *
   * @return the name of the resourcebundle
   * @see org.jfree.report.ResourceBundleFactory#getResourceBundle(String)
   */
  public String getResourceIdentifier()
  {
    return resourceIdentifier;
  }

  /**
   * Defines the name of the used resource bundle. If undefined, all calls to
   * {@link ResourceFileFilter#getValue()} will result in <code>null</code>
   * values.
   *
   * @param resourceIdentifier the resource bundle name
   */
  public void setResourceIdentifier(final String resourceIdentifier)
  {
    this.resourceIdentifier = resourceIdentifier;
  }

  /**
   * Defines the format string for the {@link java.text.MessageFormat} object
   * used in this implementation.
   *
   * @param format resourcebundle key for the message format.
   */
  public void setFormatKey(final String format)
  {
    this.formatKey = format;
  }

  /**
   * Returns the format string used in the message format.
   *
   * @return the format string.
   */
  public String getFormatKey()
  {
    return formatKey;
  }


  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final ResourceMessageFormatFilter mf = (ResourceMessageFormatFilter) super
            .clone();
    mf.reportDefinition = null;
    mf.messageFormatSupport = (MessageFormatSupport) messageFormatSupport
            .clone();
    return mf;
  }

  /**
   * Connects the connectable to the given report definition.
   *
   * @param reportDefinition the reportDefinition for this report connectable.
   * @throws IllegalStateException if this instance is already connected to a
   *                               report definition.
   */
  public void registerReportDefinition(final ReportDefinition reportDefinition)
  {
    if (this.reportDefinition != null)
    {
      throw new IllegalStateException("Already connected.");
    }
    if (reportDefinition == null)
    {
      throw new NullPointerException("The given report definition is null");
    }
    this.reportDefinition = reportDefinition;
  }

  /**
   * Disconnects the connectable from the given report definition.
   *
   * @param reportDefinition the reportDefinition for this report connectable.
   * @throws IllegalStateException if this instance is already connected to a
   *                               report definition.
   */
  public void unregisterReportDefinition(final ReportDefinition reportDefinition)
  {
    if (this.reportDefinition != reportDefinition)
    {
      throw new IllegalStateException(
              "This report definition is not registered.");
    }
    this.reportDefinition = null;
  }

  public String getNullString()
  {
    return messageFormatSupport.getNullString();
  }

  public void setNullString(final String nullString)
  {
    this.messageFormatSupport.setNullString(nullString);
  }
}
