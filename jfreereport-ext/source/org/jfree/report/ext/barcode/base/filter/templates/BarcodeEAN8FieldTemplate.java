/**
 * Date: Jan 31, 2003
 * Time: 6:10:30 PM
 *
 * $Id: BarcodeEAN8FieldTemplate.java,v 1.1 2003/02/25 20:58:45 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter.templates;

import org.jfree.report.ext.barcode.base.Barcode;
import org.jfree.report.ext.barcode.base.BarcodeEAN8;

public class BarcodeEAN8FieldTemplate extends BarcodeEANFieldTemplate
{
  public BarcodeEAN8FieldTemplate()
  {
  }

  protected Barcode createBarcode()
  {
    return new BarcodeEAN8();
  }
}
