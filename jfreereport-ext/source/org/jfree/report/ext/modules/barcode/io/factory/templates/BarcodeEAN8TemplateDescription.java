/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id: BarcodeEAN8TemplateDescription.java,v 1.1 2003/07/08 14:21:46 taqua Exp $
 */
package org.jfree.report.ext.modules.barcode.io.factory.templates;

import org.jfree.report.ext.modules.barcode.base.filter.templates.BarcodeEAN8FieldTemplate;
import org.jfree.report.modules.parser.ext.factory.templates.AbstractTemplateDescription;

public class BarcodeEAN8TemplateDescription extends AbstractTemplateDescription
{
  public BarcodeEAN8TemplateDescription(final String name)
  {
    super(name, BarcodeEAN8FieldTemplate.class, true);
  }
}
