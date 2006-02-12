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
 * HangingPunctuation.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: HangingPunctuation.java,v 1.1 2006/02/12 21:54:27 taqua Exp $
 *
 * Changes
 * -------------------------
 * 03.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 03.12.2005, 20:00:51
 *
 * @author Thomas Morgner
 */
public class HangingPunctuation extends CSSConstant
{
  public static final HangingPunctuation NONE = new HangingPunctuation("none");
  public static final HangingPunctuation START = new HangingPunctuation("start");
  public static final HangingPunctuation END = new HangingPunctuation("end");
  public static final HangingPunctuation BOTH = new HangingPunctuation("both");

  private HangingPunctuation(final String constant)
  {
    super(constant);
  }
}
