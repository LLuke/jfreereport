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
 * FontMetrics.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 15.12.2005 : Initial version
 */
package org.jfree.fonts.registry;

/**
 * Font-metrics are computed for a certain font-size and style. There are
 * no general metrics, which are assumed to be globally available.
 *
 * The use of these font metrics is application dependent. As building these
 * metrics usually is memory and CPU intensive, this must be done in an
 * application specific context. You certainly want to cache the font data. 
 *
 * @author Thomas Morgner
 */
public interface FontMetrics
{
  /**
   * From the baseline to the
   * @return
   */
  public double getAscent();
  public double getDescent();
  public double getLeading();

  /**
   * The height of the lowercase 'x'. This is used as hint, which size the
   * lowercase characters will have.
   * 
   * @return
   */
  public double getXHeight();
  public double getOverlinePosition();
  public double getUnderlinePosition();
  public double getStrikeThroughPosition();

  public double getMaxAscent();
  public double getMaxDescent();
  public double getMaxLeading();

  public double getMaxCharAdvance();

  public double getCharWidth(char character);
  public double getCharWidthWithKerning(char previous, char character);

}
