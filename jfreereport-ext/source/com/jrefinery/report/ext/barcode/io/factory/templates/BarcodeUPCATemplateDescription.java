/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id: BarcodeUPCATemplateDescription.java,v 1.1 2003/02/25 20:58:59 taqua Exp $
 */
package com.jrefinery.report.ext.barcode.io.factory.templates;

import com.jrefinery.report.ext.barcode.filter.templates.BarcodeUPCAFieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class BarcodeUPCATemplateDescription extends AbstractTemplateDescription
{
  public BarcodeUPCATemplateDescription(final String name)
  {
    super(name, BarcodeUPCAFieldTemplate.class, true);
  }
}
