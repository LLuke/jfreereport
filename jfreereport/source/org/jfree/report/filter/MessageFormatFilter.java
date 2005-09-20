/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * -----------------------
 * MessageFormatFilter.java
 * -----------------------
 * (C)opyright 2002-2004, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   J&ouml;rg Schaible (for Elsag-Solutions AG);
 *
 * $Id: MessageFormatFilter.java,v 1.5 2005/04/14 16:37:33 taqua Exp $
 *
 * Changes
 * -------
 * 14-Dec-2004 : Initial version (copied from NumberFormatFilter)
 *
 */

package org.jfree.report.filter;

import java.io.Serializable;

import org.jfree.report.ReportDefinition;

/**
 * A filter that formats values from a data source to a string representation.
 * <p/>
 * This filter will format objects using a {@link java.text.MessageFormat} to create the
 * string representation for the number obtained from the datasource.
 *
 * @author Joerg Schaible
 * @author Thomas Morgner
 * @see java.text.MessageFormat
 */
public class MessageFormatFilter
        implements ReportConnectable, Serializable, DataSource
{
  /** The report definition registered to this connectable. */
  private transient ReportDefinition reportDefinition;

  private MessageFormatSupport messageFormatSupport;

  /**
   * Default constructor. <P> Uses a general number format for the current locale.
   */
  public MessageFormatFilter ()
  {
    messageFormatSupport = new MessageFormatSupport();
  }

  /**
   * Defines the format string for the {@link MessageFormat} object used in this
   * implementation.
   *
   * @param format the message format.
   */
  public void setFormatString (final String format)
  {
    messageFormatSupport.setFormatString(format);
  }

  /**
   * Returns the format string used in the message format.
   *
   * @return the format string.
   */
  public String getFormatString ()
  {
    return messageFormatSupport.getFormatString();
  }

  /**
   * Returns the formatted string. The value is read using the data source given and
   * formated using the formatter of this object. The formating is guaranteed to completly
   * form the object to an string or to return the defined NullValue.
   * <p/>
   * If format, datasource or object are null, the NullValue is returned.
   *
   * @return The formatted value.
   */
  public Object getValue ()
  {
    if (reportDefinition == null)
    {
      return null;
    }
    messageFormatSupport.setLocale(reportDefinition.getResourceBundleFactory().getLocale());
    return messageFormatSupport.performFormat(reportDefinition.getDataRow());
  }

  /**
   * Connects the connectable to the given report definition.
   *
   * @param reportDefinition the reportDefinition for this report connectable.
   * @throws IllegalStateException if this instance is already connected to a
   * report definition.
   */
  public void registerReportDefinition (final ReportDefinition reportDefinition)
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
   * report definition.
   */
  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    if (this.reportDefinition != reportDefinition)
    {
      throw new IllegalStateException("This report definition is not registered.");
    }
    this.reportDefinition = null;
  }

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final MessageFormatFilter mf = (MessageFormatFilter) super.clone();
    mf.reportDefinition = null;
    return mf;
  }
}
