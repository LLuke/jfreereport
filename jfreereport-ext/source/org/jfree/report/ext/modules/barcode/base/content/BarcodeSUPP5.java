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

import java.awt.Graphics2D;

public class BarcodeSUPP5 extends BarcodeEAN
{
  /** The total number of bars for supplemental 5.*/
  private static final int TOTALBARS_SUPP5 = 31;

  /** Sequence of parities to be used with supplemental 2.*/
  private static byte PARITY5[][] =
      {
        {EVEN, EVEN, ODD, ODD, ODD}, // 0
        {EVEN, ODD, EVEN, ODD, ODD}, // 1
        {EVEN, ODD, ODD, EVEN, ODD}, // 2
        {EVEN, ODD, ODD, ODD, EVEN}, // 3
        {ODD, EVEN, EVEN, ODD, ODD}, // 4
        {ODD, ODD, EVEN, EVEN, ODD}, // 5
        {ODD, ODD, ODD, EVEN, EVEN}, // 6
        {ODD, EVEN, ODD, EVEN, ODD}, // 7
        {ODD, EVEN, ODD, ODD, EVEN}, // 8
        {ODD, ODD, EVEN, ODD, EVEN}  // 9
      };

  public BarcodeSUPP5()
  {
  }

  protected int[] getGuardBarPositions()
  {
    return GUARD_EMPTY;
  }

  protected float getWidth()
  {
    return getMinWidth() * (4 + 5 * 7 + 4 * 2);
  }

  /** Creates the bars for the barcode supplemental 5.
   * @return the barcode
   */
  public byte[] getBars()
  {
    final String _code = getCode();
    final int[] code = new int[5];
    for (int k = 0; k < code.length; ++k)
      code[k] = _code.charAt(k) - '0';
    final byte[] bars = new byte[TOTALBARS_SUPP5];
    int pb = 0;
    final int parity = (((code[0] + code[2] + code[4]) * 3) + ((code[1] + code[3]) * 9)) % 10;
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 2;
    final byte[] sequence = PARITY5[parity];
    for (int k = 0; k < sequence.length; ++k)
    {
      if (k != 0)
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
