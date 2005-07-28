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
 * PercentageExpression.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PercentageExpression.java,v 1.1 2005/07/22 16:42:26 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.function;

import java.io.Serializable;

/**
 * Computes the percentage for a column in relation to a base column.
 * <p/>
 * The function undestands two parameters. The <code>dividend</code> parameter is required
 * and denotes the name of an ItemBand-field which is used as dividend. The
 * <code>divisor</code> parameter is required and denotes the name of an ItemBand-field
 * which is uses as divisor.
 * <p/>
 * If either the divident or the divisor are not numeric, the expression will return
 * <code>null</code>. 
 * <p/>
 * The formula used is as follows:
 * <pre>
 * Percent := divident / divisor
 * </pre>
 * <p/>
 * If the flag <code>useDifference</code> is set, the difference between base and subject
 * is used instead.
 * <pre>
 * Percent := (divisor - divident) / divisor
 * </pre>
 *
 * @author Heiko Evermann
 * @author Thomas Morgner
 */
public class PercentageExpression extends AbstractExpression
        implements Serializable
{
  private String dividend;
  private String divisor;
  private boolean useDifference;

  /**
   * Constructs a new function. <P> Initially the function has no name...be sure to assign
   * one before using the function.
   */
  public PercentageExpression ()
  {
  }

  public boolean isUseDifference ()
  {
    return useDifference;
  }

  public void setUseDifference (final boolean useDifference)
  {
    this.useDifference = useDifference;
  }

  /**
   * Return the current function value. <P> The value is calculated as the quotient of two
   * columns: the dividend column and the divisor column.  If the divisor is zero, the
   * return value is "n/a";
   *
   * @return The quotient
   */
  public Object getValue ()
  {
    final Object dividentFieldValue = getDataRow().get(getDividend());
    // do not add when field is null or no number
    if (dividentFieldValue instanceof Number == false)
    {
      return null;
    }
    final Number dividentNumber = (Number) dividentFieldValue;
    final double dividend = dividentNumber.doubleValue();

    final Object divisorFieldValue = getDataRow().get(getDivisor());
    // do not add when field is null or no number
    if (divisorFieldValue instanceof Number == false)
    {
      return null;
    }
    final Number divisorNumber = (Number) divisorFieldValue;
    final double divisor = divisorNumber.doubleValue();

    final double value;
    if (useDifference)
    {
      final double delta = Math.abs(dividend - divisor);
      value = delta / divisor;
    }
    else
    {
      value = dividend / divisor;
    }

    return new Double(value);
  }

  /**
   * Returns the field used as dividend by the function. <P> The field name corresponds to
   * a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getDividend ()
  {
    return dividend;
  }

  /**
   * Returns the field used as divisor by the function. <P> The field name corresponds to
   * a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getDivisor ()
  {
    return divisor;
  }

  /**
   * Sets the field name to be used as dividend for the function. <P> The field name
   * corresponds to a column name in the report's TableModel.
   *
   * @param dividend the field name (null not permitted).
   */
  public void setDividend (final String dividend)
  {
    this.dividend = dividend;
  }

  /**
   * Sets the field name to be used as divisor for the function. <P> The field name
   * corresponds to a column name in the report's TableModel.
   *
   * @param divisor the field name (null not permitted).
   */
  public void setDivisor (final String divisor)
  {
    this.divisor = divisor;
  }
}