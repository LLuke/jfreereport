/**
 * Date: Jan 31, 2003
 * Time: 5:51:48 PM
 *
 * $Id: BarcodePostnetFieldTemplate.java,v 1.1 2003/02/25 20:58:47 taqua Exp $
 */
package com.jrefinery.report.ext.barcode.filter.templates;

import com.jrefinery.report.ext.barcode.Barcode;
import com.jrefinery.report.ext.barcode.BarcodeInter25;
import com.jrefinery.report.ext.barcode.BarcodePostnet;
import com.jrefinery.report.ext.barcode.filter.templates.BarcodeFieldTemplate;

public class BarcodePostnetFieldTemplate extends BarcodeFieldTemplate
{
  private BarcodePostnet barcode;

  public BarcodePostnetFieldTemplate()
  {
    barcode = (BarcodePostnet) getBarcodeFilter().getBarcode();
  }

  protected Barcode createBarcode ()
  {
    return new BarcodePostnet();
  }

  public float getMultiplier()
  {
    return barcode.getMultiplier();
  }

  public void setMultiplier(final float multiplier)
  {
    this.barcode.setMultiplier(multiplier);
  }

  public float getBarSize()
  {
    return barcode.getBarSize();
  }

  public void setBarSize(final float barSize)
  {
    this.barcode.setBarSize(barSize);
  }

  public boolean isTypePlanet()
  {
    return barcode.isTypePlanet();
  }

  public void setTypePlanet(final boolean typePlanet)
  {
    this.barcode.setTypePlanet(typePlanet);
  }

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   *
   * @throws java.lang.CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final BarcodePostnetFieldTemplate ft = (BarcodePostnetFieldTemplate) super.clone();
    ft.barcode = (BarcodePostnet) ft.getBarcodeFilter().getBarcode();
    return ft;
  }
}
