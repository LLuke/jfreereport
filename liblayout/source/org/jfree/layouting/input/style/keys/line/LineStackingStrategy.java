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
 * .java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * : Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.line;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 24.11.2005, 16:38:22
 *
 * @author Thomas Morgner
 */
public class LineStackingStrategy extends CSSConstant
{
  public static final LineStackingStrategy INLINE_LINE_HEIGHT =
          new LineStackingStrategy("inline-line-height");
  public static final LineStackingStrategy BLOCK_LINE_HEIGHT =
          new LineStackingStrategy("block-line-height");
  public static final LineStackingStrategy MAX_LINE_HEIGHT =
          new LineStackingStrategy("max-line-height");
  public static final LineStackingStrategy GRID_HEIGHT =
          new LineStackingStrategy("grid-height");


  private LineStackingStrategy (String name)
  {
    super(name);
  }
}
