/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: DateFieldTemplateDescription.java,v 1.1 2003/01/14 21:08:14 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.DateFieldTemplate;

public class DateFieldTemplateDescription extends AbstractTemplateDescription
{
  public DateFieldTemplateDescription(String name)
  {
    super(name, DateFieldTemplate.class, false);
    setParameterDefinition("name", String.class);
    setParameterDefinition("field", String.class);
    setParameterDefinition("format", String.class);
    setParameterDefinition("nullValue", String.class);
  }
}
