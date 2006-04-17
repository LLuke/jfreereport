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
 * ColorStyleKeys.java
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

package org.jfree.layouting.input.style.keys.color;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Creation-Date: 30.10.2005, 18:47:30
 *
 * @author Thomas Morgner
 */
public class ColorStyleKeys
{
  public static final StyleKey COLOR =
          StyleKeyRegistry.getRegistry().createKey
          ("color", false, true, false);

  /**
   * Not sure whether we can implement this one. It is a post-processing
   * operation, and may or may not be supported by the output target.
   */
  public static final StyleKey OPACITY =
          StyleKeyRegistry.getRegistry().createKey
          ("opacity", false, false, false);

  /**
   * For now, we do not care about color profiles. This might have to do with
   * me being clueless about the topic, but also with the cost vs. usefullness
   * calculation involved.
   */
  public static final StyleKey COLOR_PROFILE =
          StyleKeyRegistry.getRegistry().createKey
          ("color-profile", false, true, false);
  public static final StyleKey RENDERING_INTENT =
          StyleKeyRegistry.getRegistry().createKey
          ("rendering-intent", false, true, false);

  private ColorStyleKeys() {}
}
