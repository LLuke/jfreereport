/**
 * Date: Jan 11, 2003
 * Time: 3:33:07 PM
 *
 * $Id: TemplateCollector.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.io.ext.factory.templates.Template;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollection;

import java.util.ArrayList;
import java.util.Iterator;

public class TemplateCollector extends TemplateCollection
{
  private ArrayList factories;

  public TemplateCollector()
  {
    factories = new ArrayList();
  }

  public void addTemplateCollection (TemplateCollection tc)
  {
    factories.add(tc);
  }

  public Iterator getFactories ()
  {
    return factories.iterator();
  }
  
  public Template getTemplate(String name)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      TemplateCollection fact = (TemplateCollection) factories.get(i);
      Template o = fact.getTemplate(name);
      if (o != null) return o;
    }
    return super.getTemplate(name);
  }
}
