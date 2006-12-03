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
package org.jfree.fonts.awt;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.jfree.fonts.encoding.CodePointUtilities;
import org.jfree.fonts.registry.BaselineInfo;
import org.jfree.fonts.registry.FontContext;
import org.jfree.fonts.registry.FontMetrics;

/**
 * Creation-Date: 16.12.2005, 21:09:39
 *
 * @author Thomas Morgner
 */
public class AWTFontMetrics implements FontMetrics
{
  private static final Graphics2D[] graphics = new Graphics2D[4];

  private Font font;
  private FontContext context;
  private java.awt.FontMetrics fontMetrics;
  private double lineHeight;
  private double maxCharAdvance;
  private char[] cpBuffer;
  private FontRenderContext frc;
  private double xheight;

  public AWTFontMetrics(final Font font, final FontContext context)
  {
    this.font = font;
    this.context = context;
    this.frc = new FontRenderContext
            (null, context.isAntiAliased(), context.isFractionalMetrics());


    Graphics2D graphics = createGraphics();
    this.fontMetrics = graphics.getFontMetrics(font);
    final Rectangle2D rect = this.font.getMaxCharBounds(frc);
    this.lineHeight = rect.getHeight();
    this.maxCharAdvance = rect.getWidth();

    GlyphVector gv = font.createGlyphVector(frc, "x");
    Rectangle2D bounds = gv.getVisualBounds();
    this.xheight = bounds.getHeight();

    this.cpBuffer = new char[4];

  }

  protected Graphics2D createGraphics()
  {
    int idx = 0;
    if (context.isAntiAliased())
    {
      idx += 1;
    }
    if (context.isFractionalMetrics())
    {
      idx += 2;
    }

    synchronized(graphics)
    {
      final Graphics2D retval = graphics[idx];
      if (retval == null)
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
        graphics[idx] = g2;
        return g2;
      }
      return retval;
    }
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
    return xheight;
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

  /**
   * Baselines are defined for scripts, not glyphs. A glyph carries script
   * information most of the time (unless it is a neutral characters or just
   * weird).
   *
   * The baseline info does not take any leading into account.
   *
   * @param c the character that is used to select the script type.
   * @return
   */
  public BaselineInfo getBaselines(int c, BaselineInfo info)
  {
    LineMetrics lm = font.getLineMetrics(((char) (c & 0xFFFF)) + "", frc);
    float[] bls = lm.getBaselineOffsets();
    int idx = lm.getBaselineIndex();

    if (info == null)
    {
      info = new BaselineInfo();
    }

    // The ascent is local - but we need the global baseline, relative to the
    // MaxAscent.
    final double maxAscent = getMaxAscent();
    final double delta = maxAscent - lm.getAscent();
    info.setBaseline(BaselineInfo.MATHEMATICAL,
            delta + maxAscent - getXHeight());
    info.setBaseline(BaselineInfo.IDEOGRAPHIC, getMaxHeight());
    info.setBaseline(BaselineInfo.MIDDLE, maxAscent / 2);

    final double base = delta + lm.getAscent();

    switch (idx)
    {
      case Font.CENTER_BASELINE:
      {
        info.setBaseline(BaselineInfo.CENTRAL, base);
        info.setBaseline(BaselineInfo.ALPHABETIC,
                base + bls[Font.ROMAN_BASELINE]);
        info.setBaseline(BaselineInfo.HANGING,
                base + bls[Font.HANGING_BASELINE]);
        info.setDominantBaseline(BaselineInfo.CENTRAL);
        break;
      }
      case Font.HANGING_BASELINE:
      {
        info.setBaseline(BaselineInfo.CENTRAL,
                base + bls[Font.CENTER_BASELINE]);
        info.setBaseline(BaselineInfo.ALPHABETIC,
                base + bls[Font.ROMAN_BASELINE]);
        info.setBaseline(BaselineInfo.HANGING, base);
        info.setDominantBaseline(BaselineInfo.HANGING);
        break;
      }
      case Font.ROMAN_BASELINE:
      {
        info.setBaseline(BaselineInfo.ALPHABETIC, base);
        info.setBaseline(BaselineInfo.CENTRAL,
                base + bls[Font.CENTER_BASELINE]);
        info.setBaseline(BaselineInfo.HANGING,
                base + bls[Font.HANGING_BASELINE]);
        info.setDominantBaseline(BaselineInfo.ALPHABETIC);
        break;
      }
    }

    return info;
  }

  public static void main(String[] args)
  {
    Font f = new Font ("dialog", Font.PLAIN, 10);
    FontRenderContext frc = new FontRenderContext
            (null, true, true);

    LineMetrics lm = f.getLineMetrics("\u0915\u092a", frc);
    System.out.println ("Ascent: " + lm.getAscent());
    System.out.println ("Leading: " + lm.getLeading());
    System.out.println ("Descent: " + lm.getDescent());

    float[] bls = lm.getBaselineOffsets();
    int idx = lm.getBaselineIndex();
    for (int i = 0; i < bls.length; i++)
    {
      float v = bls[i];
      System.out.println ("BaseLine: " + i + " " + v);
    }

    System.out.println ("Dominantbaseline: " + idx);
  }
}
