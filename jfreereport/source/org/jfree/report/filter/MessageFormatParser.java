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
 * MessageFormatParser.java
 * -----------------------
 * (C)opyright 2000-2004, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   J&ouml;rg Schaible (for Elsag-Solutions AG);
 *
 * $Id$
 *
 * ChangeLog
 * ---------
 * 14-Dec-2004 : Initial version (copied from NumberFormatParser)
 * 
 */
package org.jfree.report.filter;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParseException;

/**
 * A filter that parses the value from a data source string into an apropriate representation.
 * <p>
 * This filter will parse the string obtained from the datasource into an apropriate object using 
 * a java.text.MessageFormat and depending on the type defined in the format.
 *
 * @see java.text.MessageFormat
 *
 * @author J&ouml;rg Schaible
 */
public class MessageFormatParser extends FormatParser
{
  /**
   * Default constructor.
   * <P>
   * Uses a general number format for the current locale.
   */
  public MessageFormatParser()
  {
    setMessageFormat(new MessageFormat("{0}"));
  }

  /**
   * Sets the number format.
   *
   * @param nf The number format.
   */
  public void setMessageFormat(final MessageFormat nf)
  {
    super.setFormatter(nf);
  }

  /**
   * Returns the number format.
   *
   * @return The number format.
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

  /**
   * Checks whether the given value is already a valid result. IF the datasource already returned
   * a valid value, and no parsing is required, a parser can skip the parsing process by returning
   * true in this function.
   *
   * @param o  the value to parse.
   *
   * @return true, if the given object is not null.
   */
  protected boolean isValidOutput(final Object o)
  {
    return o != null;
  }

  /**
   * Returns the parsed object. The value is read using the data source given
   * and parsed using the formatter of this object. The parsing is guaranteed to
   * completly form the target object or to return the defined NullValue.
   * <p>
   * If the given datasource does not return a string, the returned object is
   * transformed into a string using String.valueOf (Object) and then parsed.
   * <p>
   * If format, datasource or object are null, the NullValue is returned.
   *
   * @return The object resulting from the parsed value.
   */
  public Object getValue()
  {
    final MessageFormat f = getMessageFormat();
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

    if (isValidOutput(o))
    {
      return o;
    }

    try
    {
      return f.parse(String.valueOf(o))[0];
    }
    catch (ParseException e)
    {
      return null;
    }
  }
}
