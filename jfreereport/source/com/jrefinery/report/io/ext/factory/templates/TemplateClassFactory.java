/**
 * Date: Feb 12, 2003
 * Time: 6:24:30 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.io.ext.factory.objects.ClassFactoryImpl;

public class TemplateClassFactory extends ClassFactoryImpl
{
  public TemplateClassFactory()
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

  private void addTemplate (TemplateDescription td)
  {
    registerClass(td.getObjectClass(), td);
  }
}
