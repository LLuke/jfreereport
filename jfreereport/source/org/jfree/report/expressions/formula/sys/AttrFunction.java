/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.expressions.formula.sys;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.coretypes.AnyType;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.report.expressions.ReportFormulaContext;

/**
 * Creation-Date: 24.11.2006, 13:02:41
 *
 * @author Thomas Morgner
 */
public class AttrFunction implements Function
{
  public AttrFunction()
  {
  }

  public String getCanonicalName()
  {
    return "ATTR";
  }

  public TypeValuePair evaluate(FormulaContext context,
                                ParameterCallback parameters)
      throws EvaluationException
  {
    // we expect strings and will check, whether the reference for theses
    // strings is dirty.
    if (context instanceof ReportFormulaContext == false)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue
          (LibFormulaErrorValue.ERROR_REFERENCE_NOT_RESOLVABLE));
    }
    if (parameters.getParameterCount() == 1)
    {
      final String value = (String) parameters.getValue(0);
      final ReportFormulaContext reportFormulaContext =
          (ReportFormulaContext) context;
      return new TypeValuePair(AnyType.TYPE,
          reportFormulaContext.getElement().getAttribute(value));
    }
    else if (parameters.getParameterCount() == 2)
    {
      final String namespace = (String) parameters.getValue(0);
      final String attrName = (String) parameters.getValue(1);
      final ReportFormulaContext reportFormulaContext =
          (ReportFormulaContext) context;
      return new TypeValuePair(AnyType.TYPE,
          reportFormulaContext.getElement().getAttribute(namespace, attrName));
    }
    return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue
        (LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
  }

}
