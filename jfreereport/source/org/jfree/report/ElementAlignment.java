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
 * ElementAlignment.java
 * ---------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ElementAlignment.java,v 1.5 2003/08/25 14:29:28 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 * 05-Feb-2002 : Implemented the serializable interface.
 * 23-Feb-2003 : Added compatibility methods for the old int element alignment.
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 */

package org.jfree.report;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.jfree.report.util.ObjectStreamResolveException;

/**
 * Represents the alignment of an element.
 *
 * @author Thomas Morgner
 */
public final class ElementAlignment implements Serializable
{
  /** A constant for left alignment. */
  public static final ElementAlignment LEFT = new ElementAlignment("LEFT");

  /** A constant for center alignment (horizontal). */
  public static final ElementAlignment CENTER = new ElementAlignment("CENTER");

  /** A constant for right alignment. */
  public static final ElementAlignment RIGHT = new ElementAlignment("RIGHT");

  /** A constant for top alignment. */
  public static final ElementAlignment TOP = new ElementAlignment("TOP");

  /** A constant for middle alignment (vertical). */
  public static final ElementAlignment MIDDLE = new ElementAlignment("MIDDLE");

  /** A constant for bottom alignment. */
  public static final ElementAlignment BOTTOM = new ElementAlignment("BOTTOM");

  /** The alignment name. */
  private final String myName; // for debug only

  /**
   * Creates a new alignment object.  Since this constructor is private, you cannot create new
   * alignment objects, you can only use the predefined constants.
   *
   * @param name  the alignment name.
   */
  private ElementAlignment(final String name)
  {
    myName = name;
  }

  /**
   * Returns the alignment name.
   *
   * @return the alignment name.
   */
  public String toString()
  {
    return myName;
  }

  /**
   * Returns <code>true</code> if this object is equal to the specified object, and
   * <code>false</code> otherwise.
   *
   * @param o  the other object.
   *
   * @return A boolean.
   */
  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof ElementAlignment))
    {
      return false;
    }

    final ElementAlignment alignment = (ElementAlignment) o;

    if (!myName.equals(alignment.myName))
    {
      return false;
    }

    return true;
  }

  /**
   * Returns a hash code for the alignment object.
   *
   * @return The code.
   */
  public int hashCode()
  {
    int result;
    result = myName.hashCode();
    return result;
  }

  /**
   * Replaces the automatically generated instance with one of the enumeration instances.
   *
   * @return the resolved element
   *
   * @throws ObjectStreamException if the element could not be resolved.
   */
  protected Object readResolve() throws ObjectStreamException
  {
    if (this.equals(ElementAlignment.LEFT))
    {
      return ElementAlignment.LEFT;
    }
    if (this.equals(ElementAlignment.RIGHT))
    {
      return ElementAlignment.RIGHT;
    }
    if (this.equals(ElementAlignment.CENTER))
    {
      return ElementAlignment.CENTER;
    }
    if (this.equals(ElementAlignment.TOP))
    {
      return ElementAlignment.TOP;
    }
    if (this.equals(ElementAlignment.BOTTOM))
    {
      return ElementAlignment.BOTTOM;
    }
    if (this.equals(ElementAlignment.MIDDLE))
    {
      return ElementAlignment.MIDDLE;
    }
    // unknown element alignment...
    throw new ObjectStreamResolveException();
  }
}
