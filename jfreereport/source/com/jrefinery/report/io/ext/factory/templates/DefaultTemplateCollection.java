/**
 * Date: Jan 11, 2003
 * Time: 3:13:18 PM
 *
 * $Id: DefaultTemplateCollection.java,v 1.4 2003/01/25 02:47:09 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;



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
