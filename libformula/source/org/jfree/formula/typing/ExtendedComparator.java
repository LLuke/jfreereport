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
 * $Id: ExtendedComparator.java,v 1.2 2006/12/03 19:22:28 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.typing;

/**
 * A comparator, that offers type support. Unlike the plain Java-Comparator,
 * this class is able to compare
 *
 * @author Thomas Morgner
 */
public interface ExtendedComparator
{
  public boolean isEqual(final Type type1,
                         final Object value1,
                         final Type type2,
                         final Object value2);

  /**
   * Returns null, if the types are not comparable and are not convertible at
   * all.
   *
   * @param type1
   * @param value1
   * @param type2
   * @param value2
   * @return
   */
  public Integer compare(final Type type1,
                         final Object value1,
                         final Type type2,
                         final Object value2);
}
