/**
 * Date: Jan 31, 2003
 * Time: 6:11:18 PM
 *
 * $Id: BarcodeSUPP5FieldTemplate.java,v 1.1 2003/02/25 20:58:50 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter.templates;

import org.jfree.report.ext.barcode.base.Barcode;
import org.jfree.report.ext.barcode.base.BarcodeSUPP5;
import org.jfree.report.ext.barcode.base.filter.templates.BarcodeEANFieldTemplate;

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
