/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id: BarcodePostnetTemplateDescription.java,v 1.1 2003/02/25 20:58:56 taqua Exp $
 */
package com.jrefinery.report.ext.barcode.io.factory.templates;

import com.jrefinery.report.ext.barcode.filter.templates.BarcodePostnetFieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class BarcodePostnetTemplateDescription extends AbstractTemplateDescription
{
  public BarcodePostnetTemplateDescription(final String name)
  {
    super(name, BarcodePostnetFieldTemplate.class, true);
  }
}
