/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * -----------------------
 * NumberFormatFilter.java
 * -----------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: NumberFormatFilter.java,v 1.7 2002/10/15 20:37:22 taqua Exp $
 *
 * Changes
 * -------
 * 06-Jun-2002 : Updated Javadoc comments (DG);
 *
 */

package com.jrefinery.report.filter;

import com.jrefinery.report.util.Log;

import java.text.Format;
import java.text.NumberFormat;

/**
 * A filter that formats the numeric value from a data source to a string representation.
 * <p>
 * This filter will format java.lang.Number objects using a
 * java.text.NumericFormat to create the string representation for the number obtained
 * from the datasource.
 * <p>
 * If the object read from the datasource is no number, the NullValue defined by
 * setNullValue(Object) is returned.
 *
 * @see java.text.NumberFormat
 *
 * @author TM
 */
public class NumberFormatFilter extends FormatFilter
{
  /**
   * Default constructor.
   * <P>
   * Uses a general number format for the current locale.
   */
  public NumberFormatFilter ()
  {
    setNumberFormat (NumberFormat.getInstance ());
  }

  /**
   * Sets the number format.
   *
   * @param nf The number format.
   */
  public void setNumberFormat (NumberFormat nf)
  {
    setFormatter (nf);
  }

  /**
   * Returns the number format.
   *
   * @return The number format.
   */
  public NumberFormat getNumberFormat ()
  {
    return (NumberFormat) getFormatter ();
  }

  /**
   * Sets the formatter.
   *
   * @param f The format.
   */
  public void setFormatter (Format f)
  {
    super.setFormatter ((NumberFormat) f);
  }

  /**
   * Turns grouping on or off for the current number format.
   *
   * @param newValue The new value of the grouping flag.
   */
  public void setGroupingUsed (boolean newValue)
  {
    getNumberFormat ().setGroupingUsed (newValue);
  }

  /**
   * Returns the value of the grouping flag for the current number format.
   *
   * @return The grouping flag.
   */
  public boolean isGroupingUsed ()
  {
    return getNumberFormat ().isGroupingUsed ();
  }

  /**
   * Sets the maximum number of fraction digits for the current number format.
   *
   * @param newValue The number of digits.
   */
  public void setMaximumFractionDigits (int newValue)
  {
    getNumberFormat ().setMaximumFractionDigits (newValue);
  }

  /**
   * Returns the maximum number of fraction digits.
   *
   * @return The digits.
   */
  public int getMaximumFractionDigits ()
  {
    return getNumberFormat ().getMaximumFractionDigits ();
  }

  /**
   * Sets the maximum number of digits in the integer part of the current number format.
   *
   * @param newValue The number of digits.
   */
  public void setMaximumIntegerDigits (int newValue)
  {
    getNumberFormat ().setMaximumFractionDigits (newValue);
  }

  /**
   * Returns the maximum number of integer digits.
   *
   * @return The digits.
   */
  public int getMaximumIntegerDigits ()
  {
    return getNumberFormat ().getMaximumFractionDigits ();
  }

  /**
   * Sets the minimum number of fraction digits for the current number format.
   *
   * @param newValue The number of digits.
   */
  public void setMinimumFractionDigits (int newValue)
  {
    getNumberFormat ().setMaximumFractionDigits (newValue);
  }

  /**
   * Returns the minimum number of fraction digits.
   *
   * @return The digits.
   */
  public int getMinimumFractionDigits ()
  {
    return getNumberFormat ().getMaximumFractionDigits ();
  }

  /**
   * Sets the minimum number of digits in the integer part of the current number format.
   *
   * @param newValue The number of digits.
   */
  public void setMinimumIntegerDigits (int newValue)
  {
    getNumberFormat ().setMaximumFractionDigits (newValue);
  }

  /**
   * Returns the minimum number of integer digits.
   *
   * @return The digits.
   */
  public int getMinimumIntegerDigits ()
  {
    return getNumberFormat ().getMaximumFractionDigits ();
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
    return super.getValue();
  }
}
