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
 * Floating.java
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

package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Defines the floating property. Floating elements create a new flow inside
 * an existing flow.
 * <p>
 * The properties left and top are equivalent, as as right and bottom.
 * All properties in the specification can be reduced to either left or
 * right in the computation phase.
 * <p>
 * Floating images cannot leave their containing block vertically or horizontally.
 * If negative margins are given, they may be shifted outside the content area,
 * but vertical margins will increase the 'empty-space' between the blocks instead
 * of messing up the previous element.
 *
 * @author Thomas Morgner
 */
public class Floating extends CSSConstant
{
  public static final Floating LEFT = new Floating("left");
  public static final Floating RIGHT = new Floating("right");
  public static final Floating TOP = new Floating("top");
  public static final Floating BOTTOM = new Floating("bottom");
  public static final Floating INSIDE = new Floating("inside");
  public static final Floating OUTSIDE = new Floating("outside");
  public static final Floating START = new Floating("start");
  public static final Floating END = new Floating("end");
  public static final Floating NONE = new Floating("none");

  // from the column stuff
  public static final Floating IN_COLUMN = new Floating("in-column");
  public static final Floating MID_COLUMN = new Floating("mid-column");

  private Floating(String name)
  {
    super(name);
  }

}
