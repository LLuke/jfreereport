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
 * ----------------------------
 * AbstractStyleKeyFactory.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractStyleKeyFactory.java,v 1.5 2003/08/20 17:24:35 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext.factory.stylekey;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;

import org.jfree.report.style.StyleKey;
import org.jfree.report.util.Log;
import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.factory.objects.ObjectDescription;

/**
 * An abstract class for implementing the {@link StyleKeyFactory} interface.
 *
 * @author Thomas Morgner.
 */
public abstract class AbstractStyleKeyFactory implements StyleKeyFactory
{
  /** the parser configuration property name for this factory. */
  public static final String OBJECT_FACTORY_TAG = "object-factory";

  /** Storage for the keys. */
  private HashMap knownKeys;

  /**
   * Creates a new factory.
   */
  public AbstractStyleKeyFactory()
  {
    knownKeys = new HashMap();
  }

  /**
   * Registers a key.
   *
   * @param key  the key.
   */
  public void addKey(final StyleKey key)
  {
    knownKeys.put(key.getName(), key);
  }

  /**
   * Returns the key with the given name.
   *
   * @param name  the name.
   *
   * @return The key.
   */
  public StyleKey getStyleKey(final String name)
  {
    return (StyleKey) knownKeys.get(name);
  }

  /**
   * Creates an object.
   *
   * @param k  the style key.
   * @param value  the value.
   * @param c  the class.
   * @param fc the class factory used to create the basic object.
   *
   * @return The object.
   */
  public Object createBasicObject(final StyleKey k, final String value, 
                                  final Class c, final ClassFactory fc)
  {
    if (k == null)
    {
      // no such key registered ...
      return null;
    }

    if (c == null)
    {
      throw new NullPointerException();
    }

    if (fc == null)
    {
      throw new NullPointerException("Class " + getClass());
    }

    ObjectDescription od = fc.getDescriptionForClass(c);
    if (od == null)
    {
      od = fc.getSuperClassObjectDescription(c, null);
      if (od == null)
      {
        return null;
      }
    }
    od.setParameter("value", value);
    return od.createObject();
  }

  /**
   * Loads all public static stylekeys which are declared in the given class.
   *
   * @param c the class from where to load the stylekeys.
   * @throws SecurityException if the current security settings deny class access.
   */
  protected void loadFromClass (Class c)
  {
    Field[] fields = c.getFields();
    for (int i = 0; i < fields.length; i++)
    {
      Field f = fields[i];
      if (StyleKey.class.isAssignableFrom(f.getType()) == false)
      {
        // is no instance of stylekey...
        continue;
      }

      if (Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers()))
      {
        try
        {
          addKey((StyleKey) f.get(null));
        }
        catch (IllegalAccessException ex)
        {
          Log.warn ("Unexpected Exception while loading stylekeys", ex);
        }
      }
    }
  }

  /**
   * Returns an iterator that provides access to the registered keys.
   *
   * @return The iterator.
   */
  public Iterator getRegisteredKeys()
  {
    return knownKeys.keySet().iterator();
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
    if (!(o instanceof AbstractStyleKeyFactory))
    { 
      return false;
    }

    final AbstractStyleKeyFactory abstractStyleKeyFactory = (AbstractStyleKeyFactory) o;

    if (!knownKeys.equals(abstractStyleKeyFactory.knownKeys))
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
    return knownKeys.hashCode();
  }
}
