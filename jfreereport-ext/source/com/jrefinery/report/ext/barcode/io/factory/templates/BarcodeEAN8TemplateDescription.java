/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id: BarcodeEAN8TemplateDescription.java,v 1.1 2003/02/25 20:58:55 taqua Exp $
 */
package com.jrefinery.report.ext.barcode.io.factory.templates;

import com.jrefinery.report.ext.barcode.filter.templates.BarcodeEAN8FieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class BarcodeEAN8TemplateDescription extends AbstractTemplateDescription
{
  public BarcodeEAN8TemplateDescription(final String name)
  {
    super(name, BarcodeEAN8FieldTemplate.class, true);
  }
}
