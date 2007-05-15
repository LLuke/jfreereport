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
 * $Id: Term.java,v 1.7 2007/04/01 13:51:54 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
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
  private LValue optimizedHeadValue;
  private LValue headValue;
  private ArrayList operators;
  private ArrayList operands;
  private InfixOperator[] operatorArray;
  private LValue[] operandsArray;
  private boolean initialized;

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
    TypeValuePair result = optimizedHeadValue.evaluate();
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
    initialized = false;
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

    if (initialized)
    {
      optimizedHeadValue.initialize(context);
      for (int i = 0; i < operandsArray.length; i++)
      {
        LValue lValue = operandsArray[i];
        lValue.initialize(context);
      }
      return;
    }

    optimize(context);
  }

  private void optimize(FormulaContext context) throws EvaluationException
  {
    ArrayList operators = (ArrayList) this.operators.clone();
    ArrayList operands = (ArrayList) this.operands.clone();
    this.optimizedHeadValue = headValue;

    while (true)
    {
      // now start to optimize everything.
      // first, search the operator with the highest priority..
      final InfixOperator op = (InfixOperator) operators.get(0);
      int level = op.getLevel();
      boolean moreThanOne = false;
      for (int i = 1; i < operators.size(); i++)
      {
        final InfixOperator operator = (InfixOperator) operators.get(i);
        final int opLevel = operator.getLevel();
        if (opLevel != level)
        {
          moreThanOne = true;
          level = Math.min(opLevel, level);
        }
      }

      if (moreThanOne == false)
      {
        // No need to optimize the operators ..
        break;
      }

      // There are at least two op-levels in this term.
      Term subTerm = null;
      for (int i = 0; i < operators.size(); i++)
      {
        final InfixOperator operator = (InfixOperator) operators.get(i);
        if (operator.getLevel() != level)
        {
          subTerm = null;
          continue;
        }

        if (subTerm == null)
        {
          if (i == 0)
          {
            subTerm = new Term(optimizedHeadValue);
            optimizedHeadValue = subTerm;
          }
          else
          {
            final LValue lval = (LValue) operands.get(i - 1);
            subTerm = new Term(lval);
            operands.set(i - 1, subTerm);
          }
        }

        // OK, now a term exists, and we should join it.
        final LValue operand = (LValue) operands.get(i);
        subTerm.add(operator, operand);
        operands.remove(i);
        operators.remove(i);
        // Rollback the current index ..
        //noinspection AssignmentToForLoopParameter
        i -= 1;
      }
    }

    this.operatorArray = (InfixOperator[])
        operators.toArray(new InfixOperator[operators.size()]);
    this.operandsArray = (LValue[])
        operands.toArray(new LValue[operands.size()]);
    this.optimizedHeadValue.initialize(context);
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
    final LValue[] values = new LValue[operandsArray.length + 1];
    values[0] = headValue;
    System.arraycopy(operandsArray, 0, values, 1, operandsArray.length);
    return values;
  }


  public String toString()
  {
    StringBuffer b = new StringBuffer();

    b.append("(");
//    b.append(headValue);
//    if (operands != null && operators != null)
//    {
//      for (int i = 0; i < operands.size(); i++)
//      {
//        InfixOperator op = (InfixOperator) operators.get(i);
//        LValue value = (LValue) operands.get(i);
//        b.append(op);
//        b.append(value);
//      }
//    }
//    b.append(")");
//
//    b.append(";OPTIMIZED(");
    b.append(optimizedHeadValue);
    if (operandsArray != null && operatorArray != null)
    {
      for (int i = 0; i < operandsArray.length; i++)
      {
        InfixOperator op = operatorArray[i];
        LValue value = operandsArray[i];
        b.append(op);
        b.append(value);
      }
    }
    b.append(")");

    return b.toString();
  }

  /**
   * Checks, whether the LValue is constant. Constant lvalues always return the
   * same value.
   *
   * @return
   */
  public boolean isConstant()
  {
    if (headValue.isConstant() == false)
    {
      return false;
    }

    for (int i = 0; i < operands.size(); i++)
    {
      LValue value = (LValue) operands.get(i);
      if (value.isConstant() == false)
      {
        return false;
      }
    }
    return true;
  }

  public Object clone() throws CloneNotSupportedException
  {
    final Term o = (Term) super.clone();
    if (operands != null)
    {
      o.operands = (ArrayList) operands.clone();
    }
    if (operators != null)
    {
      o.operators = (ArrayList) operators.clone();
    }
    o.headValue = (LValue) headValue.clone();
    o.optimizedHeadValue = null;
    o.operandsArray = null;
    o.operatorArray = null;
    o.initialized = false;
    return o;
  }

  public InfixOperator[] getOperands ()
  {
    return (InfixOperator[]) operands.toArray(new InfixOperator[operands.size()]);
  }

  public LValue[] getOperators ()
  {
    return (LValue[]) operators.toArray(new LValue[operators.size()]);
  }

  public LValue getHeadValue()
  {
    return headValue;
  }
  
  /**
   * Allows access to the post optimized head value
   * note that without the optimization, it's difficult to traverse
   * libformula's object model.
   *
   * @return optimized head value
   */
  public LValue getOptimizedHeadValue()
  {
    return optimizedHeadValue;
  }
  
  /**
   * Allows access to the post optimized operator array
   * 
   * @return optimized operator array
   */
  public InfixOperator[] getOptimizedOperators()
  {
    return operatorArray;
  }
  
  /**
   * Allows access to the post optimized operand array
   *
   * @return optimized operand array
   */
  public LValue[] getOptimizedOperands()
  {
    return operandsArray;
  }
}
