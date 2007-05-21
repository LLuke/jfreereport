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
 * $Id: HourFunctionDescription.java,v 1.1 2007/05/20 21:47:52 mimil Exp $
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
 * Describes HourFunction function.
 * 
 * @see HourFunction
 * 
 * @author Cedric Pronzato
 */
public class HourFunctionDescription extends AbstractFunctionDescription
{
  public HourFunctionDescription()
  {
    super("org.jfree.formula.function.datetime.Hour-Function");
  }

  public Type getValueType()
  {
    return NumberType.GENERIC_NUMBER;
  }

  public int getParameterCount()
  {
    return 1;
  }

  public Type getParameterType(int position)
  {
    return DateTimeType.TIME_TYPE;
  }

  public boolean isParameterMandatory(int position)
  {
    return true;
  }

  public FunctionCategory getCategory()
  {
    return DateTimeFunctionCategory.CATEGORY;
  }
}
