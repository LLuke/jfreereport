/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 */
package com.jrefinery.report.ext.barcode;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.ui.FloatDimension;
import com.jrefinery.report.util.Log;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/** Implements the code 39 and code 39 extended. The default parameters are:
 * <pre>
 *x = 0.8f;
 *n = 2;
 *font = BaseFont.createFont("Helvetica", "winansi", false);
 *size = 8;
 *baseline = size;
 *barHeight = size * 3;
 *textAlignment = Element.ALIGN_CENTER;
 *generateChecksum = false;
 *checksumText = false;
 *startStopText = true;
 *extended = false;
 * </pre>
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class Barcode39 extends Barcode
{
  /** The bars to generate the code.
   */
  private static byte BARS[][] =
      {
        {0, 0, 0, 1, 1, 0, 1, 0, 0},
        {1, 0, 0, 1, 0, 0, 0, 0, 1},
        {0, 0, 1, 1, 0, 0, 0, 0, 1},
        {1, 0, 1, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 1, 0, 0, 0, 1},
        {1, 0, 0, 1, 1, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 1, 0, 1},
        {1, 0, 0, 1, 0, 0, 1, 0, 0},
        {0, 0, 1, 1, 0, 0, 1, 0, 0},
        {1, 0, 0, 0, 0, 1, 0, 0, 1},
        {0, 0, 1, 0, 0, 1, 0, 0, 1},
        {1, 0, 1, 0, 0, 1, 0, 0, 0},
        {0, 0, 0, 0, 1, 1, 0, 0, 1},
        {1, 0, 0, 0, 1, 1, 0, 0, 0},
        {0, 0, 1, 0, 1, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 1, 0, 1},
        {1, 0, 0, 0, 0, 1, 1, 0, 0},
        {0, 0, 1, 0, 0, 1, 1, 0, 0},
        {0, 0, 0, 0, 1, 1, 1, 0, 0},
        {1, 0, 0, 0, 0, 0, 0, 1, 1},
        {0, 0, 1, 0, 0, 0, 0, 1, 1},
        {1, 0, 1, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 1, 0, 0, 1, 1},
        {1, 0, 0, 0, 1, 0, 0, 1, 0},
        {0, 0, 1, 0, 1, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 1, 1, 0},
        {0, 0, 1, 0, 0, 0, 1, 1, 0},
        {0, 0, 0, 0, 1, 0, 1, 1, 0},
        {1, 1, 0, 0, 0, 0, 0, 0, 1},
        {0, 1, 1, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 1, 0, 0, 0, 1},
        {1, 1, 0, 0, 1, 0, 0, 0, 0},
        {0, 1, 1, 0, 1, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 1, 0, 1},
        {1, 1, 0, 0, 0, 0, 1, 0, 0},
        {0, 1, 1, 0, 0, 0, 1, 0, 0},
        {0, 1, 0, 1, 0, 1, 0, 0, 0},
        {0, 1, 0, 1, 0, 0, 0, 1, 0},
        {0, 1, 0, 0, 0, 1, 0, 1, 0},
        {0, 0, 0, 1, 0, 1, 0, 1, 0},
        {0, 1, 0, 0, 1, 0, 1, 0, 0}
      };

  /** The index chars to <CODE>BARS</CODE>.
   */
  private static String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*";

  /** The character combinations to make the code 39 extended.
   */
  private static String EXTENDED = "%U" +
      "$A$B$C$D$E$F$G$H$I$J$K$L$M$N$O$P$Q$R$S$T$U$V$W$X$Y$Z" +
      "%A%B%C%D%E  /A/B/C/D/E/F/G/H/I/J/K/L - ./O" +
      " 0 1 2 3 4 5 6 7 8 9/Z%F%G%H%I%J%V" +
      " A B C D E F G H I J K L M N O P Q R S T U V W X Y Z" +
      "%K%L%M%N%O%W" +
      "+A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+T+U+V+W+X+Y+Z" +
      "%P%Q%R%S%T";

  /** Show the start and stop character '*' in the text for
   * the barcode 39.
   */
  private boolean startStopText;

  /** Generates extended barcode 39.
   */
  private boolean extended;

  /** The bar multiplier for wide bars or the distance between
   * bars for Postnet and Planet. (was N)
   */
  private float multiplier;

  /** Creates a new Barcode39.
   */
  public Barcode39()
  {
    setMultiplier(2);
    setGenerateChecksum(false);
    setStartStopText(true);
    setExtended(false);
  }

  /** Gets the bar multiplier for wide bars.
   * @return the bar multiplier for wide bars
   */
  public float getMultiplier()
  {
    return multiplier;
  }

  /** Sets the bar multiplier for wide bars.
   * @param n the bar multiplier for wide bars
   */
  public void setMultiplier(float n)
  {
    this.multiplier = n;
  }

  /**
   * Creates the bars.
   * @param text the text to create the bars. This text does not include the start and
   * stop characters
   * @return the bars
   */
  private static byte[] getBarsCode39(String text)
  {
    text = "*" + text + "*";
    byte bars[] = new byte[text.length() * 10 - 1];

    for (int k = 0; k < text.length(); ++k)
    {
      int idx = CHARS.indexOf(text.charAt(k));
      if (idx < 0)
      {
        throw new IllegalArgumentException("The character '" + text.charAt(k) + "' is illegal in code 39.");
      }
      System.arraycopy(BARS[idx], 0, bars, k * 10, 9);
    }
    return bars;
  }

  /** Converts the extended text into a normal, escaped text,
   * ready to generate bars.
   * @param text the extended text
   * @return the escaped text
   */
  static String getCode39Ex(String text)
  {
    StringBuffer out = new StringBuffer();
    for (int k = 0; k < text.length(); ++k)
    {
      char c = text.charAt(k);
      if (c > 127)
      {
        throw new IllegalArgumentException("The character '" + c + "' is illegal in code 39 extended.");
      }

      char c1 = EXTENDED.charAt(c * 2);
      char c2 = EXTENDED.charAt(c * 2 + 1);
      if (c1 != ' ')
      {
        out.append(c1);
      }
      out.append(c2);
    }
    return out.toString();
  }

  /** Calculates the checksum.
   * @param text the text
   * @return the checksum
   */
  private static char getChecksum(String text)
  {
    int chk = 0;
    for (int k = 0; k < text.length(); ++k)
    {
      int idx = CHARS.indexOf(text.charAt(k));
      if (idx < 0)
      {
        throw new IllegalArgumentException("The character '" + text.charAt(k) + "' is illegal in code 39.");
      }
      chk += idx;
    }
    return CHARS.charAt(chk % 43);
  }

  private float getFullWidth()
  {
    String fullCode = getCode();
    if (isExtended())
    {
      fullCode = getCode39Ex(getCode());
    }

    int len = fullCode.length() + 2;
    if (isGenerateChecksum())
    {
      len += 1;
    }

    return (len * (6 * getMinWidth() + 3 * getMinWidth() * getMultiplier()) + (len - 1) * getMinWidth());
  }

  public String getFullCode()
  {
    String fullCode = getCode();
    if (isGenerateChecksum() && isDisplayChecksumText())
    {
      fullCode += getChecksum(fullCode);
    }
    if (isStartStopText())
    {
      fullCode = "*" + fullCode + "*";
    }
    return fullCode;
  }

  /** Creates an <CODE>Image</CODE> with the barcode.
   * @param barColor the color of the bars. It can be <CODE>null</CODE>
   * @param textColor the color of the text. It can be <CODE>null</CODE>
   * @return the <CODE>Image</CODE>
   */
  public Image createImageWithBarcode(Color barColor, Color textColor)
  {
    if (barColor == null)
      throw new NullPointerException("BarColor must not be null");
    if (textColor == null)
      throw new NullPointerException("TextColor must not be null");

    float textWidth = 0;
    String fullCode = getCode();
    float fullWidth = getFullWidth();
    float barStartX = 0;
    float textStartX = 0;
    float barStartY = 0;
    float textStartY = 0;

    FontDefinition font = getFont();
    if (font != null)
    {
      fullCode = getFullCode();
      BarcodeSizeCalculator calc = new BarcodeSizeCalculator(font);
      textWidth = calc.getStringWidth(fullCode, 0, fullCode.length());

      if (getBaseline() <= 0)
      {
        // print on top of the bars ...
        // baseline is negative, reduces y.
        textStartY = font.getFontSize() + getBaseline();
        barStartY = font.getFontSize();
      }
      else
      {
        barStartY = 0;
        textStartY = getBarHeight() + getBaseline();
      }
    }

    String bCode = getCode();
    if (isExtended())
    {
      bCode = getCode39Ex(getCode());
    }
    if (isGenerateChecksum())
    {
      bCode += getChecksum(bCode);
    }

    if (getTextAlignment() == ElementAlignment.CENTER)
    {
      if (textWidth > fullWidth)
        barStartX = textWidth - fullWidth;
      else
        textStartX = fullWidth - textWidth;
    }
    else if (getTextAlignment() == ElementAlignment.RIGHT)
    {
      if (textWidth > fullWidth)
        barStartX = (textWidth - fullWidth) / 2;
      else
        textStartX = (fullWidth - textWidth) / 2;
    }

    Dimension2D size = getBarcodeSize();
    int imageX = (int) size.getWidth();
    int imageY = (int) size.getHeight();
    // docmark: now the image size is known ...
    Log.debug ("Create Image With: " + imageX + " H= " + imageY);
    // 10x zoomed for better quality...
    int scale = 2;
    BufferedImage image = new BufferedImage(imageX * scale, imageY * scale, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = image.createGraphics();
    g2.setTransform(AffineTransform.getScaleInstance(scale,scale));

    byte bars[] = getBarsCode39(bCode);
    g2.setPaint(barColor);

    for (int k = 0; k < bars.length; ++k)
    {
      float w = 0;
      if (bars[k] == 0)
      {
        w = getMinWidth();
      }
      else
      {
        w = getMinWidth() * getMultiplier();
      }
      if (k % 2 == 0)
      {
        Rectangle2D.Float rect = new Rectangle2D.Float(barStartX, barStartY, w, getBarHeight());
        g2.fill(rect);
      }
      barStartX += w;
    }

    if (font != null)
    {
      g2.setFont(font.getFont());
      g2.setPaint(textColor);
      g2.drawString(fullCode, textStartX, textStartY);
      Log.debug ("Draw String: " + fullCode + " @ (" + textStartX + ", " + textStartY+ ")");
    }
    g2.dispose();
    return image;
  }

  /** Sets the property to show the start and stop character '*' in the text for
   * the barcode 39.
   * @return value of property startStopText
   */
  public boolean isStartStopText()
  {
    return startStopText;
  }

  /** Gets the property to show the start and stop character '*' in the text for
   * the barcode 39.
   * @param startStopText new value of property startStopText
   */
  public void setStartStopText(boolean startStopText)
  {
    this.startStopText = startStopText;
  }

  /** Gets the property to generate extended barcode 39.
   * @return value of property extended.
   */
  public boolean isExtended()
  {
    return extended;
  }

  /** Sets the property to generate extended barcode 39.
   * @param extended new value of property extended
   */
  public void setExtended(boolean extended)
  {
    this.extended = extended;
  }

  /**
   * Gets the maximum area that the barcode and the text, if
   * any, will occupy.
   * @return the size the barcode occupies.
   */
  public Dimension2D getBarcodeSize()
  {
    float textWidth = 0;
    String fullCode = getCode();
    float fullWidth = getFullWidth();
    float textHeight = 0;

    FontDefinition font = getFont();
    if (font != null)
    {
      fullCode = getFullCode();
      BarcodeSizeCalculator calc = new BarcodeSizeCalculator(font);
      textWidth = calc.getStringWidth(fullCode, 0, fullCode.length());

      if (getBaseline() <= 0)
      {
        // print on top of the bars, font area is baseline position
        // + font ascent. Baseline replaces font descent ..
        textHeight = -getBaseline() + getFontAscent(font.getFont());
      }
      else
      {
        // baseline replaces font ascent ...
        textHeight = getBaseline() + getFontDescent(font.getFont());
      }
    }

    return new FloatDimension (Math.max(fullWidth, textWidth),
                               getBarHeight() + textHeight);

    // docmark: now the image size is known ...
  }
}
