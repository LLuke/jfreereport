/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.io.factory.templates;

import com.jrefinery.report.ext.barcode.filter.templates.Barcode39FieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class Barcode39TemplateDescription extends AbstractTemplateDescription
{
  public Barcode39TemplateDescription(String name)
  {
    super(name, Barcode39FieldTemplate.class, true);
  }
}
