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
 * $Id: DateFunction.java,v 1.14 2007/05/07 22:57:01 mimil Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.DateTimeType;
import org.jfree.formula.util.DateUtil;

/**
 * Creation-Date: 04.11.2006, 18:59:11
 * 
 * @author Thomas Morgner
 */
public class DateFunction implements Function
{
  public DateFunction()
  {
  }

  public String getCanonicalName()
  {
    return "DATE";
  }

  public TypeValuePair evaluate(final FormulaContext context,
      final ParameterCallback parameters) throws EvaluationException
  {
    if (parameters.getParameterCount() != 3)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }

    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final Number n1 = typeRegistry.convertToNumber(parameters.getType(0),
        parameters.getValue(0));
    final Number n2 = typeRegistry.convertToNumber(parameters.getType(1),
        parameters.getValue(1));
    final Number n3 = typeRegistry.convertToNumber(parameters.getType(2),
        parameters.getValue(2));

    if (n1 == null || n2 == null || n3 == null)
    {
      throw new EvaluationException(
          LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    final LocalizationContext localizationContext = context
        .getLocalizationContext();
    final java.sql.Date date = DateUtil.createDate(n1.intValue(),
        n2.intValue(), n3.intValue(), localizationContext);

    return new TypeValuePair(DateTimeType.DATE_TYPE, date);
  }
}
