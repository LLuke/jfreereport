/**
 * Date: Jan 11, 2003
 * Time: 2:06:28 AM
 *
 * $Id$
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

  public void addTemplate (String name, Template template)
  {
    templates.put(name, template);
  }

  public Template getTemplate (String name)
  {
    return (Template) templates.get (name);
  }
}
