/**
 * Date: Jan 31, 2003
 * Time: 6:07:39 PM
 *
 * $Id: BarcodeEANFieldTemplate.java,v 1.1 2003/02/25 20:58:45 taqua Exp $
 */
package com.jrefinery.report.ext.barcode.filter.templates;

import com.jrefinery.report.ext.barcode.BarcodeEAN;

public abstract class BarcodeEANFieldTemplate extends BarcodeFieldTemplate
{
  private BarcodeEAN barcode;

  public BarcodeEANFieldTemplate()
  {
    barcode = (BarcodeEAN) getBarcodeFilter().getBarcode();
  }

  public boolean isGuardBars()
  {
    return barcode.isGuardBars();
  }

  public void setGuardBars(final boolean guardBars)
  {
    this.barcode.setGuardBars(guardBars);
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
    final BarcodeEANFieldTemplate ft = (BarcodeEANFieldTemplate) super.clone();
    ft.barcode = (BarcodeEAN) ft.getBarcodeFilter().getBarcode();
    return ft;
  }
}
