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

import com.jrefinery.report.ext.barcode.BarcodeEAN;
import com.jrefinery.report.ext.barcode.BarcodeSizeCalculator;

import java.awt.Graphics2D;

public class BarcodeUPCE extends BarcodeEAN
{
  /** The total number of bars for UPCE.*/
  private static final int TOTALBARS_UPCE = 9 + 6 * 4;

  /** The bar positions that are guard bars.*/
  private static int GUARD_UPCE[] = {0, 2, 28, 30, 32};

  /** The x coordinates to place the text.*/
  protected static float TEXTPOS_UPCE[] = {6.5f, 13.5f, 20.5f, 27.5f, 34.5f, 41.5f, 53.5f, 60.5f, 67.5f, 74.5f, 81.5f, 88.5f};

  /** Sequence of parities to be used with UPCE.*/
  private static byte PARITYE[][] =
      {
        {EVEN, EVEN, EVEN, ODD, ODD, ODD}, // 0
        {EVEN, EVEN, ODD, EVEN, ODD, ODD}, // 1
        {EVEN, EVEN, ODD, ODD, EVEN, ODD}, // 2
        {EVEN, EVEN, ODD, ODD, ODD, EVEN}, // 3
        {EVEN, ODD, EVEN, EVEN, ODD, ODD}, // 4
        {EVEN, ODD, ODD, EVEN, EVEN, ODD}, // 5
        {EVEN, ODD, ODD, ODD, EVEN, EVEN}, // 6
        {EVEN, ODD, EVEN, ODD, EVEN, ODD}, // 7
        {EVEN, ODD, EVEN, ODD, ODD, EVEN}, // 8
        {EVEN, ODD, ODD, EVEN, ODD, EVEN}  // 9
      };

  public BarcodeUPCE()
  {
  }

  protected float getWidth()
  {
    float width = getMinWidth() * (9 + 6 * 7);
    String char0String = "" + getCode().charAt(0);
    String char7String = "" + getCode().charAt(0);
    if (getFont() != null)
    {
      BarcodeSizeCalculator calc = new BarcodeSizeCalculator(getFont());
      width += calc.getStringWidth(char0String, 0, 1);
      width += calc.getStringWidth(char7String, 0, 1);
    }
    return width;
  }

  protected boolean isTextBased()
  {
    return true;
  }

  protected int[] getGuardBarPositions()
  {
    return GUARD_UPCE;
  }

  /** Creates the bars for the barcode UPCE.
   * @return the barcode
   */
  public byte[] getBars()
  {
    String _code = getCode();
    int code[] = new int[_code.length()];
    for (int k = 0; k < code.length; ++k)
      code[k] = _code.charAt(k) - '0';
    byte bars[] = new byte[TOTALBARS_UPCE];
    boolean flip = (code[0] != 0);
    int pb = 0;
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 1;
    byte sequence[] = PARITYE[code[code.length - 1]];
    for (int k = 1; k < code.length - 1; ++k)
    {
      int c = code[k];
      byte stripes[] = BARS[c];
      if (sequence[k - 1] == (flip ? EVEN : ODD))
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
    bars[pb++] = 1;
    return bars;
  }

  protected void drawCode(Graphics2D g2, float keepBarX, float textStartY)
  {
    BarcodeSizeCalculator calc = new BarcodeSizeCalculator(getFont());
    String code = getCode();
    g2.drawString (code.substring(0, 1), 0, textStartY);
    for (int k = 1; k < 7; ++k)
    {
      String c = code.substring(k, k + 1);
      float len = calc.getStringWidth(c, 0, 1);
      float pX = keepBarX + TEXTPOS_UPCE[k - 1] * getMinWidth() - len / 2;
      g2.drawString(c, pX, textStartY);
    }
    g2.drawString (code.substring(7, 8), (keepBarX + getMinWidth() * (9 + 6 * 7)), textStartY);
  }
}
