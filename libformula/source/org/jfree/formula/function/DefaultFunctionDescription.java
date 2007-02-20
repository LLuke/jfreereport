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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function;

import java.util.Locale;

import org.jfree.formula.function.userdefined.UserDefinedFunctionCategory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.AnyType;

/**
 * Creation-Date: 05.11.2006, 15:13:03
 *
 * @author Thomas Morgner
 */
public class DefaultFunctionDescription implements FunctionDescription
{
  private String name;

  public DefaultFunctionDescription(final String name)
  {
    this.name = name;
  }

  public Type getValueType()
  {
    return AnyType.TYPE;
  }

  public FunctionCategory getCategory()
  {
    return UserDefinedFunctionCategory.CATEGORY;
  }

  public int getParameterCount()
  {
    return 1;
  }

  public Type getParameterType(int position)
  {
    return AnyType.TYPE;
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

  public String getDisplayName(Locale locale)
  {
    return name;
  }

  public String getDescription(Locale locale)
  {
    return "";
  }

  public boolean isVolatile()
  {
    // assume the worst ..
    return true;
  }

  public boolean isInfiniteParameterCount()
  {
    return true;
  }

  public String getParameterDisplayName(int position, Locale locale)
  {
    // todo this is surely ugly ..
    return "Parameter " + String.valueOf(position);
  }

  public String getParameterDescription(int position, Locale locale)
  {
    return "";
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
}
