/**
 * Date: Jan 31, 2003
 * Time: 6:20:20 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.io.factory.templates;

import com.jrefinery.report.ext.barcode.filter.templates.Barcode128RawFieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class Barcode128RawTemplateDescription extends AbstractTemplateDescription
{
  public Barcode128RawTemplateDescription(String name)
  {
    super(name, Barcode128RawFieldTemplate.class, true);
  }
}
