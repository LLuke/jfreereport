/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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

import java.awt.Graphics2D;

public class BarcodeSUPP2 extends BarcodeEAN
{
  /** Sequence of parities to be used with supplemental 2.*/
  private static byte PARITY2[][] =
      {
        {ODD, ODD}, // 0
        {ODD, EVEN}, // 1
        {EVEN, ODD}, // 2
        {EVEN, EVEN}   // 3
      };

  /** The total number of bars for supplemental 2.*/
  private static final int TOTALBARS_SUPP2 = 13;

  public BarcodeSUPP2()
  {
  }

  protected float getWidth()
  {
    return getMinWidth() * (6 + 2 * 7);
  }

  protected int[] getGuardBarPositions()
  {
    return GUARD_EMPTY;
  }

  /** Creates the bars for the barcode supplemental 2.
   * @return the barcode
   */
  public byte[] getBars()
  {
    final String _code = getCode();
    final int[] code = new int[2];
    for (int k = 0; k < code.length; ++k)
    {
      code[k] = _code.charAt(k) - '0';
    }
    final byte[] bars = new byte[TOTALBARS_SUPP2];
    int pb = 0;
    final int parity = (code[0] * 10 + code[1]) % 4;
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 2;
    final byte[] sequence = PARITY2[parity];
    for (int k = 0; k < sequence.length; ++k)
    {
      if (k == 1)
      {
        bars[pb++] = 1;
        bars[pb++] = 1;
      }
      final int c = code[k];
      final byte[] stripes = BARS[c];
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
    return bars;
  }

  protected void drawCode(final Graphics2D g2, final float keepBarX, final float textStartY)
  {
    final BarcodeSizeCalculator calc = new BarcodeSizeCalculator(getFont());
    final String code = getCode();

    for (int k = 0; k < code.length(); ++k)
    {
      final String c = code.substring(k, k + 1);
      final float len = calc.getStringWidth(c, 0, 1);
      final float pX = (7.5f + (9 * k)) * getMinWidth() - len / 2;
      g2.drawString (c, pX, textStartY);
    }
  }
}
