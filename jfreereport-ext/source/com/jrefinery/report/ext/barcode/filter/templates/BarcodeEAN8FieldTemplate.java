/**
 * Date: Jan 31, 2003
 * Time: 6:10:30 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.filter.templates;

import com.jrefinery.report.ext.barcode.Barcode;
import com.jrefinery.report.ext.barcode.BarcodeEAN8;

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
