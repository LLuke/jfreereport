/**
 * Date: Jan 11, 2003
 * Time: 2:00:07 PM
 *
 * $Id: AbstractTemplateDescription.java,v 1.1 2003/01/14 21:08:02 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.Template;
import com.jrefinery.report.io.ext.factory.objects.BeanObjectDescription;

public abstract class AbstractTemplateDescription
    extends BeanObjectDescription implements TemplateDescription
{
  private String name;

  public AbstractTemplateDescription(String name, Class template, boolean init)
  {
    super(template, init);
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    if (name == null) throw new NullPointerException();
    this.name = name;
  }

  public Template createTemplate()
  {
    return (Template) createObject();
  }
}
