/**
 * Date: Jan 31, 2003
 * Time: 6:09:51 PM
 *
 * $Id: BarcodeEAN13FieldTemplate.java,v 1.1 2003/02/25 20:58:44 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter.templates;

import org.jfree.report.ext.barcode.base.Barcode;
import org.jfree.report.ext.barcode.base.BarcodeEAN13;

public class BarcodeEAN13FieldTemplate extends BarcodeEANFieldTemplate
{
  public BarcodeEAN13FieldTemplate()
  {
  }

  protected Barcode createBarcode()
  {
    return new BarcodeEAN13();
  }
}
