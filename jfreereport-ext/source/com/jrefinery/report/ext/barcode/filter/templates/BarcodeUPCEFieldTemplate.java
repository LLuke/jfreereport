/**
 * Date: Jan 31, 2003
 * Time: 6:11:18 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.filter.templates;

import com.jrefinery.report.ext.barcode.Barcode;
import com.jrefinery.report.ext.barcode.BarcodeUPCE;
import com.jrefinery.report.ext.barcode.filter.templates.BarcodeEANFieldTemplate;

public class BarcodeUPCEFieldTemplate extends BarcodeEANFieldTemplate
{
  public BarcodeUPCEFieldTemplate()
  {
  }

  protected Barcode createBarcode()
  {
    return new BarcodeUPCE();
  }
}
