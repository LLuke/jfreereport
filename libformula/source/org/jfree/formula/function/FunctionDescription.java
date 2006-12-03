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

import org.jfree.formula.typing.Type;

/**
 * A static definition of the function's parameters, return values etc.
 * This is a support class with emphasis on GUI tools.
 *
 * However, the parameter declarations are also used when filling in the
 * parameter values.
 *
 * Functions have a defined set of known parameters and can have a unlimited
 * number of optional parameters. If a function declares at least one parameter
 * and declares that its parameter list is infinite, then the last parameter
 * type is used on all remaining parameters.
 *
 * @author Thomas Morgner
 */
public interface FunctionDescription
{
  public String getDisplayName (Locale locale);
  public String getDescription (Locale locale);
  public boolean isVolatile();
  public Type getValueType();
  public FunctionCategory getCategory();

  public int getParameterCount ();
  public boolean isInfiniteParameterCount();

  public Type getParameterType(int position);
  public String getParameterDisplayName(int position, Locale locale);
  public String getParameterDescription(int position, Locale locale);

  /**
   * Defines, whether the parameter at the given position is mandatory. A
   * mandatory parameter must be filled in, while optional parameters need
   * not to be filled in.
   *
   * @return
   */
  public boolean isParameterMandatory(int position);

  /**
   * Returns the default value for an optional parameter. If the value returned
   * here is null, then this either means, that the parameter is mandatory or
   * that the default value is computed by the expression itself.
   *
   * @param position
   * @return
   */
  public Object getDefaultValue (int position);

}
