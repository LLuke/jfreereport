/**
 * Date: Jan 10, 2003
 * Time: 10:09:07 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.ClassFactory;

import java.util.ArrayList;

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
