/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: ElementAlignment.java,v 1.1 2003/07/07 22:43:59 taqua Exp $
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
public class ElementAlignment implements Serializable
{
  /** A constant for left alignment. */
  public static final ElementAlignment LEFT = new ElementAlignment("LEFT", 1);

  /** A constant for center alignment (horizontal). */
  public static final ElementAlignment CENTER = new ElementAlignment("CENTER", 3);

  /** A constant for right alignment. */
  public static final ElementAlignment RIGHT = new ElementAlignment("RIGHT", 2);

  /** A constant for top alignment. */
  public static final ElementAlignment TOP = new ElementAlignment("TOP", 14);

  /** A constant for middle alignment (vertical). */
  public static final ElementAlignment MIDDLE = new ElementAlignment("MIDDLE", 15);

  /** A constant for bottom alignment. */
  public static final ElementAlignment BOTTOM = new ElementAlignment("BOTTOM", 16);

  /** The alignment name. */
  private final String myName; // for debug only

  /** The corresponding constant defined in the <code>Element<code> class. */
  private final int oldAlignment;

  /**
   * Creates a new alignment object.  Since this constructor is private, you cannot create new
   * alignment objects, you can only use the predefined constants.
   *
   * @param name  the alignment name.
   * @param oldAlignment  the old alignment code.
   */
  private ElementAlignment(final String name, final int oldAlignment)
  {
    myName = name;
    this.oldAlignment = oldAlignment;
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
   * Returns the alignment code, used by the old XML parsing code, that corresponds to this
   * alignment object.
   *
   * @return the alignment code.
   */
  public int getOldAlignment()
  {
    return oldAlignment;
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

    if (oldAlignment != alignment.oldAlignment)
    {
      return false;
    }
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
    result = 29 * result + oldAlignment;
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

  /**
   * Translates the old alignment (<code>int</code>) constants into the new
   * {@link ElementAlignment} objects.
   *
   * @param alignment  the alignment code.
   *
   * @return The corresponding alignment object.
   *
   * @throws IllegalArgumentException if the supplied code does not match one
   *                                  of the predefined constant values.
   */
  public static ElementAlignment translateHorizontalAlignment(final int alignment)
  {
    if (alignment == LEFT.getOldAlignment())
    {
      return ElementAlignment.LEFT;
    }
    else if (alignment == RIGHT.getOldAlignment())
    {
      return ElementAlignment.RIGHT;
    }
    else if (alignment == CENTER.getOldAlignment())
    {
      return ElementAlignment.CENTER;
    }
    else
    {
      throw new IllegalArgumentException("The alignment must be one of LEFT, RIGHT or CENTER.");
    }
  }

  /**
   * Translates the old alignment (<code>int</code>) constants into the new
   * {@link ElementAlignment} objects.
   *
   * @param alignment  the alignment code.
   *
   * @return The corresponding alignment object.
   *
   * @throws IllegalArgumentException if the supplied code does not match one
   *                                  of the predefined constant values.
   */
  public static ElementAlignment translateVerticalAlignment(final int alignment)
  {
    if (alignment == TOP.getOldAlignment())
    {
      return ElementAlignment.TOP;
    }
    else if (alignment == BOTTOM.getOldAlignment())
    {
      return ElementAlignment.BOTTOM;
    }
    else if (alignment == MIDDLE.getOldAlignment())
    {
      return ElementAlignment.MIDDLE;
    }
    else
    {
      throw new IllegalArgumentException("The alignment must be one of TOP, BOTTOM or MIDDLE.");
    }
  }

}
