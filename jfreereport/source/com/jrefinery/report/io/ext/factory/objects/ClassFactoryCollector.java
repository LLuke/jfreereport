/**
 * Date: Jan 10, 2003
 * Time: 10:09:07 PM
 *
 * $Id: ClassFactoryCollector.java,v 1.4 2003/01/23 18:07:45 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Vector;

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
      if (od != null)
        return od;
    }
    ObjectDescription sod = super.getDescriptionForClass(c);
    if (sod != null)
      return sod;

    for (int i = 0; i < factories.size(); i++)
    {
      ClassFactory f = (ClassFactory) factories.get(i);
      ObjectDescription od = f.getSuperClassObjectDescription(c);
      if (od != null)
        return od;
    }
    return getSuperClassObjectDescription(c);
  }

  public Iterator getRegisteredClasses()
  {
    ArrayList list = new ArrayList();
    for (int i = 0; i < factories.size(); i++)
    {
      ClassFactory f = (ClassFactory) factories.get(i);
      Iterator enum = f.getRegisteredClasses();
      while (enum.hasNext())
      {
        list.add(enum.next());
      }
    }
    return list.iterator();
  }
}
