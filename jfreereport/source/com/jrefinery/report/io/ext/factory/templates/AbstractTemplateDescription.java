/**
 * Date: Jan 11, 2003
 * Time: 2:00:07 PM
 *
 * $Id: AbstractTemplateDescription.java,v 1.2 2003/01/13 19:01:01 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.io.ext.factory.objects.BeanObjectDescription;
import com.jrefinery.report.filter.templates.Template;

public abstract class AbstractTemplateDescription
    extends BeanObjectDescription implements TemplateDescription
{
  private String name;

  public AbstractTemplateDescription(String name, Class template)
  {
    super(template);
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
