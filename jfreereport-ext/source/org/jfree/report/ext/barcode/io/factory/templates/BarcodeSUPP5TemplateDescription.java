/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id: BarcodeSUPP5TemplateDescription.java,v 1.2 2003/07/03 16:06:17 taqua Exp $
 */
package org.jfree.report.ext.barcode.io.factory.templates;

import org.jfree.report.ext.barcode.base.filter.templates.BarcodeSUPP5FieldTemplate;
import org.jfree.report.modules.parser.ext.factory.templates.AbstractTemplateDescription;

public class BarcodeSUPP5TemplateDescription extends AbstractTemplateDescription
{
  public BarcodeSUPP5TemplateDescription(final String name)
  {
    super(name, BarcodeSUPP5FieldTemplate.class, true);
  }
}
