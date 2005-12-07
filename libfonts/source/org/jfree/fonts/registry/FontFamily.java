/**
 * ========================================
 * libLayout : a free Java font reading library
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
 * FontFamily.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: FontFamily.java,v 1.2 2005/11/09 21:24:12 taqua Exp $
 *
 * Changes
 * -------------------------
 * 2005-11-07 : Initial version
 */
package org.jfree.fonts.registry;

import java.io.Serializable;

/**
 * Creation-Date: 07.11.2005, 19:06:25
 *
 * @author Thomas Morgner
 */
public interface FontFamily extends Serializable
{
  /**
   * Returns the name of the font family (in english).
   *
   * @return
   */
  public String getFamilyName ();

  public String[] getAllNames ();

  /**
   * This selects the most suitable font in that family. Italics fonts are
   * preferred over oblique fonts.
   * 
   * @param bold
   * @param italics
   * @return
   */
  public FontRecord getFontRecord (final boolean bold, final boolean italics);
}
