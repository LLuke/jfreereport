/**
 * Date: Jan 31, 2003
 * Time: 6:03:03 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.filter.templates;

import com.jrefinery.report.ext.barcode.Barcode;
import com.jrefinery.report.ext.barcode.Barcode128Raw;
import com.jrefinery.report.ext.barcode.filter.templates.Barcode128FieldTemplate;

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
