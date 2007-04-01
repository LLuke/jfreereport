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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.information;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;

/**
 * This function class represents the constant error NA.
 *
 * @author Cedric Pronzato
 *
 */
public class NaFunction implements Function
{
  public NaFunction()
  {
  }

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    if(parameters.getParameterCount() != 0)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }

    throw new EvaluationException(LibFormulaErrorValue.ERROR_NA_VALUE);
  }

  public String getCanonicalName()
  {
    return "NA";
  }

}
