/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libformula/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id: AddOperator.java,v 1.9 2007/04/01 13:51:54 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.operators;

import java.math.BigDecimal;

/**
 * Null-Values are converted into ZERO
 *
 * @author Thomas Morgner
 */
public class AddOperator extends AbstractNumericOperator
{
  public AddOperator()
  {
  }

  public Number evaluate(final Number number1, final Number number2)
  {
    if ((number1 instanceof Integer || number1 instanceof Short) &&
        (number2 instanceof Integer || number2 instanceof Short))
    {
      return new BigDecimal (number1.longValue() + number2.longValue());
    }

    final BigDecimal bd1 = new BigDecimal(number1.toString());
    final BigDecimal bd2 = new BigDecimal(number2.toString());
    return bd1.add(bd2);
  }

  public int getLevel()
  {
    return 200;
  }


  public String toString()
  {
    return "+";
  }

  public boolean isLeftOperation()
  {
    return true;
  }

  /**
   * Defines, whether the operation is associative. For associative operations,
   * the evaluation order does not matter, if the operation appears more than
   * once in an expression, and therefore we can optimize them a lot better than
   * non-associative operations (ie. merge constant parts and precompute them
   * once).
   *
   * @return true, if the operation is associative, false otherwise
   */
  public boolean isAssociative()
  {
    return true;
  }

}
