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

import java.awt.Graphics2D;

public class BarcodeEAN13 extends BarcodeEAN
{
  /** The bar positions that are guard bars.*/
  private static int GUARD_EAN13[] = {0, 2, 28, 30, 56, 58};

  /** Sequence of parities to be used with EAN13.*/
  private static byte PARITY13[][] =
      {
        {ODD, ODD, ODD, ODD, ODD, ODD}, // 0
        {ODD, ODD, EVEN, ODD, EVEN, EVEN}, // 1
        {ODD, ODD, EVEN, EVEN, ODD, EVEN}, // 2
        {ODD, ODD, EVEN, EVEN, EVEN, ODD}, // 3
        {ODD, EVEN, ODD, ODD, EVEN, EVEN}, // 4
        {ODD, EVEN, EVEN, ODD, ODD, EVEN}, // 5
        {ODD, EVEN, EVEN, EVEN, ODD, ODD}, // 6
        {ODD, EVEN, ODD, EVEN, ODD, EVEN}, // 7
        {ODD, EVEN, ODD, EVEN, EVEN, ODD}, // 8
        {ODD, EVEN, EVEN, ODD, EVEN, ODD}   // 9
      };

  /** The total number of bars for EAN13.*/
  private static final int TOTALBARS_EAN13 = 11 + 12 * 4;

  /** The x coordinates to place the text.*/
  protected static float TEXTPOS_EAN13[] = {6.5f, 13.5f, 20.5f, 27.5f, 34.5f, 41.5f, 53.5f, 60.5f, 67.5f, 74.5f, 81.5f, 88.5f};

  public BarcodeEAN13()
  {
  }

  protected boolean isTextBased()
  {
    return true;
  }

  protected int[] getGuardBarPositions()
  {
    return GUARD_EAN13;
  }

  protected float getWidth()
  {
    float width = getMinWidth() * (11 + 12 * 7);
    String char0String = "" + getCode().charAt(0);
    if (getFont() != null)
    {
      BarcodeSizeCalculator calc = new BarcodeSizeCalculator(getFont());
      width += calc.getStringWidth(char0String, 0, 1);
    }
    return width;
  }

  protected String getBarCode()
  {
    return getCode();
  }

  /** Creates the bars for the barcode EAN13 and UPCA.
   * @return the barcode
   */
  public byte[] getBars()
  {
    String _code = getBarCode();
    int code[] = new int[_code.length()];

    for (int k = 0; k < code.length; ++k)
    {
      code[k] = _code.charAt(k) - '0';
    }

    byte bars[] = new byte[TOTALBARS_EAN13];
    int pb = 0;
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 1;
    byte sequence[] = PARITY13[code[0]];
    for (int k = 0; k < sequence.length; ++k)
    {
      int c = code[k + 1];
      byte stripes[] = BARS[c];
      if (sequence[k] == ODD)
      {
        bars[pb++] = stripes[0];
        bars[pb++] = stripes[1];
        bars[pb++] = stripes[2];
        bars[pb++] = stripes[3];
      }
      else
      {
        bars[pb++] = stripes[3];
        bars[pb++] = stripes[2];
        bars[pb++] = stripes[1];
        bars[pb++] = stripes[0];
      }
    }
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 1;
    for (int k = 7; k < 13; ++k)
    {
      int c = code[k];
      byte stripes[] = BARS[c];
      bars[pb++] = stripes[0];
      bars[pb++] = stripes[1];
      bars[pb++] = stripes[2];
      bars[pb++] = stripes[3];
    }
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 1;
    return bars;
  }

  protected void drawCode (Graphics2D g2, float keepBarX, float textStartY)
  {
    BarcodeSizeCalculator calc = new BarcodeSizeCalculator(getFont());
    g2.drawString(getCode().substring(0, 1), 0, textStartY);
    for (int k = 1; k < 13; ++k)
    {
      String c = getCode().substring(k, k + 1);
      float len = calc.getStringWidth(c, 0, 1);
      float pX = keepBarX + TEXTPOS_EAN13[k - 1] * getMinWidth() - len / 2;
      g2.drawString(c, pX, textStartY);
    }
  }
}
