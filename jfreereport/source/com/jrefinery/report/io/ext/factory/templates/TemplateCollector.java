/**
 * Date: Jan 11, 2003
 * Time: 3:33:07 PM
 *
 * $Id: TemplateCollector.java,v 1.2 2003/01/13 19:01:12 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

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
  
  public TemplateDescription getTemplate(String name)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      TemplateCollection fact = (TemplateCollection) factories.get(i);
      TemplateDescription o = fact.getTemplate(name);
      if (o != null) return o;
    }
    return super.getTemplate(name);
  }
}
