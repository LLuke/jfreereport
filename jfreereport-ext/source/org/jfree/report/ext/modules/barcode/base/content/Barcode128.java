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
package org.jfree.report.ext.modules.barcode.base.content;

import org.jfree.report.ElementAlignment;
import org.jfree.report.ext.modules.barcode.base.content.Barcode;
import org.jfree.ui.FloatDimension;
import org.jfree.report.style.FontDefinition;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/** Implements the code 128 and UCC/EAN-128. Other symbologies are allowed in raw mode.<p>
 * The code types allowed are:<br>
 * <ul>
 * <li><b>CODE128</b> - plain barcode 128.
 * <li><b>CODE128_UCC</b> - support for UCC/EAN-128.
 * <li><b>CODE128_RAW</b> - raw mode. The code attribute has the actual codes from 0
 *     to 105 followed by '&#92;uffff' and the human readable text.
 * </ul>
 * The default parameters are:
 * <pre>
 * x = 0.8f;
 * font = BaseFont.createFont("Helvetica", "winansi", false);
 * size = 8;
 * baseline = size;
 * barHeight = size * 3;
 * textAlignment = Element.ALIGN_CENTER;
 * codeType = CODE128;
 * </pre>
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class Barcode128 extends Barcode
{
  /** The bars to generate the code.
   */
  private static byte BARS[][] =
      {
        {2, 1, 2, 2, 2, 2},
        {2, 2, 2, 1, 2, 2},
        {2, 2, 2, 2, 2, 1},
        {1, 2, 1, 2, 2, 3},
        {1, 2, 1, 3, 2, 2},
        {1, 3, 1, 2, 2, 2},
        {1, 2, 2, 2, 1, 3},
        {1, 2, 2, 3, 1, 2},
        {1, 3, 2, 2, 1, 2},
        {2, 2, 1, 2, 1, 3},
        {2, 2, 1, 3, 1, 2},
        {2, 3, 1, 2, 1, 2},
        {1, 1, 2, 2, 3, 2},
        {1, 2, 2, 1, 3, 2},
        {1, 2, 2, 2, 3, 1},
        {1, 1, 3, 2, 2, 2},
        {1, 2, 3, 1, 2, 2},
        {1, 2, 3, 2, 2, 1},
        {2, 2, 3, 2, 1, 1},
        {2, 2, 1, 1, 3, 2},
        {2, 2, 1, 2, 3, 1},
        {2, 1, 3, 2, 1, 2},
        {2, 2, 3, 1, 1, 2},
        {3, 1, 2, 1, 3, 1},
        {3, 1, 1, 2, 2, 2},
        {3, 2, 1, 1, 2, 2},
        {3, 2, 1, 2, 2, 1},
        {3, 1, 2, 2, 1, 2},
        {3, 2, 2, 1, 1, 2},
        {3, 2, 2, 2, 1, 1},
        {2, 1, 2, 1, 2, 3},
        {2, 1, 2, 3, 2, 1},
        {2, 3, 2, 1, 2, 1},
        {1, 1, 1, 3, 2, 3},
        {1, 3, 1, 1, 2, 3},
        {1, 3, 1, 3, 2, 1},
        {1, 1, 2, 3, 1, 3},
        {1, 3, 2, 1, 1, 3},
        {1, 3, 2, 3, 1, 1},
        {2, 1, 1, 3, 1, 3},
        {2, 3, 1, 1, 1, 3},
        {2, 3, 1, 3, 1, 1},
        {1, 1, 2, 1, 3, 3},
        {1, 1, 2, 3, 3, 1},
        {1, 3, 2, 1, 3, 1},
        {1, 1, 3, 1, 2, 3},
        {1, 1, 3, 3, 2, 1},
        {1, 3, 3, 1, 2, 1},
        {3, 1, 3, 1, 2, 1},
        {2, 1, 1, 3, 3, 1},
        {2, 3, 1, 1, 3, 1},
        {2, 1, 3, 1, 1, 3},
        {2, 1, 3, 3, 1, 1},
        {2, 1, 3, 1, 3, 1},
        {3, 1, 1, 1, 2, 3},
        {3, 1, 1, 3, 2, 1},
        {3, 3, 1, 1, 2, 1},
        {3, 1, 2, 1, 1, 3},
        {3, 1, 2, 3, 1, 1},
        {3, 3, 2, 1, 1, 1},
        {3, 1, 4, 1, 1, 1},
        {2, 2, 1, 4, 1, 1},
        {4, 3, 1, 1, 1, 1},
        {1, 1, 1, 2, 2, 4},
        {1, 1, 1, 4, 2, 2},
        {1, 2, 1, 1, 2, 4},
        {1, 2, 1, 4, 2, 1},
        {1, 4, 1, 1, 2, 2},
        {1, 4, 1, 2, 2, 1},
        {1, 1, 2, 2, 1, 4},
        {1, 1, 2, 4, 1, 2},
        {1, 2, 2, 1, 1, 4},
        {1, 2, 2, 4, 1, 1},
        {1, 4, 2, 1, 1, 2},
        {1, 4, 2, 2, 1, 1},
        {2, 4, 1, 2, 1, 1},
        {2, 2, 1, 1, 1, 4},
        {4, 1, 3, 1, 1, 1},
        {2, 4, 1, 1, 1, 2},
        {1, 3, 4, 1, 1, 1},
        {1, 1, 1, 2, 4, 2},
        {1, 2, 1, 1, 4, 2},
        {1, 2, 1, 2, 4, 1},
        {1, 1, 4, 2, 1, 2},
        {1, 2, 4, 1, 1, 2},
        {1, 2, 4, 2, 1, 1},
        {4, 1, 1, 2, 1, 2},
        {4, 2, 1, 1, 1, 2},
        {4, 2, 1, 2, 1, 1},
        {2, 1, 2, 1, 4, 1},
        {2, 1, 4, 1, 2, 1},
        {4, 1, 2, 1, 2, 1},
        {1, 1, 1, 1, 4, 3},
        {1, 1, 1, 3, 4, 1},
        {1, 3, 1, 1, 4, 1},
        {1, 1, 4, 1, 1, 3},
        {1, 1, 4, 3, 1, 1},
        {4, 1, 1, 1, 1, 3},
        {4, 1, 1, 3, 1, 1},
        {1, 1, 3, 1, 4, 1},
        {1, 1, 4, 1, 3, 1},
        {3, 1, 1, 1, 4, 1},
        {4, 1, 1, 1, 3, 1},
        {2, 1, 1, 4, 1, 2},
        {2, 1, 1, 2, 1, 4},
        {2, 1, 1, 2, 3, 2}
      };

  /** The stop bars.
   */
  private static byte BARS_STOP[] = {2, 3, 3, 1, 1, 1, 2};

  /** The charset code change.
   */
  public static final char CODE_AB_TO_C = 99;
  /** The charset code change.
   */
  public static final char CODE_AC_TO_B = 100;
  /** The charset code change.
   */
  public static final char CODE_BC_TO_A = 101;

  /** The code for UCC/EAN-128.
   */
  public static final char FNC1 = 102;
  /** The start code.
   */
  public static final char START_A = 103;
  /** The start code.
   */
  public static final char START_B = 104;
  /** The start code.
   */
  public static final char START_C = 105;

  private boolean uccCode;

  /** Creates new Barcode128 */
  public Barcode128()
  {
  }

  public boolean isUccCode()
  {
    return uccCode;
  }

  public void setUccCode(final boolean ucc)
  {
    this.uccCode = ucc;
  }

  /** Returns <CODE>true</CODE> if the next <CODE>numDigits</CODE>
   * starting from index <CODE>textIndex</CODE> are numeric.
   * @param text the text to check
   * @param textIndex where to check from
   * @param numDigits the number of digits to check
   * @return the check result
   */
  private static boolean isNextCharsDigits(final String text, int textIndex, int numDigits)
  {
    if (textIndex + numDigits > text.length())
      return false;
    while (numDigits-- > 0)
    {
      final char c = text.charAt(textIndex++);
      if (c < '0' || c > '9')
        return false;
    }
    return true;
  }

  /**
   * Packs the digits for charset C. It assumes that all the parameters
   * are valid.
   * @param text the text to pack
   * @param textIndex where to pack from
   * @param numDigits the number of digits to pack. It is always an even number
   * @return the packed digits, two digits per character
   */
  private static String getPackedRawDigits(final String text, int textIndex, int numDigits)
  {
    final StringBuffer out = new StringBuffer();
    while (numDigits > 0)
    {
      numDigits -= 2;
      final int c1 = text.charAt(textIndex++) - '0';
      final int c2 = text.charAt(textIndex++) - '0';
      out.append((char) (c1 * 10 + c2));
    }
    return out.toString();
  }

  /** Converts the human readable text to the characters needed to
   * create a barcode. Some optimization is done to get the shortest code.
   * @param text the text to convert
   * @param ucc <CODE>true</CODE> if it is an UCC/EAN-128. In this case
   * the character FNC1 is added
   * @return the code ready to be fed to getBarsCode128Raw()
   */
  public static String getRawText(final String text, final boolean ucc)
  {
    final StringBuffer out = new StringBuffer();
    final int tLen = text.length();
    if (tLen == 0)
    {
      out.append(START_B);
      if (ucc)
      {
        out.append(FNC1);
      }
      return out.toString();
    }

    // check that the bar code is valid.
    int character = 0;
    for (int i = 0; i < tLen; i++)
    {
      character = text.charAt(character);
      if (character > 127)
      {
        throw new IllegalArgumentException("There are illegal characters for barcode 128 in '" + text + "'.");
      }
    }

    character = text.charAt(0);
    char nextToken = START_B;
    int index = 0;
    if (isNextCharsDigits(text, index, 2))
    {
      // starts with 2 digits ..
      nextToken = START_C;
      out.append(nextToken);
      if (ucc)
      {
        out.append(FNC1);
      }
      out.append(getPackedRawDigits(text, index, 2));
      index = 2;
    }
    else if (character < ' ')
    {
      // the character is a ascii control character (0x00 - 0x1f) ..
      nextToken = START_A;
      out.append(nextToken);
      if (ucc)
      {
        out.append(FNC1);
      }
      out.append((char) (character + 64));
      index = 1;
    }
    else
    {
      out.append(nextToken);
      if (ucc)
      {
        out.append(FNC1);
      }
      out.append((char) (character - ' '));
      index = 1;
    }

    while (index < tLen)
    {
      switch (nextToken)
      {
        case START_A:
          {
            if (isNextCharsDigits(text, index, 4))
            {
              nextToken = START_C;
              out.append(CODE_AB_TO_C);
              out.append(getPackedRawDigits(text, index, 4));
              index += 4;
            }
            else
            {
              character = text.charAt(index);
              index += 1;
              if (character > '_')
              {
                nextToken = START_B;
                out.append(CODE_AC_TO_B);
                out.append((char) (character - ' '));
              }
              else if (character < ' ')
              {
                out.append((char) (character + 64));
              }
              else
              {
                out.append((char) (character - ' '));
              }
            }
          }
          break;
        case START_B:
          {
            if (isNextCharsDigits(text, index, 4))
            {
              nextToken = START_C;
              out.append(CODE_AB_TO_C);
              out.append(getPackedRawDigits(text, index, 4));
              index += 4;
            }
            else
            {
              character = text.charAt(index++);
              if (character < ' ')
              {
                nextToken = START_A;
                out.append(CODE_BC_TO_A);
                out.append((char) (character + 64));
              }
              else
              {
                out.append((char) (character - ' '));
              }
            }
          }
          break;
        case START_C:
          {
            if (isNextCharsDigits(text, index, 2))
            {
              out.append(getPackedRawDigits(text, index, 2));
              index += 2;
            }
            else
            {
              character = text.charAt(index++);
              if (character < ' ')
              {
                nextToken = START_A;
                out.append(CODE_BC_TO_A);
                out.append((char) (character + 64));
              }
              else
              {
                nextToken = START_B;
                out.append(CODE_AC_TO_B);
                out.append((char) (character - ' '));
              }
            }
          }
          break;
      }
    }
    return out.toString();
  }

  /**
   * Generates the bars. The input has the actual barcodes, not
   * the human readable text.
   * @param text the barcode
   * @return the bars
   */
  public static byte[] getBarsCode128Raw(final String text)
  {
    final StringBuffer b = new StringBuffer();
    final int idx = text.indexOf('\uffff');
    if (idx >= 0)
    {
      b.append(text.substring(0, idx));
    }
    else
    {
      b.append(text);
    }

    // Calculate the checksum
    int chkSum = b.charAt(0);
    for (int k = 1; k < b.length(); ++k)
    {
      chkSum += k * b.charAt(k);
    }
    chkSum = chkSum % 103;
    b.append((char) chkSum);


    final byte[] bars = new byte[(b.length() + 1) * 6 + 7];
    int k;
    for (k = 0; k < b.length(); ++k)
    {
      System.arraycopy(BARS[b.charAt(k)], 0, bars, k * 6, 6);
    }
    System.arraycopy(BARS_STOP, 0, bars, k * 6, 7);
    return bars;
  }

  public String getStrippedCode()
  {
    return getCode();
  }

  public String getRawText()
  {
    return getRawText(getCode(), isUccCode());
  }

  /** Gets the maximum area that the barcode and the text, if
   * any, will occupy. The lower left corner is always (0, 0).
   * @return the size the barcode occupies.
   */
  public Dimension2D getBarcodeSize()
  {
    float fontX = 0;
    float fontY = 0;

    final FontDefinition font = getFont();
    if (font != null)
    {
      final float baseline = getBaseline();
      if (baseline > 0)
      {
        fontY = baseline - getFontDescent(font.getFont());
      }
      else
      {
        fontY = -baseline + font.getFontSize();
      }
      final String fullCode = getStrippedCode();
      final BarcodeSizeCalculator calc = new BarcodeSizeCalculator(font);
      fontX = calc.getStringWidth(fullCode, 0, fullCode.length());
    }

    final float fullWidth = Math.max(getFullWidth(), fontX);
    final float fullHeight = getBarHeight() + fontY;
    return new FloatDimension(fullWidth, fullHeight);
  }

  private float getFullWidth()
  {
    final String rawText = getRawText();
    final int len = rawText.length();
    final float fullWidth = (len + 2) * 11 * getMinWidth() + 2 * getMinWidth();
    return fullWidth;
  }

  /** Creates an <CODE>Image</CODE> with the barcode.
   * @param barColor the color of the bars. It can be <CODE>null</CODE>
   * @param textColor the color of the text. It can be <CODE>null</CODE>
   * @return the <CODE>Image</CODE>
   */
  public Image createImageWithBarcode(final Color barColor, final Color textColor)
  {
    if (barColor == null)
      throw new NullPointerException("BarColor must not be null");
    if (textColor == null)
      throw new NullPointerException("TextColor must not be null");

    final String fullCode = getStrippedCode();
    final String bCode = getRawText();

//    int len = bCode.length();
    final float fullWidth = getFullWidth();
    float barStartX = 0;
    float barStartY = 0;
    float textStartX = 0;
    float textStartY = 0;

    float textWidth = 0;
    final FontDefinition font = getFont();
    if (font != null)
    {
      final BarcodeSizeCalculator calc = new BarcodeSizeCalculator(font);
      textWidth = calc.getStringWidth(fullCode, 0, fullCode.length());

      final float baseline = getBaseline();
      if (baseline > 0)
      {
        textStartY = baseline - getFontDescent(font.getFont());
      }
      else
      {
        textStartY = -baseline + font.getFontSize();
        barStartY = textStartY + getBaseline();
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

    final int imageX = (int) Math.max (fullWidth, textWidth);
    final int imageY = (int) (getBarHeight() + textStartY);
    final BufferedImage image = new BufferedImage (imageX, imageY, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = image.createGraphics();

    final byte[] bars = getBarsCode128Raw(bCode);
    g2.setPaint(barColor);

    for (int k = 0; k < bars.length; ++k)
    {
      final float w = bars[k] * getMinWidth();
      // print every other bar ..
      if ((k % 2) == 0)
      {
        final Rectangle2D.Float rect = new Rectangle2D.Float(barStartX, barStartY, w, getBarHeight());
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
