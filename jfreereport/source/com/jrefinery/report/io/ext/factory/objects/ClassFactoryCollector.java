/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * --------------------------
 * ClassFactoryCollector.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.objects;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class factory collector.
 * 
 * @author Thomas Morgner
 */
public class ClassFactoryCollector extends ClassFactoryImpl
{
  /** Storage for the class factories. */
  private ArrayList factories;

  /**
   * Creates a new class factory collector.
   */
  public ClassFactoryCollector()
  {
    factories = new ArrayList();
  }

  /**
   * Adds a class factory to the collection.
   * 
   * @param factory  the factory.
   */
  public void addFactory (ClassFactory factory)
  {
    factories.add (factory);
  }

  /**
   * Returns an iterator the provides access to all the factories in the collection.
   * 
   * @return The iterator.
   */
  public Iterator getFactories ()
  {
    return factories.iterator();
  }

  /**
   * Returns an object description for a class.
   * 
   * @param c  the class.
   * 
   * @return The object description.
   */  
  public ObjectDescription getDescriptionForClass(Class c)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      ClassFactory f = (ClassFactory) factories.get(i);
      ObjectDescription od = f.getDescriptionForClass(c);
      if (od != null)
      {
        return od;
      }
    }
    ObjectDescription sod = super.getDescriptionForClass(c);
    if (sod != null)
    {
      return sod;
    }
    for (int i = 0; i < factories.size(); i++)
    {
      ClassFactory f = (ClassFactory) factories.get(i);
      ObjectDescription od = f.getSuperClassObjectDescription(c);
      if (od != null)
      {
        return od;
      }
    }
    return getSuperClassObjectDescription(c);
  }

  /**
   * Returns an iterator that provices access to the registered classes.
   * 
   * @return The iterator.
   */
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
