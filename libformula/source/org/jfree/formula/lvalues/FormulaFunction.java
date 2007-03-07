/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id: FormulaFunction.java,v 1.11 2007/02/22 21:34:46 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.lvalues;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.FunctionDescription;
import org.jfree.formula.function.FunctionRegistry;
import org.jfree.formula.function.ParameterCallback;
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
  private static class FormulaParameterCallback implements ParameterCallback
  {
    private TypeValuePair[] backend;
    private FormulaFunction function;

    public FormulaParameterCallback(final FormulaFunction function)
    {
      this.function = function;
      this.backend = new TypeValuePair[function.parameters.length];
    }

    private TypeValuePair get(int pos) throws EvaluationException
    {
      final LValue parameter = function.parameters[pos];
      final Type paramType = function.metaData.getParameterType(pos);
      if (parameter != null)
      {
        final TypeValuePair result = parameter.evaluate();
        // lets do some type checking, right?
        final TypeRegistry typeRegistry = function.getContext().getTypeRegistry();
        final TypeValuePair converted = typeRegistry.convertTo(paramType, result);
        if (converted == null)
        {
          Log.debug("Failed to evaluate parameter " + pos + " on function " + function);
          throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
        }
        return converted;
      }
      else
      {
        return new TypeValuePair(paramType, function.metaData.getDefaultValue(pos));
      }
    }

    public LValue getRaw(int position)
    {
      return function.parameters[position];
    }

    public Object getValue(int position) throws EvaluationException
    {
      final TypeValuePair retval = backend[position];
      if (retval != null)
      {
        return retval.getValue();
      }

      final TypeValuePair pair = get(position);
      backend[position] = pair;
      return pair.getValue();
    }

    public Type getType(int position) throws EvaluationException
    {
      final TypeValuePair retval = backend[position];
      if (retval != null)
      {
        return retval.getType();
      }

      final TypeValuePair pair = get(position);
      backend[position] = pair;
      return pair.getType();
    }

    public int getParameterCount()
    {
      return backend.length;
    }
  }

  private String functionName;
  private LValue[] parameters;
  private Function function;
  private FunctionDescription metaData;

  public FormulaFunction(final String functionName, final LValue[] parameters)
  {
    this.functionName = functionName;
    this.parameters = parameters;
  }

  public void initialize(final FormulaContext context) throws EvaluationException
  {
    super.initialize(context);
    final FunctionRegistry registry = context.getFunctionRegistry();
    function = registry.createFunction(functionName);
    metaData = registry.getMetaData(functionName);

    for (int i = 0; i < parameters.length; i++)
    {
      parameters[i].initialize(context);
    }
  }

  /**
   * Returns the function's name. This is the normalized name and may not be
   * suitable for the user. Query the function's metadata to retrieve a
   * display-name. 
   *
   * @return the function's name.
   */
  public String getFunctionName()
  {
    return functionName;
  }

  /**
   * Returns the initialized function. Be aware that this method will return
   * null if this LValue instance has not yet been initialized.
   *
   * @return the function instance or null, if the FormulaFunction instance has
   * not yet been initialized.
   */
  public Function getFunction()
  {
    return function;
  }

  /**
   * Returns the function's meta-data. Be aware that this method will return
   * null if this LValue instance has not yet been initialized.
   *
   * @return the function description instance or null, if the FormulaFunction
   * instance has not yet been initialized.
   */
  public FunctionDescription getMetaData()
  {
    return metaData;
  }

  public Object clone() throws CloneNotSupportedException
  {
    final FormulaFunction fn = (FormulaFunction) super.clone();
    fn.parameters = (LValue[]) parameters.clone();
    for (int i = 0; i < parameters.length; i++)
    {
      final LValue parameter = parameters[i];
      fn.parameters[i] = (LValue) parameter.clone();
    }
    return fn;
  }

  public TypeValuePair evaluate() throws EvaluationException
  {
    // First, grab the parameters and their types.
    final FormulaContext context = getContext();
    // And if everything is ok, compute the stuff ..
    if (function == null)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_FUNCTION_VALUE);
    }
    return function.evaluate(context, new FormulaParameterCallback(this));
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
        b.append(";");
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
