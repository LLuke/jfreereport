/**
 * Date: Jan 31, 2003
 * Time: 6:07:39 PM
 *
 * $Id: BarcodeEANFieldTemplate.java,v 1.2 2003/07/03 16:06:17 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter.templates;

import org.jfree.report.ext.barcode.base.BarcodeEAN;

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
