/**
 * Date: Jan 31, 2003
 * Time: 6:11:18 PM
 *
 * $Id: BarcodeSUPP2FieldTemplate.java,v 1.1 2003/02/25 20:58:49 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter.templates;

import org.jfree.report.ext.barcode.base.Barcode;
import org.jfree.report.ext.barcode.base.BarcodeSUPP2;
import org.jfree.report.ext.barcode.base.filter.templates.BarcodeEANFieldTemplate;

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
