/**
 * Date: Jan 31, 2003
 * Time: 6:09:51 PM
 *
 * $Id: BarcodeEAN13FieldTemplate.java,v 1.1 2003/07/08 14:21:46 taqua Exp $
 */
package org.jfree.report.ext.modules.barcode.base.filter.templates;

import org.jfree.report.ext.modules.barcode.base.content.Barcode;
import org.jfree.report.ext.modules.barcode.base.content.BarcodeEAN13;

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
