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
 * TextAutoSpace.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 02.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 02.12.2005, 19:43:56
 *
 * @author Thomas Morgner
 */
public class TextAutoSpace extends CSSConstant
{
  public static final TextAutoSpace NONE = new TextAutoSpace("none");
  public static final TextAutoSpace IDEOGRAPH_NUMERIC = new TextAutoSpace("ideograph-numeric");
  public static final TextAutoSpace IDEOGRAPH_ALPHA = new TextAutoSpace("ideograph-alpha");
  public static final TextAutoSpace IDEOGRAPH_SPACE = new TextAutoSpace("ideograph-space");
  public static final TextAutoSpace IDEOGRAPH_PARENTHESIS = new TextAutoSpace("ideograph-parenthesis");


  private TextAutoSpace(final String constant)
  {
    super(constant);
  }
}
