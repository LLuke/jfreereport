/**
 * Date: Jan 31, 2003
 * Time: 5:51:48 PM
 *
 * $Id: BarcodePostnetFieldTemplate.java,v 1.2 2003/07/03 16:06:17 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter.templates;

import org.jfree.report.ext.barcode.base.Barcode;
import org.jfree.report.ext.barcode.base.BarcodeInter25;
import org.jfree.report.ext.barcode.base.BarcodePostnet;
import org.jfree.report.ext.barcode.base.filter.templates.BarcodeFieldTemplate;

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
