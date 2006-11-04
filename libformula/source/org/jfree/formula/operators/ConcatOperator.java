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
 * ConcatOperator.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ConcatOperator.java,v 1.1 2006/11/04 15:44:33 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.operators;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.TextType;

/**
 * Creation-Date: 03.11.2006, 18:28:48
 *
 * @author Thomas Morgner
 */
public class ConcatOperator implements InfixOperator
{
  public ConcatOperator()
  {
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                final TypeValuePair value1,
                                final TypeValuePair value2)
      throws EvaluationException
  {
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final String text1 =
        typeRegistry.convertToText(value1.getType(), value1.getValue());
    final String text2 =
        typeRegistry.convertToText(value2.getType(), value2.getValue());
    if (text1 == null || text2 == null)
    {
      throw new EvaluationException
          (new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }

    return new TypeValuePair(TextType.TYPE, text1 + text2);
  }

  public int getLevel()
  {
    return 300;
  }


  public String toString()
  {
    return "&";
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
    return false;
  }

}