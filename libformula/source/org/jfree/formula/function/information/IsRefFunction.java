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
 * $Id: IsTextFunction.java,v 1.5 2007/04/01 13:51:52 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.information;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.ContextLookup;
import org.jfree.formula.lvalues.LValue;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.coretypes.LogicalType;

/**
 * This function retruns true if the given value is reference.
 * 
 * @author Cedric Pronzato
 * 
 */
public class IsRefFunction implements Function
{
  private static final TypeValuePair RETURN_TRUE = new TypeValuePair(
      LogicalType.TYPE, Boolean.TRUE);

  private static final TypeValuePair RETURN_FALSE = new TypeValuePair(
      LogicalType.TYPE, Boolean.FALSE);

  public IsRefFunction()
  {
  }

  public TypeValuePair evaluate(FormulaContext context,
      ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount < 1)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }

    // we want error values propagated so we need to evaluate the parameter
    parameters.getValue(0);

    final LValue raw = parameters.getRaw(0);
    if (raw instanceof ContextLookup)
    {
      return RETURN_TRUE;
    }

    return RETURN_FALSE;
  }

  public String getCanonicalName()
  {
    return "ISREF";
  }

}
