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
 * -----------------------------
 * StyleKeyFactoryCollector.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.targets.style.StyleKey;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A style key factory.
 * 
 * @author Thomas Morgner
 */
public class StyleKeyFactoryCollector implements StyleKeyFactory
{
  /** Storage for the factories. */
  private ArrayList factories;

  /**
   * Creates a new factory. 
   */
  public StyleKeyFactoryCollector()
  {
    factories = new ArrayList();
  }

  /**
   * Adds a factory.
   * 
   * @param factory  the factory.
   */
  public void addFactory (StyleKeyFactory factory)
  {
    factories.add (factory);
  }

  /**
   * Returns an iterator that provides access to the factories.
   * 
   * @return The iterator.
   */
  public Iterator getFactories ()
  {
    return factories.iterator();
  }

  /**
   * Returns a style key.
   * 
   * @param name  the name.
   * 
   * @return The style key.
   */
  public StyleKey getStyleKey(String name)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      StyleKeyFactory fact = (StyleKeyFactory) factories.get(i);
      StyleKey o = fact.getStyleKey(name);
      if (o != null)
      {
        return o;
      }
    }
    return null;
  }

  /**
   * Creates an object.
   * 
   * @param k  the style key.
   * @param value  the value.
   * @param c  the class.
   * 
   * @return The object.
   */
  public Object createBasicObject(StyleKey k, String value, Class c)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      StyleKeyFactory fact = (StyleKeyFactory) factories.get(i);
      Object o = fact.createBasicObject(k, value, c);
      if (o != null)
      {
        return o;
      }
    }
    return null;
  }

  /**
   * Initialise.
   * 
   * @param parser  the parser.
   */
  public void init(Parser parser)
  {
  }

  /**
   * Returns an iterator that provides access to the registered keys.
   * 
   * @return The iterator.
   */
  public Iterator getRegisteredKeys()
  {
    ArrayList list = new ArrayList();
    for (int i = 0; i < factories.size(); i++)
    {
      StyleKeyFactory f = (StyleKeyFactory) factories.get(i);
      Iterator enum = f.getRegisteredKeys();
      while (enum.hasNext())
      {
        list.add(enum.next());
      }
    }
    return list.iterator();
  }
}
