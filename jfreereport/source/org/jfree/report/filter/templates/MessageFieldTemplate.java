/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * MessageFieldTemplate.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.filter.templates;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.MessageFormatFilter;
import org.jfree.report.filter.ReportConnectable;
import org.jfree.report.filter.StringFilter;

public class MessageFieldTemplate extends AbstractTemplate
        implements ReportConnectable
{
  /**
   * A string filter.
   */
  private StringFilter stringFilter;

  private MessageFormatFilter messageFormatFilter;

  /**
   * Creates a new string field template.
   */
  public MessageFieldTemplate ()
  {
    messageFormatFilter = new MessageFormatFilter();
    stringFilter = new StringFilter();
    stringFilter.setDataSource(messageFormatFilter);
  }

  public String getFormat ()
  {
    return messageFormatFilter.getFormatString();
  }

  public void setFormat (final String format)
  {
    this.messageFormatFilter.setFormatString(format);
  }

  /**
   * Returns the value displayed by the field when the data source value is
   * <code>null</code>.
   *
   * @return A value to represent <code>null</code>.
   */
  public String getNullValue ()
  {
    return stringFilter.getNullValue();
  }

  /**
   * Sets the value displayed by the field when the data source value is
   * <code>null</code>.
   *
   * @param nullValue the value that represents <code>null</code>.
   */
  public void setNullValue (final String nullValue)
  {
    stringFilter.setNullValue(nullValue);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue ()
  {
    return stringFilter.getValue();
  }

  /**
   * Clones the template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final MessageFieldTemplate template = (MessageFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.messageFormatFilter = (MessageFormatFilter) template.stringFilter.getDataSource();
    return template;
  }

  public void registerReportDefinition (final ReportDefinition reportDefinition)
  {
    messageFormatFilter.registerReportDefinition(reportDefinition);
  }

  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    messageFormatFilter.unregisterReportDefinition(reportDefinition);
  }
}
