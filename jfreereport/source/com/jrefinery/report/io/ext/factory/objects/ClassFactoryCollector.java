/**
 * Date: Jan 10, 2003
 * Time: 10:09:07 PM
 *
 * $Id: ClassFactoryCollector.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.ClassFactory;

import java.util.ArrayList;
import java.util.Iterator;

public class ClassFactoryCollector extends ClassFactory
{
  private ArrayList factories;

  public ClassFactoryCollector()
  {
    factories = new ArrayList();
  }

  public void addFactory (ClassFactory factory)
  {
    factories.add (factory);
  }

  public Iterator getFactories ()
  {
    return factories.iterator();
  }
  
  public ObjectDescription getDescriptionForClass(Class c)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      ClassFactory f = (ClassFactory) factories.get(i);
      ObjectDescription od = f.getDescriptionForClass(c);
      if (od != null) return od;
    }
    return super.getDescriptionForClass(c);
  }
}
