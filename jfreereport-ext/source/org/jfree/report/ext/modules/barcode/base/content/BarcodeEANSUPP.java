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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import org.jfree.ui.FloatDimension;

/** This class takes 2 barcodes, an EAN/UPC and a supplemental
 * and creates a single barcode with both combined in the
 * expected layout. The UPC/EAN should have a positive text
 * baseline and the supplemental a negative one (in the supplemental
 * the text is on the top of the barcode.<p>
 * The default parameters are:
 * <pre>
 *n = 8; // horizontal distance between the two barcodes
 * </pre>
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class BarcodeEANSUPP extends Barcode
{
  /** The barcode with the EAN/UPC.
   */
  private Barcode ean;
  /** The barcode with the supplemental.
   */
  private Barcode supp;

  private float barcodeGap;

  /** Creates new combined barcode.
   * @param ean the EAN/UPC barcode
   * @param supp the supplemental barcode
   */
  public BarcodeEANSUPP(final Barcode ean, final Barcode supp)
  {
    setBarcodeGap(8); // horizontal distance between the two barcodes
    this.ean = ean;
    this.supp = supp;
  }

  public float getBarcodeGap()
  {
    return barcodeGap;
  }

  public void setBarcodeGap(final float barcodeGap)
  {
    this.barcodeGap = barcodeGap;
  }

  /** Gets the maximum area that the barcode and the text, if
   * any, will occupy. The lower left corner is always (0, 0).
   * @return the size the barcode occupies.
   */
  public Dimension2D getBarcodeSize()
  {
    final Dimension2D eanDim = ean.getBarcodeSize();
    final Dimension2D suppDim = supp.getBarcodeSize();

    return new FloatDimension((float) (eanDim.getWidth() + suppDim.getWidth() + getBarcodeGap()),
                              (float) Math.max(eanDim.getHeight(), suppDim.getHeight()));
  }


  /** Creates an <CODE>Image</CODE> with the barcode.
   * @param barColor the color of the bars. It can be <CODE>null</CODE>
   * @param textColor the color of the text. It can be <CODE>null</CODE>
   * @return the <CODE>Image</CODE>
   */
  public Image createImageWithBarcode(final Color barColor, final Color textColor)
  {
    if (barColor == null)
    {
      throw new NullPointerException("BarColor must not be null");
    }
    if (textColor == null)
    {
      throw new NullPointerException("TextColor must not be null");
    }

    if (supp.getFont() != null)
    {
      final float barHeight = (ean.getBarHeight() +
          supp.getBaseline() -
          getFontAscent(getFont().getFont()));
      supp.setBarHeight(barHeight);
    }
    else
    {
      supp.setBarHeight(ean.getBarHeight());
    }

    final Dimension2D imgSize = getBarcodeSize();

    final BufferedImage image = new BufferedImage ((int) imgSize.getWidth(),
                                             (int) imgSize.getHeight(),
                                             BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = image.createGraphics();
    final Image eanImg = ean.createImageWithBarcode(barColor, textColor);
    final Image suppImg = supp.createImageWithBarcode(barColor, textColor);
    g2.drawImage(eanImg, 0,0, null);
    g2.drawImage(suppImg, (int) (eanImg.getWidth(null) + getBarcodeGap()), 0, null);
    g2.dispose();
    return image;
  }
}
