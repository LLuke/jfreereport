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
 * VerticalAlign.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: VerticalAlign.java,v 1.2 2006/04/17 20:51:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.keys.line;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 24.11.2005, 17:08:01
 *
 * @author Thomas Morgner
 */
public class VerticalAlign
{
  public static final CSSConstant USE_SCRIPT =
          new CSSConstant("use-script");
  public static final CSSConstant BASELINE =
          new CSSConstant("baseline");
  public static final CSSConstant SUB =
          new CSSConstant("sub");
  public static final CSSConstant SUPER =
          new CSSConstant("super");

  public static final CSSConstant TOP =
          new CSSConstant("top");
  public static final CSSConstant TEXT_TOP =
          new CSSConstant("text-top");
  public static final CSSConstant CENTRAL =
          new CSSConstant("central");
  public static final CSSConstant MIDDLE =
          new CSSConstant("middle");
  public static final CSSConstant BOTTOM =
          new CSSConstant("bottom");
  public static final CSSConstant TEXT_BOTTOM =
          new CSSConstant("text-bottom");

  private VerticalAlign ()
  {
  }
}
