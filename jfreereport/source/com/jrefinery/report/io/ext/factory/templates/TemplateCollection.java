/**
 * Date: Jan 11, 2003
 * Time: 2:06:28 AM
 *
 * $Id: TemplateCollection.java,v 1.2 2003/01/13 19:01:12 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

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
}
