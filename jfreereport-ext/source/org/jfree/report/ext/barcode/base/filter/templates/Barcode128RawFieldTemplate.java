/**
 * Date: Jan 31, 2003
 * Time: 6:03:03 PM
 *
 * $Id: Barcode128RawFieldTemplate.java,v 1.1 2003/02/25 20:58:44 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter.templates;

import org.jfree.report.ext.barcode.base.Barcode;
import org.jfree.report.ext.barcode.base.Barcode128Raw;
import org.jfree.report.ext.barcode.base.filter.templates.Barcode128FieldTemplate;

public class Barcode128RawFieldTemplate extends Barcode128FieldTemplate
{
  public Barcode128RawFieldTemplate()
  {
  }

  protected Barcode createBarcode()
  {
    return new Barcode128Raw();
  }
}
