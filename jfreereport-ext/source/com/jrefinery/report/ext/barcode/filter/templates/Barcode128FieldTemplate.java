/**
 * Date: Jan 31, 2003
 * Time: 5:51:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.filter.templates;

import com.jrefinery.report.ext.barcode.Barcode128;
import com.jrefinery.report.ext.barcode.Barcode;

public class Barcode128FieldTemplate extends BarcodeFieldTemplate
{
  private Barcode128 barcode;

  public Barcode128FieldTemplate()
  {
    barcode = (Barcode128) getBarcodeFilter().getBarcode();
  }

  protected Barcode createBarcode ()
  {
    return new Barcode128();
  }

  public boolean isUccCode()
  {
    return barcode.isUccCode();
  }

  public void setUccCode(boolean uccCode)
  {
    barcode.setUccCode(uccCode);
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
    Barcode128FieldTemplate ft = (Barcode128FieldTemplate) super.clone();
    ft.barcode = (Barcode128) ft.getBarcodeFilter().getBarcode();
    return ft;
  }
}
