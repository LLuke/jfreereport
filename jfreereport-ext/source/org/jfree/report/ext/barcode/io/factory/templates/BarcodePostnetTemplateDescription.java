/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id: BarcodePostnetTemplateDescription.java,v 1.2 2003/07/03 16:06:17 taqua Exp $
 */
package org.jfree.report.ext.barcode.io.factory.templates;

import org.jfree.report.ext.barcode.base.filter.templates.BarcodePostnetFieldTemplate;
import org.jfree.report.modules.parser.ext.factory.templates.AbstractTemplateDescription;

public class BarcodePostnetTemplateDescription extends AbstractTemplateDescription
{
  public BarcodePostnetTemplateDescription(final String name)
  {
    super(name, BarcodePostnetFieldTemplate.class, true);
  }
}
