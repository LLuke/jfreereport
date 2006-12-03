/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.keys.list;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Creation-Date: 01.12.2005, 19:15:04
 *
 * @author Thomas Morgner
 */
public class ListStyleKeys
{
  public static final StyleKey LIST_STYLE_IMAGE =
          StyleKeyRegistry.getRegistry().createKey
                  ("list-style-image", false, false,
                          StyleKey.DOM_ELEMENTS | StyleKey.PSEUDO_MARKER);
  public static final StyleKey LIST_STYLE_TYPE =
          StyleKeyRegistry.getRegistry().createKey
                  ("list-style-type", false, false,
                          StyleKey.DOM_ELEMENTS | StyleKey.PSEUDO_MARKER);
  public static final StyleKey LIST_STYLE_POSITION =
          StyleKeyRegistry.getRegistry().createKey
                  ("list-style-position", false, false,
                          StyleKey.DOM_ELEMENTS | StyleKey.PSEUDO_MARKER);


  private ListStyleKeys()
  {
  }
}
