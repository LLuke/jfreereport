/**
 * Date: Jan 31, 2003
 * Time: 6:11:18 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.filter.templates;

import com.jrefinery.report.ext.barcode.Barcode;
import com.jrefinery.report.ext.barcode.BarcodeSUPP5;
import com.jrefinery.report.ext.barcode.filter.templates.BarcodeEANFieldTemplate;

public class BarcodeSUPP5FieldTemplate extends BarcodeEANFieldTemplate
{
  public BarcodeSUPP5FieldTemplate()
  {
  }

  protected Barcode createBarcode()
  {
    return new BarcodeSUPP5();
  }
}
