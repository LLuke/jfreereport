/**
 * Date: Jan 11, 2003
 * Time: 3:13:18 PM
 *
 * $Id: DefaultTemplateCollection.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.io.ext.factory.templates.DateFieldTemplate;

public class DefaultTemplateCollection extends TemplateCollection
{
  public DefaultTemplateCollection()
  {
    addTemplate(new DateFieldTemplate("date-field"));
    addTemplate(new ImageFieldTemplate("image-field"));
    addTemplate(new ImageURLFieldTemplate("image-url-field"));
    addTemplate(new ImageURLElementTemplate("image-url-element"));
    addTemplate(new LabelTemplate("label"));
    addTemplate(new NumberFieldTemplate("number-field"));
    addTemplate(new StringFieldTemplate("string-field"));
  }
}
