/**
 * Date: Jan 31, 2003
 * Time: 6:11:18 PM
 *
 * $Id: BarcodeUPCAFieldTemplate.java,v 1.1 2003/02/25 20:58:50 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter.templates;

import org.jfree.report.ext.barcode.base.Barcode;
import org.jfree.report.ext.barcode.base.BarcodeUPCA;
import org.jfree.report.ext.barcode.base.filter.templates.BarcodeEANFieldTemplate;

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
