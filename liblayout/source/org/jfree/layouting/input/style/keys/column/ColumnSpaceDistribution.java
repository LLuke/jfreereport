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
 * ColumnSpaceDistribution.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ColumnSpaceDistribution.java,v 1.2 2006/04/17 20:51:01 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.keys.column;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 03.12.2005, 21:11:43
 *
 * @author Thomas Morgner
 */
public class ColumnSpaceDistribution
{
  // [start || end || inner || outer || between] | inherit
  public static final CSSConstant START =
          new CSSConstant("start");
  public static final CSSConstant END =
          new CSSConstant("end");
  public static final CSSConstant INNER =
          new CSSConstant("inner");
  public static final CSSConstant OUTER =
          new CSSConstant("outer");
  public static final CSSConstant BETWEEN =
          new CSSConstant("between");

  private ColumnSpaceDistribution()
  {
  }
}
