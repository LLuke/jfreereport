/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libfonts/
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
 * TrueTypeFontMetrics.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TrueTypeFontMetrics.java,v 1.4 2006/04/17 16:33:46 taqua Exp $
 *
 * Changes
 * -------
 *
 *
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
    return fontSize * fontMetrics.getAscent();
  }

  public double getMaxDescent()
  {
    return fontSize * fontMetrics.getDescent();
  }

  public double getMaxLeading()
  {
    return fontSize * fontMetrics.getLeading();
  }

  public double getMaxHeight()
  {
    return getMaxAscent() - getMaxDescent() - getMaxLeading();
  }

  public double getMaxCharAdvance()
  {
    return 0;
  }

  public double getCharWidth(int character)
  {
    return 0;
  }

  public double getCharWidthWithKerning(int previous, int character)
  {
    return 0;
  }
}
