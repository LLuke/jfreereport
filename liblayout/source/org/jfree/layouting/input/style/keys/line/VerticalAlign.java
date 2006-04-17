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
 * $Id$
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
public class VerticalAlign extends CSSConstant
{
  public static final VerticalAlign USE_SCRIPT =
          new VerticalAlign("use-script");
  public static final VerticalAlign BASELINE =
          new VerticalAlign("baseline");
  public static final VerticalAlign SUB =
          new VerticalAlign("sub");
  public static final VerticalAlign SUPER =
          new VerticalAlign("super");

  public static final VerticalAlign TOP =
          new VerticalAlign("top");
  public static final VerticalAlign TEXT_TOP =
          new VerticalAlign("text-top");
  public static final VerticalAlign CENTRAL =
          new VerticalAlign("central");
  public static final VerticalAlign MIDDLE =
          new VerticalAlign("middle");
  public static final VerticalAlign BOTTOM =
          new VerticalAlign("bottom");
  public static final VerticalAlign TEXT_BOTTOM =
          new VerticalAlign("text-bottom");

  private VerticalAlign (String name)
  {
    super(name);
  }
}
