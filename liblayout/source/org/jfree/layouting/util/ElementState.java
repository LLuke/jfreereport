/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * ElementState.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
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

package org.jfree.layouting.util;

/**
 * Creation-Date: 31.10.2005, 13:05:57
 *
 * @author Thomas Morgner
 */
public final class ElementState implements Comparable
{
  /**
   * The element has been constructed and is not closed.
   */
  public static final ElementState INPUT_NEW = new ElementState(0);
  /**
   * The element itself is closed, but one of the childs is not yet finished.
   * (Check, whether and how we can implement that in a performant way.)
   */
  public static final ElementState INPUT_CLOSE_PENDING = new ElementState(1);
  /**
   * The element construction is fully done. The element will not be changed
   * anymore.
   */
  public static final ElementState INPUT_CLOSED = new ElementState(2);
  /**
   * The stylesheet cascade has been resolved. Each element has a set of
   * absolute or relative properties - no inheritance needs to be considered
   * anymore.
   */
  public static final ElementState LAYOUT_CASCADED = new ElementState(3);
  /**
   * The generated content has been added. Generated content is text only.
   * It cannot generate new document nodes other than CDATA sections.
   */
  public static final ElementState LAYOUT_GENERATED = new ElementState(4);
  /**
   * The stylesheet properties have been computed. All missing style properties
   * must be resolved during the actual layouting process. This involves
   * building the anonymous boxes needed for the layouting and doing all the
   * pagination.
   */
  public static final ElementState LAYOUT_COMPUTED = new ElementState(5);
  /**
   * Everything is done. The fully computed page can be saved and reused
   * later or that document part can be forwarded to the output engine.
   */
  public static final ElementState LAYOUT_DONE = new ElementState(6);

  /**
   * The weight specifies how the layouting process has proceeded so far. 
   */
  private int weight;

  private ElementState (int weight)
  {
    this.weight = weight;
  }

  public int compareTo(final Object o)
  {
    ElementState es = (ElementState) o;
    if (weight < es.weight)
    {
      return -1;
    }
    if (weight > es.weight)
    {
      return 1;
    }
    return 0;
  }
}
