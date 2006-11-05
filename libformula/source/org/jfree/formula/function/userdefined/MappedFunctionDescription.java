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
 * MappedFunctionMetaData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.function.userdefined;

import java.util.Locale;

import org.jfree.formula.typing.Type;
import org.jfree.formula.function.FunctionDescription;
import org.jfree.formula.function.FunctionCategory;

/**
 * Creation-Date: 05.11.2006, 14:01:22
 *
 * @author Thomas Morgner
 */
public class MappedFunctionDescription implements FunctionDescription
{
  public MappedFunctionDescription()
  {
  }

  public String getDisplayName(Locale locale)
  {
    return null;
  }

  public String getDescription(Locale locale)
  {
    return null;
  }

  public boolean isVolatile()
  {
    return false;
  }

  public Type getValueType()
  {
    return null;
  }

  public int getParameterCount()
  {
    return 0;
  }

  public boolean isInfiniteParameterCount()
  {
    return false;
  }

  public Type getParameterType(int position)
  {
    return null;
  }

  public String getParameterDisplayName(int position, Locale locale)
  {
    return null;
  }

  public String getParameterDescription(int position, Locale locale)
  {
    return null;
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

  public FunctionCategory getCategory()
  {
    return UserDefinedFunctionCategory.CATEGORY;
  }
}
