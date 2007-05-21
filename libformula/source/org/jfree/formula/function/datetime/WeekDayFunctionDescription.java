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
 * $Id: WeekDayFunctionDescription.java,v 1.1 2007/05/20 21:47:51 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

import org.jfree.formula.function.AbstractFunctionDescription;
import org.jfree.formula.function.FunctionCategory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.DateTimeType;
import org.jfree.formula.typing.coretypes.NumberType;

/**
 * Describes WeekDayFunction function.
 * 
 * @see WeekDayFunction
 * 
 * @author Cedric Pronzato
 */
public class WeekDayFunctionDescription extends AbstractFunctionDescription
{
  public WeekDayFunctionDescription()
  {
    super("org.jfree.formula.function.datetime.WeekDay-Function");
  }

  public Type getValueType()
  {
    return NumberType.GENERIC_NUMBER;
  }

  public int getParameterCount()
  {
    return 2;
  }

  public Type getParameterType(int position)
  {
    if (position == 0)
    {
      return DateTimeType.DATE_TYPE;
    }
    return NumberType.GENERIC_NUMBER;
  }

  public boolean isParameterMandatory(int position)
  {
    if (position == 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public FunctionCategory getCategory()
  {
    return DateTimeFunctionCategory.CATEGORY;
  }
}
