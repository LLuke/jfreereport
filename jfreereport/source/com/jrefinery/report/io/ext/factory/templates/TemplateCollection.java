/**
 * Date: Jan 11, 2003
 * Time: 2:06:28 AM
 *
 * $Id: TemplateCollection.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.io.ext.factory.templates.Template;

import java.util.Hashtable;

public class TemplateCollection
{
  private Hashtable templates;

  public TemplateCollection()
  {
    templates = new Hashtable();
  }

  public void addTemplate (Template template)
  {
    templates.put(template.getName(), template);
  }

  public Template getTemplate (String name)
  {
    return (Template) templates.get (name);
  }
}
