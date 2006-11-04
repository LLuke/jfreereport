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
 * NumberType.java
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
package org.jfree.formula.typing.coretypes;

import org.jfree.formula.typing.DefaultType;
import org.jfree.formula.typing.Type;

/**
 * Creation-Date: 02.11.2006, 09:37:54
 *
 * @author Thomas Morgner
 */
public class NumberType extends DefaultType
{
  public static final NumberType GENERIC_NUMBER;
  public static final NumberType GENERIC_NUMBER_ARRAY;

  static {
    GENERIC_NUMBER = new NumberType();
    GENERIC_NUMBER.lock();

    GENERIC_NUMBER_ARRAY = new NumberType();
    GENERIC_NUMBER_ARRAY.addFlag(Type.ARRAY_TYPE);
    GENERIC_NUMBER_ARRAY.lock();
  }

  public NumberType()
  {
    addFlag(Type.NUMERIC_TYPE);
    addFlag(Type.SCALAR_TYPE);
  }
}
