/**
 * Date: Jan 31, 2003
 * Time: 6:11:18 PM
 *
 * $Id: BarcodeSUPP5FieldTemplate.java,v 1.1 2003/07/08 14:21:46 taqua Exp $
 */
package org.jfree.report.ext.modules.barcode.base.filter.templates;

import org.jfree.report.ext.modules.barcode.base.content.Barcode;
import org.jfree.report.ext.modules.barcode.base.content.BarcodeSUPP5;
import org.jfree.report.ext.modules.barcode.base.filter.templates.BarcodeEANFieldTemplate;

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
