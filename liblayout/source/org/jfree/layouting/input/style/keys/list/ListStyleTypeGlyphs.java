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
 * ListStyleTypeGlyphs.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ListStyleTypeGlyphs.java,v 1.2 2006/04/17 20:51:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.keys.list;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 01.12.2005, 18:40:19
 *
 * @author Thomas Morgner
 */
public class ListStyleTypeGlyphs
{
   // box | check | circle | diamond | disc | hyphen | square
  public static final CSSConstant BOX = new CSSConstant("box");
  public static final CSSConstant CHECK = new CSSConstant("check");
  public static final CSSConstant CIRCLE = new CSSConstant("circle");
  public static final CSSConstant DIAMOND = new CSSConstant("diamon");
  public static final CSSConstant DISC = new CSSConstant("disc");
  public static final CSSConstant HYPHEN = new CSSConstant("hyphen");
  public static final CSSConstant SQUARE = new CSSConstant("square");

  private ListStyleTypeGlyphs()
  {
  }
}
