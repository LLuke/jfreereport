/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -------------------------------
 * ItemColumnQuotientFunction.java
 * -------------------------------
 * (C)opyright 2002, 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemColumnQuotientExpression.java,v 1.5 2003/08/28 19:36:30 taqua Exp $
 *
 * Changes
 * -------
 * 23-Dec-2002 : Initial version
 *
 */

package org.jfree.report.function;

import java.io.Serializable;

import org.jfree.report.util.Log;

/**
 * A report function that calculates the quotient of two fields (columns)
 * from the current row.
 * <p>
 * This function expects its input values to be either java.lang.Number instances or Strings
 * that can be parsed to java.lang.Number instances using a java.text.DecimalFormat.
 * <p>
 * The function undestands two parameters.
 * The <code>dividend</code> parameter is required and denotes the name of an ItemBand-field
 * which is used as dividend. The <code>divisor</code> parameter is required and denotes
 * the name of an ItemBand-field which is uses as divisor.
 * <p>
 *
 * @author Heiko Evermann
 */
public class ItemColumnQuotientExpression extends AbstractExpression implements Serializable
{
  /** Literal text for the 'dividend' property. */
  public static final String DIVIDEND_PROPERTY = "dividend";

  /** Literal text for the 'divisor' property. */
  public static final String DIVISOR_PROPERTY = "divisor";

  /**
   * Constructs a new function.
   * <P>
   * Initially the function has no name...be sure to assign one before using the function.
   */
  public ItemColumnQuotientExpression()
  {
  }

  /**
   * Returns a clone of the function.
   * <P>
   * Be aware, this does not create a deep copy. If you have complex
   * strucures contained in objects, you have to overwrite this function.
   *
   * @return A clone of the function.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Return the current function value.
   * <P>
   * The value is calculated as the quotient of two columns: the dividend column and the divisor
   * column.  If the divisor is zero, the return value is "n/a";
   *
   * @return The quotient
   */
  public Object getValue()
  {
    // calculate quotient
    double dividend = 0.0;
    double divisor = 0.0;

    Object fieldValue = getDataRow().get(getDividend());
    // do not add when field is null
    if (fieldValue != null)
    {
      try
      {
        final Number n = (Number) fieldValue;
        dividend = n.doubleValue();
      }
      catch (Exception e)
      {
        Log.error("ItemColumnQuotientExpression(): problem with dividend value");
      }
    }
    // sum up divisor column
    fieldValue = getDataRow().get(getDivisor());
    // do not add when field is null
    if (fieldValue != null)
    {
      try
      {
        final Number n = (Number) fieldValue;
        divisor = n.doubleValue();
      }
      catch (Exception e)
      {
        Log.error("ItemColumnQuotientExpression(): problem with divisor value");
      }
    }

    if (divisor == 0.0)
    {
      return "n/a";
    }
    return new Double(dividend / divisor);
  }

  /**
   * Returns the field used as dividend by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getDividend()
  {
    return getProperty(DIVIDEND_PROPERTY);
  }

  /**
   * Returns the field used as divisor by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getDivisor()
  {
    return getProperty(DIVISOR_PROPERTY);
  }

  /**
   * Sets the field name to be used as dividend for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param dividend the field name (null not permitted).
   */
  public void setDividend(final String dividend)
  {
    if (dividend == null)
    {
      throw new NullPointerException();
    }
    setProperty(DIVIDEND_PROPERTY, dividend);
  }

  /**
   * Sets the field name to be used as divisor for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param divisor the field name (null not permitted).
   */
  public void setDivisor(final String divisor)
  {
    if (divisor == null)
    {
      throw new NullPointerException();
    }
    setProperty(DIVISOR_PROPERTY, divisor);
  }

  /**
   * Checks that the function has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   *
   * @throws FunctionInitializeException if the function name is not set or the call to
   * isInitialized returns false.
   */
  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
    if (getProperty(DIVIDEND_PROPERTY) == null)
    {
      throw new FunctionInitializeException("Dividend is required");
    }
    if (getProperty(DIVISOR_PROPERTY) == null)
    {
      throw new FunctionInitializeException("Divisor is required");
    }
  }

  /**
   * Return a completely separated copy of this function. The copy does no
   * longer share any changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final ItemColumnQuotientExpression function =
        (ItemColumnQuotientExpression) super.getInstance();
    return function;
  }
}