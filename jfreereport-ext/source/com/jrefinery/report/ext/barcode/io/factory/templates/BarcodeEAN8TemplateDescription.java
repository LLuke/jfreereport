/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.io.factory.templates;

import com.jrefinery.report.ext.barcode.filter.templates.BarcodeEAN8FieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class BarcodeEAN8TemplateDescription extends AbstractTemplateDescription
{
  public BarcodeEAN8TemplateDescription(String name)
  {
    super(name, BarcodeEAN8FieldTemplate.class, true);
  }
}
