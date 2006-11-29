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

import org.jfree.formula.function.AbstractFunctionDescription;
import org.jfree.formula.function.FunctionCategory;
import org.jfree.formula.function.userdefined.UserDefinedFunctionCategory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.AnyType;
import org.jfree.formula.typing.coretypes.TextType;

/**
 * Creation-Date: 29.11.2006, 18:04:18
 *
 * @author Thomas Morgner
 */
public class AttrFunctionDescription extends AbstractFunctionDescription
{
  public AttrFunctionDescription()
  {
    super("org.jfree.report.expressions.formula.sys.Attr-Function");
  }

  public int getParameterCount()
  {
    return 1;
  }

  public boolean isInfiniteParameterCount()
  {
    return false;
  }

  public Type getParameterType(int position)
  {
    return TextType.TYPE;
  }

  public Type getValueType()
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
    if (position == 0)
    {
      return true;
    }
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
