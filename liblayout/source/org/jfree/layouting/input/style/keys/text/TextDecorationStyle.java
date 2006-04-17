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
 * TextDecorationStyle.java
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
package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Used for underline, strike-through and overline.
 *
 * @author Thomas Morgner
 */
public class TextDecorationStyle extends CSSConstant
{
  // none | solid | double | dotted | dashed | dot-dash | dot-dot-dash | wave
  public static final TextDecorationStyle NONE = new TextDecorationStyle("none");
  public static final TextDecorationStyle SOLID = new TextDecorationStyle("solid");
  public static final TextDecorationStyle DOUBLE = new TextDecorationStyle("double");
  public static final TextDecorationStyle DOTTED = new TextDecorationStyle("dotted");
  public static final TextDecorationStyle DASHED = new TextDecorationStyle("dashed");
  public static final TextDecorationStyle DOT_DASH = new TextDecorationStyle("dot-dash");
  public static final TextDecorationStyle DOT_DOT_DASH = new TextDecorationStyle("dot-dot-dash");
  public static final TextDecorationStyle WAVE = new TextDecorationStyle("wave");

  private TextDecorationStyle(final String constant)
  {
    super(constant);
  }
}
