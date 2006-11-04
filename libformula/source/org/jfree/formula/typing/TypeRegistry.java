/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * TypeRegistry.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 02.11.2006 : Initial version
 */
package org.jfree.formula.typing;

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
   * converts the object of the given type into a number. If the object is not
   * convertible, a NumberFormatException is thrown.
   *
   * @param type1
   * @param value
   * @return
   * @throws NumberFormatException if the type cannot be represented as number.
   */
  public Number convertToNumber (Type type1, Object value)
      throws NumberFormatException;

  public String convertToText (Type type1, Object value);

  /**
   * Checks, whether the target type would accept the specified value object
   * and value type.
   *
   * @param targetType
   * @param valuePair
   */
  public TypeValuePair convertTo(final Type targetType,
                                 final TypeValuePair valuePair);
}
