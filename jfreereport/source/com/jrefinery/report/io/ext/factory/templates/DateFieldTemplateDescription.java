/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: DateFieldTemplateDescription.java,v 1.2 2003/01/13 19:01:03 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.DateFieldTemplate;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplateDescription;

public class DateFieldTemplateDescription extends AbstractTemplateDescription
{
  public DateFieldTemplateDescription(String name)
  {
    super(name, DateFieldTemplate.class);
  }
}
