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
 * $Id: TypeRegistry.java,v 1.5 2007/01/14 17:49:41 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.typing;

import java.util.Date;

import org.jfree.formula.ErrorValue;
import org.jfree.formula.lvalues.TypeValuePair;

/**
 * The type registry manages the known value types.
 *
 * @author Thomas Morgner
 */
public interface TypeRegistry
{
  /**
   * Returns an comparator for the given types.
   *
   * @param type1
   * @param type2
   * @return
   */
  public ExtendedComparator getComparator(Type type1, Type type2);

  /**
   * Converts the object of the given type into a number. If the object is not
   * convertible, a NumberFormatException is thrown. (This conversion is used
   * by the operator implementations.)
   *
   * @param type1
   * @param value
   * @return the value as number or ZERO if the value is unconvertible.
   * @throws NumberFormatException if the type cannot be represented as number.
   */
  public Number convertToNumber (Type type1, Object value)
      throws NumberFormatException;

  /**
   * (This conversion is used by the operator implementations.)
   *
   * @param type1
   * @param value
   * @return the value as string or an empty string.
   */
  public String convertToText (Type type1, Object value);

  /**
   * Converts the object of the given type into a boolean.
   * 
   * @param type1
   * @param value
   * @return The value as Boolean or null.
   */
  public Boolean convertToLogical (Type type1, Object value);
  
  /**
   * Converts the object of the given type into a date.
   * 
   * @param type1
   * @param value
   * @return The value as Date or null.
   */
  public Date convertToDate(Type type1, Object value);
  
  /**
   * Checks, whether the target type would accept the specified value object
   * and value type. (This conversion is used by the functions.)
   *
   * @param targetType
   * @param valuePair
   */
  public TypeValuePair convertTo(final Type targetType,
                                 final TypeValuePair valuePair);
  
  /**
   * Returns the first non null error value of the passed arguments or null if there is no error.
   * It is used ot propagate the errors. 
   * 
   * @param value1 First value to check.
   * @param value2 Second value to check.
   * @return The error or null if no errors.
   */
  public TypeValuePair getError(final TypeValuePair value1, final TypeValuePair value2);
}
