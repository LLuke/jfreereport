/**
 * Date: Jan 31, 2003
 * Time: 6:03:51 PM
 *
 * $Id: Barcode39FieldTemplate.java,v 1.2 2003/07/03 16:06:17 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter.templates;

import org.jfree.report.ext.barcode.base.Barcode;
import org.jfree.report.ext.barcode.base.Barcode39;

public class Barcode39FieldTemplate extends BarcodeFieldTemplate
{
  private Barcode39 barcode;

  public Barcode39FieldTemplate()
  {
    barcode = (Barcode39) getBarcodeFilter().getBarcode();
  }

  protected Barcode createBarcode()
  {
    return new Barcode39();
  }

  public boolean isExtended()
  {
    return barcode.isExtended();
  }

  public void setExtended(final boolean extended)
  {
    this.barcode.setExtended(extended);
  }

  public boolean isStartStopText()
  {
    return barcode.isStartStopText();
  }

  public void setStartStopText(final boolean startStopText)
  {
    this.barcode.setStartStopText(startStopText);
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
    final Barcode39FieldTemplate ft = (Barcode39FieldTemplate) super.clone();
    ft.barcode = (Barcode39) ft.getBarcodeFilter().getBarcode();
    return ft;
  }

}
