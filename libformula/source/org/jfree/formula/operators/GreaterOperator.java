/**
 * =========================================
 * LibLayout : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * GreaterEqualOperator.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.operators;

import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.ExtendedComparator;
import org.jfree.formula.typing.coretypes.LogicalType;

/**
 * Creation-Date: 31.10.2006, 16:34:11
 *
 * @author Thomas Morgner
 */
public class GreaterOperator implements InfixOperator
{
  public GreaterOperator()
  {
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                TypeValuePair value1, TypeValuePair value2)
      throws EvaluationException
  {
    final TypeRegistry typeRegistry = context.getTypeRegistry();
    final ExtendedComparator comparator =
        typeRegistry.getComparator(value1.getType(), value2.getType());
    final Integer result = comparator.compare
        (value1.getType(), value1.getValue(),
            value2.getType(), value2.getValue());
    if (result == null)
    {
      throw new EvaluationException
          (new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }

    if (result.intValue() > 0)
    {
      return new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
    }
    else
    {
      return new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);
    }
  }

  public int getLevel()
  {
    return 400;
  }

  public String toString()
  {
    return ">";
  }

}
