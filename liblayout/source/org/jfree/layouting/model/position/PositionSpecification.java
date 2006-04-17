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
 * PositionSpecification.java
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

package org.jfree.layouting.model.position;

import org.jfree.layouting.input.style.keys.positioning.Position;

public class PositionSpecification
{
  private Position position;
  private Long top;
  private Long left;
  private Long bottom;
  private Long right;

  private int z;

  public PositionSpecification ()
  {
  }

  public Long getBottom ()
  {
    return bottom;
  }

  public void setBottom (Long bottom)
  {
    this.bottom = bottom;
  }

  public Long getLeft ()
  {
    return left;
  }

  public void setLeft (Long left)
  {
    this.left = left;
  }

  public Position getPosition ()
  {
    return position;
  }

  public void setPosition (Position position)
  {
    this.position = position;
  }

  public Long getRight ()
  {
    return right;
  }

  public void setRight (Long right)
  {
    this.right = right;
  }

  public Long getTop ()
  {
    return top;
  }

  public void setTop (Long top)
  {
    this.top = top;
  }

  public int getZ ()
  {
    return z;
  }

  public void setZ (int z)
  {
    this.z = z;
  }
}
