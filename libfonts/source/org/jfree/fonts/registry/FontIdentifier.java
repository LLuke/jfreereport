/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
package org.jfree.fonts.registry;

/**
 * A font identifier is a general handle to map Font-Metrics for a given font.
 * The same font identifier may be used by several fonts, if the fonts share
 * the same metrics (this is commonly true for TrueType fonts).
 *
 * @author Thomas Morgner
 */
public interface FontIdentifier
{
  public boolean equals(Object o);
  public int hashCode();

  /**
   * Defines, whether the font identifier represents a scalable font type.
   * Such fonts usually create one font metric object for each physical font,
   * and apply the font size afterwards.
   *
   * @return true, if the font is scalable, false otherwise
   */
  public boolean isScalable();

  /**
   * Returns the general type of this font identifier. This is for debugging,
   * not for the real world.
   *
   * @return
   */
  public FontType getFontType();
}
