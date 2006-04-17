/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/libfonts/
 * Project Lead:  Thomas Morgner;
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
 * DefaultFontContext.java
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
package org.jfree.fonts.registry;

/**
 * Creation-Date: 01.02.2006, 22:10:01
 *
 * @author Thomas Morgner
 */
public class DefaultFontContext implements FontContext
{
  private double fontSize;
  private boolean antiAliased;
  private boolean fractionalMetrics;

  public DefaultFontContext(final double fontSize,
                            final boolean antiAliased,
                            final boolean fractionalMetrics)
  {
    this.fontSize = fontSize;
    this.antiAliased = antiAliased;
    this.fractionalMetrics = fractionalMetrics;
  }

  /**
   * This is controlled by the output target and the stylesheet. If the output
   * target does not support aliasing, it makes no sense to enable it and all
   * such requests are ignored.
   *
   * @return
   */
  public boolean isAntiAliased()
  {
    return antiAliased;
  }

  /**
   * This is defined by the output target. This is not controlled by the
   * stylesheet.
   *
   * @return
   */
  public boolean isFractionalMetrics()
  {
    return fractionalMetrics;
  }

  /**
   * The requested font size. A font may have a fractional font size (ie. 8.5
   * point). The font size may be influenced by the output target.
   *
   * @return the font size.
   */
  public double getFontSize()
  {
    return fontSize;
  }
}
