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
 * TrueTypeFontMetrics.java
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
package org.jfree.fonts.truetype;

import org.jfree.fonts.registry.FontMetrics;


/**
 * Creation-Date: 15.12.2005, 12:01:13
 *
 * @author Thomas Morgner
 */
public class TrueTypeFontMetrics implements FontMetrics
{
  private ScalableTrueTypeFontMetrics fontMetrics;
  private double fontSize;

  public TrueTypeFontMetrics(final ScalableTrueTypeFontMetrics fontMetrics,
                             final double fontSize)
  {
    if (fontMetrics == null)
    {
      throw new NullPointerException("The font must not be null");
    }
    this.fontMetrics = fontMetrics;
    this.fontSize = fontSize;
  }

  /**
   * From the baseline to the
   *
   * @return
   */
  public double getAscent()
  {
    return fontSize * fontMetrics.getAscent();
  }

  public double getDescent()
  {
    return fontSize * fontMetrics.getDescent();
  }

  public double getLeading()
  {
    return fontSize * fontMetrics.getLeading();
  }

  public double getXHeight()
  {
    return fontSize * fontMetrics.getXHeight();
  }

  public double getOverlinePosition()
  {
    return fontSize * fontMetrics.getOverlinePosition();
  }

  public double getUnderlinePosition()
  {
    return fontSize * fontMetrics.getUnderlinePosition();
  }

  public double getStrikeThroughPosition()
  {
    return fontSize * fontMetrics.getStrikeThroughPosition();
  }

  public double getMaxAscent()
  {
    return 0;
  }

  public double getMaxDescent()
  {
    return 0;
  }

  public double getMaxLeading()
  {
    return 0;
  }

  public double getMaxCharAdvance()
  {
    return 0;
  }

  public double getCharWidth(char character)
  {
    return 0;
  }

  public double getCharWidthWithKerning(char previous, char character)
  {
    return 0;
  }
}