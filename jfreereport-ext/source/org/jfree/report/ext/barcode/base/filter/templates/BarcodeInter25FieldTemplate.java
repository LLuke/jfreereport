/**
 * Date: Jan 31, 2003
 * Time: 5:51:48 PM
 *
 * $Id: BarcodeInter25FieldTemplate.java,v 1.2 2003/07/03 16:06:17 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter.templates;

import org.jfree.report.ext.barcode.base.Barcode;
import org.jfree.report.ext.barcode.base.BarcodeInter25;
import org.jfree.report.ext.barcode.base.filter.templates.BarcodeFieldTemplate;

public class BarcodeInter25FieldTemplate extends BarcodeFieldTemplate
{
  private BarcodeInter25 barcode;

  public BarcodeInter25FieldTemplate()
  {
    barcode = (BarcodeInter25) getBarcodeFilter().getBarcode();
  }

  protected Barcode createBarcode ()
  {
    return new BarcodeInter25();
  }

  public float getMultiplier()
  {
    return barcode.getMultiplier();
  }

  public void setMultiplier(final float multiplier)
  {
    this.barcode.setMultiplier(multiplier);
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
    final BarcodeInter25FieldTemplate ft = (BarcodeInter25FieldTemplate) super.clone();
    ft.barcode = (BarcodeInter25) ft.getBarcodeFilter().getBarcode();
    return ft;
  }
}
