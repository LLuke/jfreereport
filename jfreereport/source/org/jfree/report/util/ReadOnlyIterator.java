/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ---------------------
 * ReadOnlyIterator.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ReadOnlyIterator.java,v 1.3 2003/08/25 14:29:34 taqua Exp $
 *
 * Changes
 * -------
 * 05.04.2003 : Initial version
 *
 */

package org.jfree.report.util;

import java.util.Iterator;

/**
 * A unmodifiable iterator wrapper.
 *
 * @author Thomas Morgner
 */
public class ReadOnlyIterator implements Iterator
{
  /** The base iterator. */
  private final Iterator base;

  /**
   * Creates a read-only iterator.
   *
   * @param base  the base iterator.
   */
  public ReadOnlyIterator(final Iterator base)
  {
    this.base = base;
  }

  /**
   * Returns <tt>true</tt> if the iteration has more elements. (In other
   * words, returns <tt>true</tt> if <tt>next</tt> would return an element
   * rather than throwing an exception.)
   *
   * @return <tt>true</tt> if the iterator has more elements.
   */
  public boolean hasNext()
  {
    return base.hasNext();
  }

  /**
   * Returns the next element in the iteration.
   *
   * @return the next element in the iteration.
   */
  public Object next()
  {
    return base.next();
  }

  /**
   *
   * Removes from the underlying collection the last element returned by the
   * iterator (optional operation).  This method can be called only once per
   * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
   * the underlying collection is modified while the iteration is in
   * progress in any way other than by calling this method.
   *
   * @exception UnsupportedOperationException if the <tt>remove</tt>
   *            operation is not supported by this Iterator.

   * @exception IllegalStateException if the <tt>next</tt> method has not
   *            yet been called, or the <tt>remove</tt> method has already
   *            been called after the last call to the <tt>next</tt>
   *            method.
   */
  public void remove()
  {
    throw new UnsupportedOperationException();
  }
}
