/**
 * Date: Jan 10, 2003
 * Time: 10:09:07 PM
 *
 * $Id: ClassFactoryCollector.java,v 1.2 2003/01/13 19:00:51 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.util.ArrayList;
import java.util.Iterator;

public class ClassFactoryCollector extends ClassFactoryImpl
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
