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
package org.jfree.formula.lvalues;

import java.io.Serializable;

import org.jfree.formula.typing.Type;

/**
 * Creation-Date: 02.11.2006, 10:02:54
 *
 * @author Thomas Morgner
 */
public class TypeValuePair implements Serializable
{
  private Type type;
  private Object value;

  public TypeValuePair(final Type type, final Object value)
  {
    this.type = type;
    this.value = value;
  }

  public Type getType()
  {
    return type;
  }

  public Object getValue()
  {
    return value;
  }


  public String toString()
  {
    return "TypeValuePair{" +
        "type=" + type +
        ", value=" + value +
        '}';
  }
}
