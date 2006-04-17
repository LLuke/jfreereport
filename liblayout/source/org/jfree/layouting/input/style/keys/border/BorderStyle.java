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
 * BorderStyle.java
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

package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 19:37:35
 *
 * @author Thomas Morgner
 */
public class BorderStyle extends CSSConstant
{
  public static final BorderStyle NONE = new BorderStyle("none");
  public static final BorderStyle HIDDEN = new BorderStyle("hidden");
  public static final BorderStyle DOTTED = new BorderStyle("dotted");
  public static final BorderStyle DASHED = new BorderStyle("dashed");
  public static final BorderStyle SOLID = new BorderStyle("solid");
  public static final BorderStyle DOUBLE = new BorderStyle("double");
  public static final BorderStyle DOT_DASH = new BorderStyle("dot-dash");
  public static final BorderStyle DOT_DOT_DASH = new BorderStyle("dot-dot-dash");
  public static final BorderStyle WAVE = new BorderStyle("wave");
  public static final BorderStyle GROOVE = new BorderStyle("groove");
  public static final BorderStyle RIDGE = new BorderStyle("ridge");
  public static final BorderStyle INSET = new BorderStyle("inset");
  public static final BorderStyle OUTSET = new BorderStyle("outset");

  private BorderStyle(String name)
  {
    super(name);
  }
}
