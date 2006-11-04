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
 * Function.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FormulaFunction.java,v 1.1 2006/11/04 15:44:32 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.lvalues;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.FunctionDescription;
import org.jfree.formula.function.FunctionRegistry;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.util.Log;

/**
 * A function. Formulas consist of functions, references or static values, which
 * are connected by operators.
 * <p/>
 * Functions always have a cannonical name, which must be unique and which
 * identifies the function. Functions can have a list of parameters. The number
 * of parameters can vary, and not all parameters need to be filled.
 * <p/>
 * Functions can have required and optional parameters. Mixing required and
 * optional parameters is not allowed. Optional parameters cannot be ommited,
 * unless they are the last parameter in the list.
 * <p/>
 * This class provides the necessary wrapper functionality to fill in the
 * parameters.
 *
 * @author Thomas Morgner
 */
public class FormulaFunction extends AbstractLValue
{
  private String functionName;
  private LValue[] parameters;
  private Function function;
  private FunctionDescription metaData;

  public FormulaFunction(final String functionName, final LValue[] parameters)
  {
    this.functionName = functionName;
    this.parameters = parameters;
  }

  public void initialize(FormulaContext context) throws EvaluationException
  {
    super.initialize(context);
    final FunctionRegistry registry = context.getFunctionRegistry();
    function = registry.createFunction(functionName);
    metaData = registry.getMetaData(functionName);
  }

  public Object clone() throws CloneNotSupportedException
  {
    final FormulaFunction fn = (FormulaFunction) super.clone();
    fn.parameters = (LValue[]) parameters.clone();
    for (int i = 0; i < parameters.length; i++)
    {
      LValue parameter = parameters[i];
      fn.parameters[i] = (LValue) parameter.clone();
    }
    return fn;
  }

  public TypeValuePair evaluate() throws EvaluationException
  {
    // First, grab the parameters and their types.
    final FormulaContext context = getContext();
    final TypeRegistry typeRegistry = context.getTypeRegistry();
    final TypeValuePair[] params = new TypeValuePair[parameters.length];

    // prepare the parameters ..
    for (int i = 0; i < parameters.length; i++)
    {
      final LValue parameter = parameters[i];
      final Type paramType = metaData.getParameterType(i);
      if (parameter != null)
      {
        final TypeValuePair result = parameter.evaluate();
        // lets do some type checking, right?
        final TypeValuePair converted = typeRegistry.convertTo(paramType, result);
        if (converted == null)
        {
          final LibFormulaErrorValue errorValue = new LibFormulaErrorValue
              (LibFormulaErrorValue.ERROR_INVALID_ARGUMENT);
          Log.debug("Failed to evaluate paramter " + i + " on function " + function);
          throw new EvaluationException(errorValue);
        }
        params[i] = converted;

      }
      else
      {
        params[i] = new TypeValuePair(paramType, metaData.getDefaultValue(i));
      }
    }

    // And if everything is ok, compute the stuff ..
    return function.evaluate(context, params);
  }

  /**
   * Returns any dependent lvalues (parameters and operands, mostly).
   *
   * @return
   */
  public LValue[] getChildValues()
  {
    return (LValue[]) parameters.clone();
  }


  public String toString()
  {
    StringBuffer b = new StringBuffer();
    b.append(functionName);
    b.append("(");
    for (int i = 0; i < parameters.length; i++)
    {
      if (i > 0)
      {
        b.append(",");
      }
      LValue parameter = parameters[i];
      b.append(parameter);
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
    if (metaData.isVolatile())
    {
      return false;
    }
    for (int i = 0; i < parameters.length; i++)
    {
      LValue value = parameters[i];
      if (value.isConstant() == false)
      {
        return false;
      }
    }
    return true;
  }
}