/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * DateElement.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: DateElement.java,v 1.2 2002/05/14 21:35:02 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 10-May-2002 : Removed all compelex constructors
 */

package com.jrefinery.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A data element that handles java.util.Date objects.
 */
public class DateElement extends DataElement
{

  /** The formatting object for this data element. */
  private DateFormat formatter;

  /**
   * Constructs a date element using float coordinates.
   */
  public DateElement()
  {
    setFormatString(null);
  }

  /**
   * sets the format of the element to SimpleDate using the given formatString.
   */
  public void setFormatString(String s)
  {
    if (s == null)
    {
      setFormatter(new SimpleDateFormat());
    }
    else
    {
      setFormatter(new SimpleDateFormat(s));
    }
  }

  /**
   * @return the current formater for this element. This function will never
   * return null.
   */
  public DateFormat getFormatter()
  {
    return formatter;
  }

  /**
   * Defines the current formater for the element. If the formater is null,
   * an exception is thrown.
   */
  public void setFormatter(DateFormat format)
  {
    if (format == null)
    {
      throw new NullPointerException("Given format may not be null");
    }
    this.formatter = format;
  }

  /**
   * Returns a string representing the formatted date.
   * @return A formatted version of the data value.
   */
  public String getFormattedText()
  {

    if (getValue() instanceof Date)
    {
      return formatter.format(getValue());
    }
    else
      return "-";

  }

}