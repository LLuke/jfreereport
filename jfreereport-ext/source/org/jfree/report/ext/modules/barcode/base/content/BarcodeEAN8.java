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

public class BarcodeEAN8 extends BarcodeEAN
{
  /** The total number of bars for EAN8.*/
  private static final int TOTALBARS_EAN8 = 11 + 8 * 4;

  /** The bar positions that are guard bars.*/
  private static int GUARD_EAN8[] = {0, 2, 20, 22, 40, 42};

  /** The x coordinates to place the text.*/
  private static float TEXTPOS_EAN8[] = {6.5f, 13.5f, 20.5f, 27.5f, 39.5f, 46.5f, 53.5f, 60.5f};

  public BarcodeEAN8()
  {
  }

  protected float getWidth()
  {
    return getMinWidth() * (11 + 8 * 7);
  }

  protected int[] getGuardBarPositions()
  {
    return GUARD_EAN8;
  }

  /** Creates the bars for the barcode EAN8.
   * @return the barcode
   */
  public byte[] getBars()
  {
    final String _code = getCode();
    final int[] code = new int[_code.length()];
    for (int k = 0; k < code.length; ++k)
      code[k] = _code.charAt(k) - '0';
    final byte[] bars = new byte[TOTALBARS_EAN8];
    int pb = 0;
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 1;
    for (int k = 0; k < 4; ++k)
    {
      final int c = code[k];
      final byte[] stripes = BARS[c];
      bars[pb++] = stripes[0];
      bars[pb++] = stripes[1];
      bars[pb++] = stripes[2];
      bars[pb++] = stripes[3];
    }
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 1;
    bars[pb++] = 1;
    for (int k = 4; k < 8; ++k)
    {
      final int c = code[k];
      final byte[] stripes = BARS[c];
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

  protected void drawCode(final Graphics2D g2, final float keepBarX, final float textStartY)
  {
    final String code = getCode();
    final BarcodeSizeCalculator calc = new BarcodeSizeCalculator(getFont());
    for (int k = 0; k < 8; ++k)
    {
      final String c = code.substring(k, k + 1);
      final float len = calc.getStringWidth(c, 0, 1);
      final float pX = TEXTPOS_EAN8[k] * getMinWidth() - len / 2;
      g2.drawString(c, pX, textStartY);
    }
  }
}
