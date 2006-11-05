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
 * SumFunctionMetaData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SumFunctionDescription.java,v 1.2 2006/11/04 17:27:37 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.function.math;

import java.util.Locale;

import org.jfree.formula.function.FunctionDescription;
import org.jfree.formula.function.FunctionCategory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.NumberType;

/**
 * Creation-Date: 31.10.2006, 17:41:12
 *
 * @author Thomas Morgner
 */
public class SumFunctionDescription implements FunctionDescription
{
  public SumFunctionDescription()
  {
  }

  public Type getValueType()
  {
    return NumberType.GENERIC_NUMBER;
  }

  public String getDisplayName(Locale locale)
  {
    return "SUM";
  }

  public String getDescription(Locale locale)
  {
    return "Adds all given numeric arguments.";
  }

  public int getParameterCount()
  {
    return 1;
  }

  public boolean isInfiniteParameterCount()
  {
    return true;
  }

  public Type getParameterType(int position)
  {
    return NumberType.GENERIC_NUMBER_ARRAY;
  }

  public String getParameterDisplayName(int position, Locale locale)
  {
    return "NumberList";
  }

  public String getParameterDescription(int position, Locale locale)
  {
    return "A list of numeric parameters";
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

  /**
   * Returns the default value for an optional parameter. If the value returned
   * here is null, then this either means, that the parameter is mandatory or
   * that the default value is computed by the expression itself.
   *
   * @param position
   * @return
   */
  public Object getDefaultValue(int position)
  {
    return null;
  }

  public boolean isVolatile()
  {
    return false;
  }

  public FunctionCategory getCategory()
  {
    return MathFunctionCategory.CATEGORY;
  }
}
