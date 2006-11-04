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
 * OperatorSequence.java
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
package org.jfree.formula.lvalues;

import java.util.ArrayList;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.operators.InfixOperator;

/**
 * An term is a list of LValues connected by operators. For the sake of
 * efficiency, this is not stored as tree. We store the term as a list in the
 * following format: (headValue)(OP value)* ...
 *
 * @author Thomas Morgner
 */
public class Term extends AbstractLValue
{
  private LValue headValue;
  private ArrayList operators;
  private ArrayList operands;
  private InfixOperator[] operatorArray;
  private LValue[] operandsArray;

  public Term(final LValue headValue)
  {
    if (headValue == null)
    {
      throw new NullPointerException();
    }

    this.headValue = headValue;
  }

  public TypeValuePair evaluate() throws EvaluationException
  {
    TypeValuePair result = headValue.evaluate();
    for (int i = 0; i < operandsArray.length; i++)
    {
      final LValue value = operandsArray[i];
      final InfixOperator op = operatorArray[i];
      result = op.evaluate(getContext(), result, value.evaluate());
    }
    return result;
  }

  public void add(InfixOperator operator, LValue operand)
  {
    if (operator == null)
    {
      throw new NullPointerException();
    }
    if (operand == null)
    {
      throw new NullPointerException();
    }

    if (operands == null || operators == null)
    {
      this.operands = new ArrayList();
      this.operators = new ArrayList();
    }

    operands.add(operand);
    operators.add(operator);
  }

  public void initialize(FormulaContext context) throws EvaluationException
  {
    super.initialize(context);
    if (operands == null || operators == null)
    {
      this.operandsArray = new LValue[0];
      this.operatorArray = new InfixOperator[0];
      return;
    }
    this.operatorArray = (InfixOperator[])
        operators.toArray(new InfixOperator[operators.size()]);
    this.operandsArray = (LValue[])
        operands.toArray(new LValue[operands.size()]);

    for (int i = 0; i < operandsArray.length; i++)
    {
      final LValue value = operandsArray[i];
      value.initialize(context);
    }
  }

  /**
   * Returns any dependent lvalues (parameters and operands, mostly).
   *
   * @return
   */
  public LValue[] getChildValues()
  {
    return (LValue[]) operandsArray.clone();
  }


  public String toString()
  {
    StringBuffer b = new StringBuffer();

    b.append("Term={");
    b.append(headValue);

    for (int i = 0; i < operands.size(); i++)
    {
      InfixOperator op = (InfixOperator) operators.get(i);
      LValue value = (LValue) operands.get(i);
      b.append(op);
      b.append(value);
    }
    b.append("}");
    return b.toString();
  }
}
