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
 * FloatReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: FloatReadHandler.java,v 1.1 2006/02/12 21:57:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 28.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.box;

import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.keys.box.Floating;

/**
 * Creation-Date: 28.11.2005, 15:52:18
 *
 * @author Thomas Morgner
 */
public class FloatReadHandler extends OneOfConstantsReadHandler
{
  public FloatReadHandler()
  {
    super( false);
    addValue(Floating.BOTTOM);
    addValue(Floating.END);
    addValue(Floating.INSIDE);
    addValue(Floating.LEFT);
    addValue(Floating.NONE);
    addValue(Floating.OUTSIDE);
    addValue(Floating.RIGHT);
    addValue(Floating.START);
    addValue(Floating.TOP);
    addValue(Floating.IN_COLUMN);
    addValue(Floating.MID_COLUMN);
  }
}