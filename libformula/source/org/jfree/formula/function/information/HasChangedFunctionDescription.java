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

package org.jfree.formula.function.information;

import java.util.Locale;

import org.jfree.formula.function.FunctionCategory;
import org.jfree.formula.function.FunctionDescription;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.LogicalType;
import org.jfree.formula.typing.coretypes.TextType;

/**
 * Creation-Date: 31.10.2006, 17:41:12
 *
 * @author Thomas Morgner
 */
public class HasChangedFunctionDescription implements FunctionDescription
{
  public HasChangedFunctionDescription()
  {
  }

  public Type getValueType()
  {
    return LogicalType.TYPE;
  }

  public String getDisplayName(Locale locale)
  {
    return "HASCHANGED";
  }

  public String getDescription(Locale locale)
  {
    return "Checks whether at least one of the given references has changed.";
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
    return TextType.TYPE;
  }

  public String getParameterDisplayName(int position, Locale locale)
  {
    return "ReferencesList";
  }

  public String getParameterDescription(int position, Locale locale)
  {
    return "A list of reference names";
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
    return true;
  }

  public FunctionCategory getCategory()
  {
    return InformationFunctionCategory.CATEGORY;
  }
}
