/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id: BarcodeSUPP2TemplateDescription.java,v 1.1 2003/07/11 20:05:37 taqua Exp $
 */
package org.jfree.report.ext.modules.barcode.io.factory.templates;

import org.jfree.report.ext.modules.barcode.base.filter.templates.BarcodeSUPP2FieldTemplate;
import org.jfree.report.modules.parser.ext.factory.templates.AbstractTemplateDescription;

public class BarcodeSUPP2TemplateDescription extends AbstractTemplateDescription
{
  public BarcodeSUPP2TemplateDescription(final String name)
  {
    super(name, BarcodeSUPP2FieldTemplate.class, true);
  }
}
