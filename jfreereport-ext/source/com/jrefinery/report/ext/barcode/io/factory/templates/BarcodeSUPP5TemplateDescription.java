/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id: BarcodeSUPP5TemplateDescription.java,v 1.1 2003/02/25 20:58:58 taqua Exp $
 */
package com.jrefinery.report.ext.barcode.io.factory.templates;

import com.jrefinery.report.ext.barcode.filter.templates.BarcodeSUPP5FieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class BarcodeSUPP5TemplateDescription extends AbstractTemplateDescription
{
  public BarcodeSUPP5TemplateDescription(final String name)
  {
    super(name, BarcodeSUPP5FieldTemplate.class, true);
  }
}
