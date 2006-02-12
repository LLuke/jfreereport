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
 * InternalStyleKeys.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.internal;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 *  
 *
 * @author Thomas Morgner
 */
public class InternalStyleKeys
{
  /**
   * To which JFreeReport pseudo-class does this element belong to.
   * A pseudo-class membership is defined by an expression.
   */
  public static final StyleKey PSEUDOCLASS =
          StyleKeyRegistry.getRegistry().createKey
          ("-x-liblayout-pseudoclass", false, false, true);

  /**
   * Which language does the content have? This is an ISO code like
   * 'en' maybe enriched with an country code 'en_US' and variant
   * 'en_US_native'
   */
  public static final StyleKey LANG =
          StyleKeyRegistry.getRegistry().createKey
          ("-x-liblayout-language", true, true, false);

  private InternalStyleKeys()
  {
  }
}
