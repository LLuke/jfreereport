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
 * PageStyleKeys.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PageStyleKeys.java,v 1.2 2006/04/17 20:51:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.keys.page;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Creation-Date: 30.11.2005, 17:11:54
 *
 * @author Thomas Morgner
 */
public class PageStyleKeys
{
  /** The physical Page Size */
  public static final StyleKey SIZE =
          StyleKeyRegistry.getRegistry().createKey
                  ("size", false, false, StyleKey.PAGE_CONTEXT);

  /**
   * A dimension ({length}{2}) that defines how often the page is repeated
   * horizontally and vertically if the content does not fit.
   *
   * Todo: Should we allow Auto for the horizontal or vertical pages? 
   */
  public static final StyleKey HORIZONTAL_PAGE_SPAN =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-horizontal-page-span", false, false, StyleKey.PAGE_CONTEXT);

  public static final StyleKey VERTICAL_PAGE_SPAN =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-vertical-page-span", false, false, StyleKey.PAGE_CONTEXT);

  public static final StyleKey PAGE_BREAK_BEFORE =
          StyleKeyRegistry.getRegistry().createKey
                  ("page-break-before", false, true, StyleKey.BLOCK_ELEMENTS);
  public static final StyleKey PAGE_BREAK_AFTER =
          StyleKeyRegistry.getRegistry().createKey
                  ("page-break-after", false, true, StyleKey.BLOCK_ELEMENTS);
  public static final StyleKey PAGE_BREAK_INSIDE =
          StyleKeyRegistry.getRegistry().createKey
                  ("page-break-inside", false, true, StyleKey.BLOCK_ELEMENTS);
  
  public static final StyleKey PAGE =
          StyleKeyRegistry.getRegistry().createKey
                  ("page", false, true, StyleKey.BLOCK_ELEMENTS);
  public static final StyleKey PAGE_POLICY =
          StyleKeyRegistry.getRegistry().createKey
                  ("page-policy", false, false, StyleKey.COUNTERS);
  public static final StyleKey ORPHANS =
          StyleKeyRegistry.getRegistry().createKey
                  ("orphans", false, false, StyleKey.BLOCK_ELEMENTS);
  public static final StyleKey WIDOWS =
          StyleKeyRegistry.getRegistry().createKey
                  ("widows", false, false, StyleKey.BLOCK_ELEMENTS);
  public static final StyleKey IMAGE_ORIENTATION =
          StyleKeyRegistry.getRegistry().createKey
                  ("image-orientation", false, false, StyleKey.All_ELEMENTS);

  private PageStyleKeys()
  {
  }
}
