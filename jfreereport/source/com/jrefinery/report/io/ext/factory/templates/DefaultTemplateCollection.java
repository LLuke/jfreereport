/**
 * Date: Jan 11, 2003
 * Time: 3:13:18 PM
 *
 * $Id: DefaultTemplateCollection.java,v 1.3 2003/01/14 21:08:17 taqua Exp $
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
    addTemplate(new ResourceFieldTemplateDescription("resource-field"));
    addTemplate(new ResourceLabelTemplateDescription("resource-label"));
  }
}
