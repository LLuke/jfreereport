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

public class BarcodeUPCA extends BarcodeEAN13
{
  /** The bar positions that are guard bars.*/
  private static int GUARD_UPCA[] = {0, 2, 4, 6, 28, 30, 52, 54, 56, 58};

  public BarcodeUPCA()
  {
  }

  protected float getWidth()
  {
    float width = getMinWidth() * (11 + 12 * 7);
    final String char0String = "" + getCode().charAt(0);
    final String char11String = "" + getCode().charAt(11);
    if (getFont() != null)
    {
      final BarcodeSizeCalculator calc = new BarcodeSizeCalculator(getFont());
      width += calc.getStringWidth(char0String, 0, 1);
      width += calc.getStringWidth(char11String, 0, 1);
    }
    return width;
  }

  protected int[] getGuardBarPositions()
  {
    return GUARD_UPCA;
  }

  /** Converts an UPCA code into an UPCE code. If the code can not
   * be converted a IllegalArgumentException is thrown
   * @param text the code to convert. It must have 12 numeric characters
   * @return the 8 converted digits or <CODE>null</CODE> if the
   * code could not be converted
   */
  public static String convertUPCAtoUPCE(final String text)
  {
    if (text.length() != 12 || !(text.startsWith("0") || text.startsWith("1")))
    {
      throw new IllegalArgumentException("The UPCA code is not valid");
    }

    if (text.substring(3, 6).equals("000") ||
        text.substring(3, 6).equals("100") ||
        text.substring(3, 6).equals("200"))
    {
      if (text.substring(6, 8).equals("00"))
      {
        final StringBuffer retval = new StringBuffer();
        retval.append (text.substring(0, 1));
        retval.append (text.substring(1, 3));
        retval.append (text.substring(8, 11));
        retval.append (text.substring(3, 4));
        retval.append (text.substring(11));
        return retval.toString();
      }
    }
    else if (text.substring(4, 6).equals("00"))
    {
      if (text.substring(6, 9).equals("000"))
      {
        final StringBuffer retval = new StringBuffer();
        retval.append (text.substring(0, 1));
        retval.append (text.substring(1, 4));
        retval.append (text.substring(9, 11));
        retval.append ("3");
        retval.append (text.substring(11));
        return retval.toString();
      }
    }
    else if (text.substring(5, 6).equals("0"))
    {
      if (text.substring(6, 10).equals("0000"))
      {
        final StringBuffer retval = new StringBuffer();
        retval.append(text.substring(0, 1));
        retval.append(text.substring(1, 5));
        retval.append(text.substring(10, 11));
        retval.append("4");
        retval.append(text.substring(11));
        return retval.toString();
      }
    }
    else if (text.charAt(10) >= '5')
    {
      if (text.substring(6, 10).equals("0000"))
      {
        final StringBuffer retval = new StringBuffer();
        retval.append (text.substring(0, 1));
        retval.append (text.substring(1, 6));
        retval.append (text.substring(10, 11));
        retval.append (text.substring(11));
        return retval.toString();
      }
    }
    return null;
  }

  /** Creates the bars for the barcode EAN13 and UPCA.
   * @return the barcode
   */
  protected String getBarCode()
  {
    return ("0" + getCode());
  }

  protected void drawCode(final Graphics2D g2, final float keepBarX, final float textStartY)
  {
    final BarcodeSizeCalculator calc = new BarcodeSizeCalculator(getFont());
    final String code = getCode();
    g2.drawString(code.substring(0, 1), 0, textStartY);
    for (int k = 1; k < 11; ++k)
    {
      final String c = code.substring(k, k + 1);
      final float len = calc.getStringWidth(c, 0, 1);
      final float pX = keepBarX + TEXTPOS_EAN13[k] * getMinWidth() - len / 2;
      g2.drawString (c, pX, textStartY);
    }

    g2.drawString (code.substring(11, 12),
                   (keepBarX + getMinWidth() * (11 + 12 * 7)),
                   textStartY);
  }
}
