/**
 * Date: Jan 31, 2003
 * Time: 6:03:03 PM
 *
 * $Id: Barcode128RawFieldTemplate.java,v 1.1 2003/07/11 20:05:37 taqua Exp $
 */
package org.jfree.report.ext.modules.barcode.base.filter.templates;

import org.jfree.report.ext.modules.barcode.base.content.Barcode;
import org.jfree.report.ext.modules.barcode.base.content.Barcode128Raw;

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
