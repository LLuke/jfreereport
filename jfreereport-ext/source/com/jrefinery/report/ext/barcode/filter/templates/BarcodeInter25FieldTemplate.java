/**
 * Date: Jan 31, 2003
 * Time: 5:51:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.filter.templates;

import com.jrefinery.report.ext.barcode.Barcode;
import com.jrefinery.report.ext.barcode.BarcodeInter25;
import com.jrefinery.report.ext.barcode.filter.templates.BarcodeFieldTemplate;

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

  public void setMultiplier(float multiplier)
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
    BarcodeInter25FieldTemplate ft = (BarcodeInter25FieldTemplate) super.clone();
    ft.barcode = (BarcodeInter25) ft.getBarcodeFilter().getBarcode();
    return ft;
  }
}
