/**
 * Date: Jan 31, 2003
 * Time: 6:11:18 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.filter.templates;

import com.jrefinery.report.ext.barcode.Barcode;
import com.jrefinery.report.ext.barcode.BarcodeUPCA;
import com.jrefinery.report.ext.barcode.filter.templates.BarcodeEANFieldTemplate;

public class BarcodeUPCAFieldTemplate extends BarcodeEANFieldTemplate
{
  public BarcodeUPCAFieldTemplate()
  {
  }

  protected Barcode createBarcode()
  {
    return new BarcodeUPCA();
  }
}
