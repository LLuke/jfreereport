/**
 * Date: Jan 31, 2003
 * Time: 5:51:48 PM
 *
 * $Id: Barcode128FieldTemplate.java,v 1.2 2003/07/03 16:06:17 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter.templates;

import org.jfree.report.ext.barcode.base.Barcode128;
import org.jfree.report.ext.barcode.base.Barcode;

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

  public void setUccCode(final boolean uccCode)
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
    final Barcode128FieldTemplate ft = (Barcode128FieldTemplate) super.clone();
    ft.barcode = (Barcode128) ft.getBarcodeFilter().getBarcode();
    return ft;
  }
}
