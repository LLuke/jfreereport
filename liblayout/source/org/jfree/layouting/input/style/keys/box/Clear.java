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
 * Clear.java
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
 * Creation-Date: 28.11.2005, 15:55:25
 *
 * @author Thomas Morgner
 */
public class Clear extends CSSConstant
{
  public static final Clear NONE = new Clear("none");
  public static final Clear LEFT = new Clear("left");
  public static final Clear RIGHT = new Clear("right");
  public static final Clear TOP = new Clear("top");
  public static final Clear BOTTOM = new Clear("bottom");
  public static final Clear INSIDE = new Clear("inside");
  public static final Clear OUTSIDE = new Clear("outside");
  public static final Clear START = new Clear("start");
  public static final Clear END = new Clear("end");
  public static final Clear BOTH = new Clear("both");

  private Clear(final String constant)
  {
    super(constant);
  }
}
