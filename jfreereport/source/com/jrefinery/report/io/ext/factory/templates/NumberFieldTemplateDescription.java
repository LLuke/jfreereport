/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: NumberFieldTemplateDescription.java,v 1.1 2003/01/14 21:09:12 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.NumberFieldTemplate;

public class NumberFieldTemplateDescription extends AbstractTemplateDescription
{
  public NumberFieldTemplateDescription(String name)
  {
    super(name, NumberFieldTemplate.class, false);
    setParameterDefinition("name", String.class);
    setParameterDefinition("field", String.class);
    setParameterDefinition("format", String.class);
    setParameterDefinition("nullValue", String.class);
  }

}
