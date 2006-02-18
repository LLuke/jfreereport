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
 * PageBreak.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: PageBreak.java,v 1.1 2006/02/12 21:54:27 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.page;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.11.2005, 18:00:14
 *
 * @author Thomas Morgner
 */
public class PageBreak extends CSSConstant
{
  public static PageBreak ALWAYS = new PageBreak("always");
  public static PageBreak AVOID = new PageBreak("avoid");
  public static PageBreak LEFT = new PageBreak("left");
  public static PageBreak RIGHT = new PageBreak("right");

  private PageBreak(final String constant)
  {
    super(constant);
  }
}