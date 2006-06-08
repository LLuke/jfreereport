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
 * AWTFontMetrics.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AWTFontMetrics.java,v 1.5 2006/04/30 09:31:13 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.awt;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.jfree.fonts.encoding.CodePointUtilities;
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
  private double lineHeight;
  private double maxCharAdvance;
  private char[] cpBuffer;
  private FontRenderContext frc;

  public AWTFontMetrics(final Font font, final FontContext context)
  {
    this.font = font;
    this.context = context;
    this.frc = new FontRenderContext
            (null, context.isAntiAliased(), context.isFractionalMetrics());


    Graphics2D graphics = getGraphics(frc);
    this.fontMetrics = graphics.getFontMetrics(font);
    final Rectangle2D rect = this.font.getMaxCharBounds(frc);
    this.lineHeight = rect.getHeight();
    this.maxCharAdvance = rect.getWidth();
    this.cpBuffer = new char[4];
  }

  protected Graphics2D getGraphics(final FontRenderContext frc)
  {
    if (graphics == null)
    {
      final BufferedImage image = new BufferedImage
              (1, 1, BufferedImage.TYPE_INT_ARGB);
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
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
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
    return fontMetrics.getMaxAscent();
  }

  public double getMaxDescent()
  {
    return fontMetrics.getMaxDescent();
  }

  public double getMaxLeading()
  {
    return lineHeight - getMaxAscent() - getMaxDescent();
  }

  public double getMaxHeight()
  {
    return lineHeight;
  }

  public double getMaxCharAdvance()
  {
    return maxCharAdvance;
  }

  public synchronized double getCharWidth(int character)
  {
    final int retval = CodePointUtilities.toChars(character, cpBuffer, 0);

    if (retval > 0)
    {
      Rectangle2D lm = font.getStringBounds(cpBuffer, 0, retval, frc);
      return lm.getWidth();
    }
    else
    {
      return 0;
    }
  }

  /**
   * This method is <b>EXPENSIVE</b>. 
   * @param previous
   * @param character
   * @return
   */
  public synchronized double getKerning(int previous, int character)
  {
    final int retvalC1 = CodePointUtilities.toChars(previous, cpBuffer, 0);
    if (retvalC1 <= 0)
    {
      return 0;
    }

    final int retvalC2 = CodePointUtilities.toChars(character, cpBuffer, retvalC1);
    if (retvalC2 > 0)
    {
      final int limit = (retvalC1 + retvalC2);
      GlyphVector gv = font.createGlyphVector(frc, new String (cpBuffer, 0, limit));
      final double totalSize = gv.getGlyphPosition(limit).getX();
      final double renderedWidth = gv.getOutline().getBounds2D().getWidth();
      return totalSize - renderedWidth;
    }
    else
    {
      return 0;
    }
  }
}
