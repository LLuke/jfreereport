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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.formula.typing;

/**
 * Creation-Date: 10.04.2007, 14:23:22
 *
 * @author Thomas Morgner
 */
public class TypeRegistryUtility
{
  private TypeRegistryUtility()
  {
  }

  public static Number convertToNumber (final TypeRegistry registry,
                                        final Type type,
                                        final Object value,
                                        final Number defaultValue)
  {
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
      return registry.convertToNumber(type, value);
    }
    catch (TypeConversionException e)
    {
      return defaultValue;
    }
  }
}
