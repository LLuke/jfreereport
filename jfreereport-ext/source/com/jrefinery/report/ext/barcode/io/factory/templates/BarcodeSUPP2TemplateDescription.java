/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id: BarcodeSUPP2TemplateDescription.java,v 1.1 2003/02/25 20:58:57 taqua Exp $
 */
package com.jrefinery.report.ext.barcode.io.factory.templates;

import com.jrefinery.report.ext.barcode.filter.templates.Barcode128FieldTemplate;
import com.jrefinery.report.ext.barcode.filter.templates.BarcodeSUPP2FieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class BarcodeSUPP2TemplateDescription extends AbstractTemplateDescription
{
  public BarcodeSUPP2TemplateDescription(final String name)
  {
    super(name, BarcodeSUPP2FieldTemplate.class, true);
  }
}
