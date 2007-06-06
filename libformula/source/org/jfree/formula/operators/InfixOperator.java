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
 * $Id: InfixOperator.java,v 1.4 2007/04/01 13:51:54 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.operators;

import java.io.Serializable;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.lvalues.TypeValuePair;

/**
 * An operator. An operator always takes two arguments. Prefix and postfix
 * operators are implemented differently.
 *
 * @author Thomas Morgner
 */
public interface InfixOperator extends Serializable
{
  /**
   * Evaluates the comptuation for both parameters. This method must never return null.
   *
   * @param context
   * @param value1
   * @param value2
   * @return
   * @throws EvaluationException
   */
  public TypeValuePair evaluate(FormulaContext context,
                                TypeValuePair value1, TypeValuePair value2)
      throws EvaluationException;

  public int getLevel();

  /**
   * Defines the bind-direction of the operator. That direction defines, in
   * which direction a sequence of equal operators is resolved.
   *
   * @return true, if the operation is left-binding, false if right-binding
   */
  public boolean isLeftOperation();

  /**
   * Defines, whether the operation is associative. For associative operations,
   * the evaluation order does not matter, if the operation appears more than
   * once in an expression, and therefore we can optimize them
   * a lot better than non-associative operations (ie. merge constant parts
   * and precompute them once).
   *
   * @return true, if the operation is associative, false otherwise
   */
  public boolean isAssociative();
}
