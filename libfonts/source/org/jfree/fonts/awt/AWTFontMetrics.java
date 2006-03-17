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
 * AWTFontMetrics.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: AWTFontMetrics.java,v 1.1 2006/01/27 20:38:37 taqua Exp $
 *
 * Changes
 * -------------------------
 * 16.12.2005 : Initial version
 */
package org.jfree.fonts.awt;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;

import org.jfree.fonts.registry.FontContext;
import org.jfree.fonts.registry.FontMetrics;

/**
 * Creation-Date: 16.12.2005, 21:09:39
 *
 * @author Thomas Morgner
 */
public class AWTFontMetrics implements FontMetrics
{
  private Font font;
  private FontContext context;
  private java.awt.FontMetrics fontMetrics;
  private Graphics2D graphics;

  public AWTFontMetrics(final Font font, final FontContext context)
  {
    this.font = font;
    this.context = context;
    FontRenderContext frc = new FontRenderContext
            (null, context.isAntiAliased(), context.isFractionalMetrics());


    Graphics2D graphics = getGraphics(frc);
    fontMetrics = graphics.getFontMetrics(font);
  }

  protected Graphics2D getGraphics(final FontRenderContext frc)
  {
    if (graphics == null)
    {
      final BufferedImage image = new BufferedImage
              (1,1,BufferedImage.TYPE_INT_ARGB);
      final Graphics2D g2 = image.createGraphics();
      if (context.isAntiAliased())
      {
        g2.setRenderingHint
                (RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      }
      if (context.isFractionalMetrics())
      {
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      }
      graphics = g2;
    }
    return graphics;
  }

  public Font getFont()
  {
    return font;
  }

  /**
   * From the baseline to the
   *
   * @return
   */
  public double getAscent()
  {
    return fontMetrics.getAscent();
  }

  public double getDescent()
  {
    return fontMetrics.getDescent();
  }

  public double getLeading()
  {
    return fontMetrics.getLeading();
  }

  /**
   * The height of the lowercase 'x'. This is used as hint, which size the
   * lowercase characters will have.
   *
   * @return
   */
  public double getXHeight()
  {
    return fontMetrics.getAscent() * 0.7;
  }

  public double getOverlinePosition()
  {
    return 0;
  }

  public double getUnderlinePosition()
  {
    return 0;
  }

  public double getStrikeThroughPosition()
  {
    return 0;
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

  public double getMaxHeight()
  {
    return getMaxAscent() + getMaxDescent() + getMaxLeading();
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
