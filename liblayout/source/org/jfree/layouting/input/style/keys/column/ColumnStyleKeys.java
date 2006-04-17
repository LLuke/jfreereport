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
 * ColumnStyleKeys.java
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
package org.jfree.layouting.input.style.keys.column;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Creation-Date: 03.12.2005, 20:48:04
 *    * column-count
    * column-width
    * column-min-width
    * column-width-policy

The second group of properties describes the space between columns:

    * column-gap
    * column-rule
    * column-rule-color
    * column-rule-style
    * column-rule-width

The third group consists of one property which make it possible an element to span several columns:

    * column-span
 *
 *
 * @author Thomas Morgner
 */
public class ColumnStyleKeys
{
  public static final StyleKey COLUMN_COUNT =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-count", false, false, false);

  public static final StyleKey COLUMN_SPACE_DISTRIBUTION =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-space-distribution", false, false, false);

  public static final StyleKey COLUMN_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-width", false, false, false);

  public static final StyleKey COLUMN_MIN_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-min-width", false, false, false);

  public static final StyleKey COLUMN_WIDTH_POLICY =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-width-policy", false, false, false);

  public static final StyleKey COLUMN_GAP =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-gap", false, false, false);

  public static final StyleKey COLUMN_RULE_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-rule-color", false, false, false);

  public static final StyleKey COLUMN_RULE_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-rule-style", false, false, false);

  public static final StyleKey COLUMN_RULE_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-rule-width", false, false, false);

  public static final StyleKey COLUMN_SPAN =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-span", false, false, false);
  
  private ColumnStyleKeys()
  {
  }
}
