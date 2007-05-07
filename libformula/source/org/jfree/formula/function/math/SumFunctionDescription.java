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
 * $Id: SumFunctionDescription.java,v 1.6 2007/04/01 13:51:53 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.math;

import org.jfree.formula.function.AbstractFunctionDescription;
import org.jfree.formula.function.FunctionCategory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.NumberType;

/**
 * Creation-Date: 31.10.2006, 17:41:12
 *
 * @author Thomas Morgner
 */
public class SumFunctionDescription extends AbstractFunctionDescription
{
  public SumFunctionDescription()
  {
    super("org.jfree.formula.function.math.Sum-Function");
  }

  public Type getValueType()
  {
    return NumberType.GENERIC_NUMBER;
  }

  public int getParameterCount()
  {
    return 0;
  }

  public boolean isInfiniteParameterCount()
  {
    return true;
  }

  public Type getParameterType(int position)
  {
    return NumberType.GENERIC_NUMBER;
  }

  /**
   * Defines, whether the parameter at the given position is mandatory. A
   * mandatory parameter must be filled in, while optional parameters need not
   * to be filled in.
   *
   * @return
   */
  public boolean isParameterMandatory(int position)
  {
    return false;
  }

  public FunctionCategory getCategory()
  {
    return MathFunctionCategory.CATEGORY;
  }
}
