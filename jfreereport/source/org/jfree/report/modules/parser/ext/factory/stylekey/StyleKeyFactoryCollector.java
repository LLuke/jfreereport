/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: StyleKeyFactoryCollector.java,v 1.5 2003/08/24 15:08:21 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext.factory.stylekey;

import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.report.style.StyleKey;
import org.jfree.xml.factory.objects.ClassFactory;

/**
 * A style key factory.
 *
 * @author Thomas Morgner
 */
public class StyleKeyFactoryCollector implements StyleKeyFactory
{
  /** Storage for the factories. */
  private final ArrayList factories;

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
  public void addFactory(final StyleKeyFactory factory)
  {
    factories.add(factory);
  }

  /**
   * Returns an iterator that provides access to the factories.
   *
   * @return The iterator.
   */
  public Iterator getFactories()
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
  public StyleKey getStyleKey(final String name)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      final StyleKeyFactory fact = (StyleKeyFactory) factories.get(i);
      final StyleKey o = fact.getStyleKey(name);
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
   * @param cf the class factory used to create the basic object.
   *
   * @return The object.
   */
  public Object createBasicObject(final StyleKey k, final String value,
                                  final Class c, final ClassFactory cf)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      final StyleKeyFactory fact = (StyleKeyFactory) factories.get(i);
      final Object o = fact.createBasicObject(k, value, c, cf);
      if (o != null)
      {
        return o;
      }
    }
    return null;
  }

  /**
   * Returns an iterator that provides access to the registered keys.
   *
   * @return The iterator.
   */
  public Iterator getRegisteredKeys()
  {
    final ArrayList list = new ArrayList();
    for (int i = 0; i < factories.size(); i++)
    {
      final StyleKeyFactory f = (StyleKeyFactory) factories.get(i);
      final Iterator enum = f.getRegisteredKeys();
      while (enum.hasNext())
      {
        list.add(enum.next());
      }
    }
    return list.iterator();
  }

  /**
   * Indicated whether an other object is equal to this one.
   * @see java.lang.Object#equals(java.lang.Object)
   *
   * @param o the other object.
   * @return true, if the object is equal, false otherwise.
   */
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof StyleKeyFactoryCollector))
    {
      return false;
    }

    final StyleKeyFactoryCollector styleKeyFactoryCollector = (StyleKeyFactoryCollector) o;

    if (!factories.equals(styleKeyFactoryCollector.factories))
    {
      return false;
    }

    return true;
  }

  /**
   * Computes an hashcode for this factory.
   * @see java.lang.Object#hashCode()
   *
   * @return the hashcode.
   */
  public int hashCode()
  {
    return factories.hashCode();
  }
}
