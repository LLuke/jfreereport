/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.modules.output.plaintext;

import org.jfree.fonts.registry.FontMetricsFactory;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontContext;
import org.jfree.fonts.registry.EmptyFontMetrics;

/**
 * Creation-Date: 13.11.2006, 12:51:41
 *
 * @author Thomas Morgner
 */
public class PlaintextFontMetricsFactory implements FontMetricsFactory
{
  private double charWidth;
  private double charHeight;

  public PlaintextFontMetricsFactory(final double charWidth,
                                     final double charHeight)
  {
    this.charWidth = charWidth;
    this.charHeight = charHeight;
  }

  /**
   * Loads the font metrics for the font identified by the given identifier.
   *
   * @param identifier
   * @param context
   * @return
   */
  public FontMetrics createMetrics(final FontIdentifier identifier,
                                   final FontContext context)
  {
    return new EmptyFontMetrics(charHeight, charWidth);
  }
}
