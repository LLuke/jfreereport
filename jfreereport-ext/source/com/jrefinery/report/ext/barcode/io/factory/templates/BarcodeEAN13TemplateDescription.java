/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.io.factory.templates;

import com.jrefinery.report.ext.barcode.filter.templates.BarcodeEAN13FieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class BarcodeEAN13TemplateDescription extends AbstractTemplateDescription
{
  public BarcodeEAN13TemplateDescription(String name)
  {
    super(name, BarcodeEAN13FieldTemplate.class, true);
  }
}
