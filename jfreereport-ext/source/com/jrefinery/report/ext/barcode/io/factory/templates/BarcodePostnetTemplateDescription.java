/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.io.factory.templates;

import com.jrefinery.report.ext.barcode.filter.templates.BarcodePostnetFieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class BarcodePostnetTemplateDescription extends AbstractTemplateDescription
{
  public BarcodePostnetTemplateDescription(String name)
  {
    super(name, BarcodePostnetFieldTemplate.class, true);
  }
}
