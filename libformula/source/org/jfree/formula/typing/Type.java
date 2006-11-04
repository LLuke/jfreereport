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
 * Type.java
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
package org.jfree.formula.typing;

/**
 * Creation-Date: 02.11.2006, 09:32:21
 *
 * @author Thomas Morgner
 */
public interface Type
{
  public static final String NUMERIC_UNIT = "unit.numeric";
  public static final String NUMERIC_TYPE = "type.numeric";
  public static final String TEXT_TYPE = "type.text";
  public static final String LOGICAL_TYPE = "type.logical";
  public static final String SCALAR_TYPE = "type.scalar";
  public static final String ANY_TYPE = "type.any";
  public static final String ERROR_TYPE = "type.error";
  public static final String DATE_TYPE = "type.date";
  /** Also for lists .. */
  public static final String ARRAY_TYPE = "type.array";

  public boolean isFlagSet (String name);
  public Object getProperty (String name);
}
