/**
 * Date: Jan 11, 2003
 * Time: 3:13:18 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.io.ext.factory.templates.DateFieldTemplate;

public class DefaultTemplateCollection extends TemplateCollection
{
  public DefaultTemplateCollection()
  {
    addTemplate("date-field", new DateFieldTemplate());
    addTemplate("image-field", new ImageFieldTemplate());
    addTemplate("image-url-field", new ImageURLFieldTemplate());
    addTemplate("image-url-element", new ImageURLElementTemplate());
    addTemplate("label", new LabelTemplate());
    addTemplate("number-field", new NumberFieldTemplate());
    addTemplate("string-field", new StringFieldTemplate());
  }
}
