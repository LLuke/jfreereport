/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id: BarcodeUPCATemplateDescription.java,v 1.2 2003/07/03 16:06:17 taqua Exp $
 */
package org.jfree.report.ext.barcode.io.factory.templates;

import org.jfree.report.ext.barcode.base.filter.templates.BarcodeUPCAFieldTemplate;
import org.jfree.report.modules.parser.ext.factory.templates.AbstractTemplateDescription;

public class BarcodeUPCATemplateDescription extends AbstractTemplateDescription
{
  public BarcodeUPCATemplateDescription(final String name)
  {
    super(name, BarcodeUPCAFieldTemplate.class, true);
  }
}
