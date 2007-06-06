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
 * $Id: AbstractNumericOperator.java,v 1.2 2007/05/31 14:58:35 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.formula.operators;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.TypeRegistryUtility;
import org.jfree.formula.typing.coretypes.NumberType;

/**
 * Creation-Date: 10.04.2007, 15:02:39
 *
 * @author Thomas Morgner
 */
public abstract class AbstractNumericOperator implements InfixOperator
{
  protected static final Number ZERO = new Integer(0);

  protected AbstractNumericOperator()
  {
  }

  public final TypeValuePair evaluate(final FormulaContext context,
                                      final TypeValuePair value1,
                                      final TypeValuePair value2)
      throws EvaluationException
  {
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    if (value1 == null || value2 == null)
    {
      // If this happens, then one of the implementations has messed up.
      throw new EvaluationException(LibFormulaErrorValue.ERROR_UNEXPECTED_VALUE);
    }

    final Object raw1 = value1.getValue();
    final Object raw2 = value2.getValue();
    if (raw1 == null || raw2 == null)
    {
      throw new EvaluationException (LibFormulaErrorValue.ERROR_NA_VALUE);
    }

    final Number number1 = TypeRegistryUtility.convertToNumber(typeRegistry, value1.getType(), raw1, ZERO);
    final Number number2 = TypeRegistryUtility.convertToNumber(typeRegistry, value2.getType(), raw2, ZERO);
    return new TypeValuePair(NumberType.GENERIC_NUMBER, evaluate(number1, number2));
  }

  protected abstract Number evaluate (final Number number1, final Number number2) throws EvaluationException;
}
