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
 * $Id: MessageFormatFilter.java,v 1.1 2005/01/28 19:34:09 taqua Exp $
 *
 * Changes
 * -------
 * 14-Dec-2004 : Initial version (copied from NumberFormatFilter)
 *
 */

package org.jfree.report.filter;

import java.text.Format;
import java.text.MessageFormat;

/**
 * A filter that formats values from a data source to a string representation.
 * <p>
 * This filter will format objects using a {@link MessageFormat} to create the 
 * string representation for the number obtained from the datasource.
 *
 * @see java.text.MessageFormat
 *
 * @author Thomas Morgner
 */
public class MessageFormatFilter extends FormatFilter
{
  /**
   * Default constructor.
   * <P>
   * Uses a general number format for the current locale.
   */
  public MessageFormatFilter()
  {
      setMessageFormat(new MessageFormat("{0}"));
  }

  /**
   * Sets the message format.
   *
   * @param mf The message format.
   */
  public void setMessageFormat(final MessageFormat mf)
  {
    setFormatter(mf);
  }

  /**
   * Returns the message format.
   *
   * @return The message format.
   */
  public MessageFormat getMessageFormat()
  {
    return (MessageFormat) getFormatter();
  }

  /**
   * Sets the formatter.
   *
   * @param f The format.
   */
  public void setFormatter(final Format f)
  {
    final MessageFormat fm = (MessageFormat) f;
    super.setFormatter(fm);
  }

  public void setFormatString (final String format)
  {
    super.setFormatter(new MessageFormat(format));
  }

  public String getFormatString ()
  {
    return getMessageFormat().toPattern();
  }

  /**
   * Returns the formatted string. The value is read using the data source given
   * and formated using the formatter of this object. The formating is guaranteed to
   * completly form the object to an string or to return the defined NullValue.
   * <p>
   * If format, datasource or object are null, the NullValue is returned.
   *
   * @return The formatted value.
   */
  public Object getValue()
  {
    final Format f = getFormatter();
    if (f == null)
    {
      return getNullValue();
    }

    final DataSource ds = getDataSource();
    if (ds == null)
    {
      return getNullValue();
    }

    final Object o = ds.getValue();
    if (o == null)
    {
      return getNullValue();
    }

    try
    {
      return f.format(new Object[]{ o });
    }
    catch (IllegalArgumentException e)
    {
      return getNullValue();
    }
  }
}
