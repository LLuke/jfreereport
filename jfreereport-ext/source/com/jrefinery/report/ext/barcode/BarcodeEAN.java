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

import org.jfree.ui.FloatDimension;
import com.jrefinery.report.targets.FontDefinition;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/** Generates barcodes in several formats: EAN13, EAN8, UPCA, UPCE,
 * supplemental 2 and 5. The default parameters are:
 * <pre>
 *x = 0.8f;
 *font = BaseFont.createFont("Helvetica", "winansi", false);
 *size = 8;
 *baseline = size;
 *barHeight = size * 3;
 *guardBars = true;
 *codeType = EAN13;
 *code = "";
 * </pre>
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public abstract class BarcodeEAN extends Barcode
{
  /** The bar positions that are guard bars.*/
  protected static int GUARD_EMPTY[] = {};
  /** The basic bar widths.*/
  protected static byte BARS[][] =
      {
        {3, 2, 1, 1}, // 0
        {2, 2, 2, 1}, // 1
        {2, 1, 2, 2}, // 2
        {1, 4, 1, 1}, // 3
        {1, 1, 3, 2}, // 4
        {1, 2, 3, 1}, // 5
        {1, 1, 1, 4}, // 6
        {1, 3, 1, 2}, // 7
        {1, 2, 1, 3}, // 8
        {3, 1, 1, 2}  // 9
      };
  /** Marker for odd parity.*/
  protected static final int ODD = 0;
  /** Marker for even parity.*/
  protected static final int EVEN = 1;

  /**
   * Show the guard bars for barcode EAN.
   */
  private boolean guardBars;

  /** Creates new BarcodeEAN */
  public BarcodeEAN()
  {
    setGuardBars(true);
  }

  protected abstract byte[] getBars ();

  /** Gets the property to show the guard bars for barcode EAN.
   * @return value of property guardBars
   */
  public boolean isGuardBars()
  {
    return guardBars;
  }

  /** Sets the property to show the guard bars for barcode EAN.
   * @param guardBars new value of property guardBars
   */
  public void setGuardBars(boolean guardBars)
  {
    this.guardBars = guardBars;
  }

  /** Calculates the EAN parity character.
   * @param code the code
   * @return the parity character
   */
  protected static int calculateEANParity(String code)
  {
    int mul = 3;
    int total = 0;
    for (int k = code.length() - 1; k >= 0; --k)
    {
      int n = code.charAt(k) - '0';
      total += mul * n;
      mul ^= 2;
    }
    return (10 - (total % 10)) % 10;
  }

  protected abstract float getWidth ();

  /** Gets the maximum area that the barcode and the text, if
   * any, will occupy. The lower left corner is always (0, 0).
   * @return the size the barcode occupies.
   */
  public Dimension2D getBarcodeSize()
  {
    float width = getWidth();
    float height = getBarHeight();
    FontDefinition font = getFont();
    if (font != null)
    {
      float baseline = getBaseline();
      if (baseline > 0)
      {
        height += baseline - getFontDescent(font.getFont());
      }
      else
      {
        height += -baseline + font.getFontSize();
      }
    }
    return new FloatDimension(width, height);
  }

  protected boolean isTextBased ()
  {
    return false;
  }

  protected abstract int[] getGuardBarPositions ();

  protected abstract void drawCode (Graphics2D g2, float keepBarX, float textStartY);

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

    Dimension2D rect = getBarcodeSize();
    float barStartX = 0;
    float barStartY = 0;
    float textStartY = 0;

    FontDefinition font = getFont();
    if (font != null)
    {
      float baseline = getBaseline();
      if (baseline <= 0)
      {
        textStartY = getBarHeight() - baseline;
      }
      else
      {
        textStartY = -getFontDescent(font.getFont());
        barStartY = textStartY + baseline;
      }

      BarcodeSizeCalculator calc = new BarcodeSizeCalculator(getFont());
      barStartX += calc.getStringWidth("" + getCode().charAt(0), 0, 1);
    }

    byte bars[] = getBars();
    int guard[] = getGuardBarPositions();

    int imageX = (int) rect.getWidth();
    int imageY = (int) rect.getHeight();
    BufferedImage image = new BufferedImage (imageX, imageY, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = image.createGraphics();
    g2.setPaint(barColor);

    float keepBarX = barStartX;
    float guardPos = 0;

    if ((font != null) && (getBaseline() > 0) && isGuardBars())
    {
      guardPos = getBaseline() / 2;
    }

    for (int k = 0; k < bars.length; ++k)
    {
      float w = bars[k] * getMinWidth();
      if (k % 2 == 0)
      {
        if (Arrays.binarySearch(guard, k) >= 0)
        {
          Rectangle2D.Float bar = new Rectangle2D.Float(barStartX, barStartY - guardPos, w, getBarHeight() + guardPos);
          g2.fill(bar);
        }
        else
        {
          Rectangle2D.Float bar = new Rectangle2D.Float(barStartX, barStartY, w, getBarHeight());
          g2.fill(bar);
        }
      }
      barStartX += w;
    }

    if (font != null)
    {
      g2.setFont(font.getFont());
      g2.setPaint(textColor);
      drawCode(g2, keepBarX, textStartY);
    }
    g2.dispose();
    return image;
  }
}
