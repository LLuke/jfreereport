/**
 * Date: Jan 31, 2003
 * Time: 6:11:18 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.filter.templates;

import com.jrefinery.report.ext.barcode.Barcode;
import com.jrefinery.report.ext.barcode.BarcodeSUPP2;
import com.jrefinery.report.ext.barcode.filter.templates.BarcodeEANFieldTemplate;

public class BarcodeSUPP2FieldTemplate extends BarcodeEANFieldTemplate
{
  public BarcodeSUPP2FieldTemplate()
  {
  }

  protected Barcode createBarcode()
  {
    return new BarcodeSUPP2();
  }
}
