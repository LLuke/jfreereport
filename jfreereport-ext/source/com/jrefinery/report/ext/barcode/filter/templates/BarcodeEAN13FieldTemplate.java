/**
 * Date: Jan 31, 2003
 * Time: 6:09:51 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.filter.templates;

import com.jrefinery.report.ext.barcode.Barcode;
import com.jrefinery.report.ext.barcode.BarcodeEAN13;

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
