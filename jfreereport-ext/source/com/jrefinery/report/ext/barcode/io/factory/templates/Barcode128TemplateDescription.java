/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id: Barcode128TemplateDescription.java,v 1.1 2003/02/25 20:58:51 taqua Exp $
 */
package com.jrefinery.report.ext.barcode.io.factory.templates;

import com.jrefinery.report.ext.barcode.filter.templates.Barcode128FieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class Barcode128TemplateDescription extends AbstractTemplateDescription
{
  public Barcode128TemplateDescription(final String name)
  {
    super(name, Barcode128FieldTemplate.class, true);
  }
}
