/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 * 05-Feb-2002 : Implemented the serializable interface.
 */

package com.jrefinery.report;

import com.jrefinery.report.util.ObjectStreamResolveException;

import java.io.Serializable;
import java.io.ObjectStreamException;

/**
 * Represents the alignment of an element.
 *
 * @author Thomas Morgner
 */
public class ElementAlignment implements Serializable
{
  /** A constant for left alignment. */
  public static final ElementAlignment LEFT = new ElementAlignment("LEFT", Element.LEFT);
  
  /** A constant for center alignment (horizontal). */
  public static final ElementAlignment CENTER = new ElementAlignment("CENTER", Element.CENTER);
  
  /** A constant for right alignment. */
  public static final ElementAlignment RIGHT = new ElementAlignment("RIGHT", Element.RIGHT);
  
  /** A constant for top alignment. */
  public static final ElementAlignment TOP = new ElementAlignment("TOP", Element.TOP);
  
  /** A constant for middle alignment (vertical). */
  public static final ElementAlignment MIDDLE = new ElementAlignment("MIDDLE", Element.MIDDLE);
  
  /** A constant for bottom alignment. */
  public static final ElementAlignment BOTTOM = new ElementAlignment("BOTTOM", Element.BOTTOM);

  /** The alignment name. */
  private final String myName; // for debug only
  
  /** The corresponding constant defined in the <code>Element<code> class. */
  private final int oldAlignment;

  /**
   * Creates a new alignment object.  Since this constructor is private, you cannot create new
   * alignment objects, you can only use the predefined constants. 
   *
   * @param name  the alignment name.
   * @param oldAlignment  the alignment code.
   */
  private ElementAlignment(String name, int oldAlignment)
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
   * Returns the alignment code.
   *
   * @return the alignment code.
   */
  public int getOldAlignment()
  {
    return oldAlignment;
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof ElementAlignment)) return false;

    final ElementAlignment alignment = (ElementAlignment) o;

    if (oldAlignment != alignment.oldAlignment) return false;
    if (!myName.equals(alignment.myName)) return false;

    return true;
  }

  public int hashCode()
  {
    int result;
    result = myName.hashCode();
    result = 29 * result + oldAlignment;
    return result;
  }

  /**
   * Replaces the automaticly generated instance with one of the enumeration
   * instances.
   * @return the resolved element
   * @throws ObjectStreamException if the element could not be resolved.
   */
  protected Object readResolve() throws ObjectStreamException
  {
    if (this.equals(ElementAlignment.LEFT))
      return ElementAlignment.LEFT;
    if (this.equals(ElementAlignment.RIGHT))
      return ElementAlignment.RIGHT;
    if (this.equals(ElementAlignment.CENTER))
      return ElementAlignment.CENTER;
    if (this.equals(ElementAlignment.TOP))
      return ElementAlignment.TOP;
    if (this.equals(ElementAlignment.BOTTOM))
      return ElementAlignment.BOTTOM;
    if (this.equals(ElementAlignment.MIDDLE))
      return ElementAlignment.MIDDLE;
    // unknown element alignment...
    throw new ObjectStreamResolveException();
  }

}
