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
import org.jfree.ui.FloatDimension;
import com.jrefinery.report.targets.FontDefinition;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/** Implements the code interleaved 2 of 5. The text can include
 * non numeric characters that are printed but do not generate bars.
 * The default parameters are:
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
 * </pre>
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class BarcodeInter25 extends Barcode
{

  /** The bars to generate the code.
   */
  private static byte BARS[][] =
      {
        {0, 0, 1, 1, 0},
        {1, 0, 0, 0, 1},
        {0, 1, 0, 0, 1},
        {1, 1, 0, 0, 0},
        {0, 0, 1, 0, 1},
        {1, 0, 1, 0, 0},
        {0, 1, 1, 0, 0},
        {0, 0, 0, 1, 1},
        {1, 0, 0, 1, 0},
        {0, 1, 0, 1, 0}
      };

  private float multiplier;

  /** Creates new BarcodeInter25 */
  public BarcodeInter25()
  {
    setMultiplier(2);
    setGenerateChecksum(false);
    setDisplayChecksumText(false);
  }

  public float getMultiplier()
  {
    return multiplier;
  }

  public void setMultiplier(float multiplier)
  {
    this.multiplier = multiplier;
  }

  /** Deletes all the non numeric characters from <CODE>text</CODE>.
   * @param text the text
   * @return a <CODE>String</CODE> with only numeric characters
   */
  private static String keepNumbers(String text)
  {
    StringBuffer sb = new StringBuffer();
    for (int k = 0; k < text.length(); ++k)
    {
      char c = text.charAt(k);
      if (c >= '0' && c <= '9')
        sb.append(c);
    }
    return sb.toString();
  }

  /** Calculates the checksum.
   * @param text the numeric text
   * @return the checksum
   */
  private static char getChecksum(String text)
  {
    int mul = 3;
    int total = 0;
    for (int k = text.length() - 1; k >= 0; --k)
    {
      int n = text.charAt(k) - '0';
      total += mul * n;
      mul ^= 2;
    }
    return (char) (((10 - (total % 10)) % 10) + '0');
  }

  /** Creates the bars for the barcode.
   * @param text the text. It can contain non numeric characters
   * @return the barcode
   */
  private static byte[] getBarsInter25(String text)
  {
    text = keepNumbers(text);
    byte bars[] = new byte[text.length() * 5 + 7];
    int pb = 0;
    bars[pb++] = 0;
    bars[pb++] = 0;
    bars[pb++] = 0;
    bars[pb++] = 0;
    int len = text.length() / 2;
    for (int k = 0; k < len; ++k)
    {
      int c1 = text.charAt(k * 2) - '0';
      int c2 = text.charAt(k * 2 + 1) - '0';
      byte b1[] = BARS[c1];
      byte b2[] = BARS[c2];
      for (int j = 0; j < 5; ++j)
      {
        bars[pb++] = b1[j];
        bars[pb++] = b2[j];
      }
    }
    bars[pb++] = 1;
    bars[pb++] = 0;
    bars[pb++] = 0;
    return bars;
  }

  /** Gets the maximum area that the barcode and the text, if
   * any, will occupy. The lower left corner is always (0, 0).
   * @return the size the barcode occupies.
   */
  public Dimension2D getBarcodeSize()
  {
    float fontX = 0;
    float fontY = 0;
    FontDefinition font = getFont();
    if (font != null)
    {
      float baseline = getBaseline();
      if (baseline > 0)
      {
        fontY = baseline - getFontDescent(font.getFont());
      }
      else
      {
        fontY = -baseline + font.getFontSize();
      }

      String fullCode = getCode();
      if (isGenerateChecksum() && isDisplayChecksumText())
      {
        fullCode += getChecksum(fullCode);
      }
      BarcodeSizeCalculator calc = new BarcodeSizeCalculator(font);
      fontX = calc.getStringWidth(fullCode, 0, fullCode.length());
    }

    float fullWidth = Math.max(getFullWidth(), fontX);
    float fullHeight = getBarHeight() + fontY;
    return new FloatDimension(fullWidth, fullHeight);
  }

  private float getFullWidth ()
  {
    String fullCode = keepNumbers(getCode());
    int len = fullCode.length();
    if (isGenerateChecksum())
    {
      len += 1;
    }

    float fullWidth = len * (3 * getMinWidth() + 2 * getMinWidth() * getMultiplier()) +
        (6 + getMultiplier()) * getMinWidth();
    return fullWidth;
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

    String fullCode = getCode();
    float textWidth = 0;
    float barStartY = 0;
    float textStartY = 0;
    float barStartX = 0;
    float textStartX = 0;

    String bCode = keepNumbers(getCode());
    if (isGenerateChecksum())
    {
      bCode += getChecksum(bCode);
    }
    float fullWidth = getFullWidth();

    FontDefinition font = getFont();
    if (font != null)
    {
      if (isGenerateChecksum() && isDisplayChecksumText())
      {
        fullCode += getChecksum(fullCode);
      }
      BarcodeSizeCalculator calc = new BarcodeSizeCalculator(font);
      textWidth = calc.getStringWidth(fullCode, 0, fullCode.length());

      float baseline = getBaseline();
      if (baseline <= 0)
        textStartY = getBarHeight() - baseline;
      else
      {
        textStartY = -getFontDescent(font.getFont());
        barStartY = textStartY + baseline;
      }
    }

    if (getTextAlignment() == ElementAlignment.RIGHT)
    {
        if (textWidth > fullWidth)
          barStartX = textWidth - fullWidth;
        else
          textStartX = fullWidth - textWidth;
    }
    else if (getTextAlignment() == ElementAlignment.CENTER)
    {
        if (textWidth > fullWidth)
          barStartX = (textWidth - fullWidth) / 2;
        else
          textStartX = (fullWidth - textWidth) / 2;
    }

    int imageX = (int) Math.max(fullWidth, textWidth);
    int imageY = (int) (getBarHeight() + textStartY);
    BufferedImage image = new BufferedImage(imageX, imageY, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = image.createGraphics();
    g2.setPaint(barColor);

    byte bars[] = getBarsInter25(bCode);

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
      if ((k % 2) == 0)
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
    }
    g2.dispose();
    return image;
  }
}
