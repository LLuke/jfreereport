/**
 * Date: Jan 11, 2003
 * Time: 3:13:18 PM
 *
 * $Id: DefaultTemplateCollection.java,v 1.2 2003/01/13 19:01:03 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.io.ext.factory.templates.DateFieldTemplateDescription;

public class DefaultTemplateCollection extends TemplateCollection
{
  public DefaultTemplateCollection()
  {
    addTemplate(new DateFieldTemplateDescription("date-field"));
    addTemplate(new ImageFieldTemplateDescription("image-field"));
    addTemplate(new ImageURLFieldTemplateDescription("image-url-field"));
    addTemplate(new ImageURLElementTemplateDescription("image-url-element"));
    addTemplate(new LabelTemplateDescription("label"));
    addTemplate(new NumberFieldTemplateDescription("number-field"));
    addTemplate(new StringFieldTemplateDescription("string-field"));
  }
}
