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

import com.jrefinery.ui.FloatDimension;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/** Implements the Postnet and Planet barcodes. The default parameters are:
 * <pre>
 *n = 72f / 22f; // distance between bars
 *x = 0.02f * 72f; // bar width
 *barHeight = 0.125f * 72f; // height of the tall bars
 *size = 0.05f * 72f; // height of the short bars
 *codeType = POSTNET; // type of code
 * </pre>
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class BarcodePostnet extends Barcode
{

  /** The bars for each character.
   */
  private static byte BARS[][] =
      {
        {1, 1, 0, 0, 0},
        {0, 0, 0, 1, 1},
        {0, 0, 1, 0, 1},
        {0, 0, 1, 1, 0},
        {0, 1, 0, 0, 1},
        {0, 1, 0, 1, 0},
        {0, 1, 1, 0, 0},
        {1, 0, 0, 0, 1},
        {1, 0, 0, 1, 0},
        {1, 0, 1, 0, 0}
      };

  private float barSize;
  private boolean typePlanet;
  private float multiplier;

  /** Creates new BarcodePostnet */
  public BarcodePostnet()
  {
    setMultiplier(72f / 22f); // distance between bars
    setMinWidth(0.02f * 72f); // bar width
    setBarHeight(0.125f * 72f); // height of the tall bars
    barSize = 0.05f * 72f; // height of the short bars
    typePlanet = false;
  }

  public float getMultiplier()
  {
    return multiplier;
  }

  public void setMultiplier(float multiplier)
  {
    this.multiplier = multiplier;
  }

  public boolean isTypePlanet()
  {
    return typePlanet;
  }

  public void setTypePlanet(boolean typePlanet)
  {
    this.typePlanet = typePlanet;
  }

  public float getBarSize()
  {
    return barSize;
  }

  public void setBarSize(float barSize)
  {
    this.barSize = barSize;
  }

  /**
   * Creates the bars for Postnet.
   * @return the bars
   */
  public byte[] getBarsPostnet()
  {
    String text = getCode();
    int total = 0;
    for (int k = text.length() - 1; k >= 0; --k)
    {
      int n = text.charAt(k) - '0';
      total += n;
    }
    text += (char) (((10 - (total % 10)) % 10) + '0');

    byte bars[] = new byte[text.length() * 5 + 2];
    bars[0] = 1;
    bars[bars.length - 1] = 1;
    for (int k = 0; k < text.length(); ++k)
    {
      int c = text.charAt(k) - '0';
      System.arraycopy(BARS[c], 0, bars, k * 5 + 1, 5);
    }
    return bars;
  }

  /** Gets the maximum area that the barcode and the text, if
   * any, will occupy. The lower left corner is always (0, 0).
   * @return the size the barcode occupies.
   */
  public Dimension2D getBarcodeSize()
  {
    String code = getCode();
    float width = ((code.length() + 1) * 5 + 1) * getMultiplier() + getMinWidth();
    return new FloatDimension(width, getBarHeight());
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

    Dimension2D imageDim = getBarcodeSize();
    BufferedImage image = new BufferedImage((int) imageDim.getWidth(),
                                            (int) imageDim.getHeight(),
                                            BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = image.createGraphics();
    g2.setPaint(barColor);

    byte bars[] = getBarsPostnet();
    byte flip = 1;

    if (isTypePlanet())
    {
      flip = 0;
      bars[0] = 0;
      bars[bars.length - 1] = 0;
    }

    float startX = 0;
    for (int k = 0; k < bars.length; ++k)
    {
      float height = 0;
      if (bars[k] == flip)
      {
        height = getBarHeight();
      }
      else
      {
        height = getBarSize();
      }
      Rectangle2D rect = new Rectangle2D.Float(startX, 0, getMinWidth(), height);
      g2.fill(rect);
      startX += getMultiplier();
    }
    g2.dispose();
    return image;
  }

}
