/**
 * Date: Jan 11, 2003
 * Time: 2:06:28 AM
 *
 * $Id: TemplateCollection.java,v 1.4 2003/01/22 19:38:27 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.Template;

import java.util.Enumeration;
import java.util.Hashtable;

public class TemplateCollection
{
  private Hashtable templates;

  public TemplateCollection()
  {
    templates = new Hashtable();
  }

  public void addTemplate (TemplateDescription template)
  {
    templates.put(template.getName(), template);
  }

  public TemplateDescription getTemplate (String name)
  {
    return (TemplateDescription) templates.get (name);
  }

  public TemplateDescription getDescription (Template template)
  {
    Enumeration enum = templates.elements();
    while (enum.hasMoreElements())
    {
      TemplateDescription td = (TemplateDescription) enum.nextElement();
      if (td.getObjectClass().equals(template.getClass()))
        return td;
    }
    return null;
  }
}
