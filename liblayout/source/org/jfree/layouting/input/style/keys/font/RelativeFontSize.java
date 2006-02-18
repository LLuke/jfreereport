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
 * RelativeFontSizeConstant.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: RelativeFontSize.java,v 1.1 2006/02/12 21:54:26 taqua Exp $
 *
 * Changes
 * -------------------------
 * 18.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.font;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 18.12.2005, 17:14:42
 *
 * @author Thomas Morgner
 */
public class RelativeFontSize extends CSSConstant
{
  public static final RelativeFontSize SMALLER = new RelativeFontSize("smaller");
  public static final RelativeFontSize LARGER = new RelativeFontSize("larger");

  private RelativeFontSize(final String constant)
  {
    super(constant);
  }

}