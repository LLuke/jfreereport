/**
 * Date: Jan 31, 2003
 * Time: 5:51:48 PM
 *
 * $Id$
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

  public void setMultiplier(float multiplier)
  {
    this.barcode.setMultiplier(multiplier);
  }

  public float getBarSize()
  {
    return barcode.getBarSize();
  }

  public void setBarSize(float barSize)
  {
    this.barcode.setBarSize(barSize);
  }

  public boolean isTypePlanet()
  {
    return barcode.isTypePlanet();
  }

  public void setTypePlanet(boolean typePlanet)
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
    BarcodePostnetFieldTemplate ft = (BarcodePostnetFieldTemplate) super.clone();
    ft.barcode = (BarcodePostnet) ft.getBarcodeFilter().getBarcode();
    return ft;
  }
}
