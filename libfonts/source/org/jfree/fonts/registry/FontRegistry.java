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
 * FontRegistry.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 16.12.2005 : Initial version
 */
package org.jfree.fonts.registry;

/**
 * Creation-Date: 16.12.2005, 20:11:11
 *
 * @author Thomas Morgner
 */
public interface FontRegistry
{
  public void initialize();

  /**
   * Tries to find a font family with the given name, looking through all
   * alternative font names if neccessary.
   *
   * @param name
   * @return the font family or null, if there is no such family.
   */
  public FontFamily getFontFamily (String name);

  public String[] getRegisteredFamilies ();
  public String[] getAllRegisteredFamilies ();

  /**
   * Creates a new font metrics factory. That factory is specific to a certain
   * font registry and is not required to handle font records from foreign
   * font registries.
   *
   * A font metrics factory should never be used on its own. It should be
   * embedded into and used by a FontStorage implementation.
   *
   * @return
   */
  public FontMetricsFactory createMetricsFactory();
}
