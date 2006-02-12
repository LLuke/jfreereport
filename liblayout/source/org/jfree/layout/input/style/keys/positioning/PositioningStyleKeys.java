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
 * PositioningStyleKeys.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 08.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.positioning;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Creation-Date: 08.12.2005, 14:50:40
 *
 * @author Thomas Morgner
 */
public class PositioningStyleKeys
{
  /** Width and height are defined in the Box-module. */

  public static final StyleKey TOP =
          StyleKeyRegistry.getRegistry().createKey
                  ("top", false, false, false);

  public static final StyleKey LEFT =
          StyleKeyRegistry.getRegistry().createKey
                  ("left", false, false, false);

  public static final StyleKey BOTTOM =
          StyleKeyRegistry.getRegistry().createKey
                  ("bottom", false, false, false);

  public static final StyleKey RIGHT =
          StyleKeyRegistry.getRegistry().createKey
                  ("right", false, false, false);

  public static final StyleKey POSITION =
          StyleKeyRegistry.getRegistry().createKey
                  ("position", false, false, false);

  public static final StyleKey Z_INDEX =
          StyleKeyRegistry.getRegistry().createKey
                  ("z-index", false, false, false);


  private PositioningStyleKeys()
  {
  }
}
