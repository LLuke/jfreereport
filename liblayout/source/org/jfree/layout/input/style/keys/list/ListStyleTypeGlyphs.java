/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * ListStyleTypeGlyphs.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.list;

import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.LibLayoutBoot;

/**
 * Creation-Date: 01.12.2005, 18:40:19
 *
 * @author Thomas Morgner
 */
public class ListStyleTypeGlyphs extends CSSConstant
{
   // box | check | circle | diamond | disc | hyphen | square
  public static final ListStyleTypeGlyphs BOX = new ListStyleTypeGlyphs("box");
  public static final ListStyleTypeGlyphs CHECK = new ListStyleTypeGlyphs("check");
  public static final ListStyleTypeGlyphs CIRCLE = new ListStyleTypeGlyphs("circle");
  public static final ListStyleTypeGlyphs DIAMOND = new ListStyleTypeGlyphs("diamon");
  public static final ListStyleTypeGlyphs DISC = new ListStyleTypeGlyphs("disc");
  public static final ListStyleTypeGlyphs HYPHEN = new ListStyleTypeGlyphs("hyphen");
  public static final ListStyleTypeGlyphs SQUARE = new ListStyleTypeGlyphs("square");

  private ListStyleTypeGlyphs(final String constant)
  {
    super(constant);
  }
}
