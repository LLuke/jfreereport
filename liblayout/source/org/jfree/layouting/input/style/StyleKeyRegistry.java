/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * StyleKeyFactory.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: StyleKeyRegistry.java,v 1.1 2006/02/12 21:54:26 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20.11.2005 : Initial version
 */
package org.jfree.layouting.input.style;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.Log;

/**
 * This class should not be static, or we might create a memory leak.
 *
 * @author Thomas Morgner
 */
public class StyleKeyRegistry
{
  private static StyleKeyRegistry registry;

  public static synchronized StyleKeyRegistry getRegistry()
  {
    if (registry == null)
    {
      registry = new StyleKeyRegistry();
      registry.registerDefaults();
    }
    return registry;
  }

  private HashMap knownStyleKeys;

  private StyleKeyRegistry()
  {
    knownStyleKeys = new HashMap();
  }

  public StyleKey findKeyByName(String name)
  {
    return (StyleKey) knownStyleKeys.get(name);
  }

  public int getIndexForKey(StyleKey key)
  {
    final Integer index = (Integer) knownStyleKeys.get(key.getName());
    if (index != null)
    {
      return index.intValue();
    }

    throw new IllegalStateException("This key is none of mine.");
  }

  public int getKeyCount()
  {
    return knownStyleKeys.size();
  }

  public void registerDefaults()
  {
    final Configuration config = LibLayoutBoot.getInstance().getGlobalConfig();
    Iterator it = config.findPropertyKeys("org.jfree.layouting.stylekeys.");
    final ClassLoader classLoader =
            ObjectUtilities.getClassLoader(StyleKeyRegistry.class);

    while (it.hasNext())
    {
      String key = (String) it.next();
      try
      {
        Class c = classLoader.loadClass(config.getConfigProperty(key));
        registerClass(c);
      }
      catch (ClassNotFoundException e)
      {
        // ignore that class
      }
      catch (NullPointerException e)
      {
        // ignore invalid values as well.
      }
    }

  }

  public void registerClass(Class c)
  {
    Log.debug ("Registering stylekeys from " + c);
    try
    {
      Field[] fields = c.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        Field field = fields[i];
        final int modifiers = field.getModifiers();
        if (Modifier.isPublic(modifiers) &&
                Modifier.isStatic(modifiers))
        {
          if (field.getType().isAssignableFrom(StyleKey.class))
          {
            StyleKey value = (StyleKey) field.get(null);
            // ignore the returned value, all we want is to trigger the key
            // creation
            // Log.debug ("Loaded key " + value);
          }
        }
      }
    }
    catch (IllegalAccessException e)
    {
      // wont happen, we've checked it..
    }
  }

  public synchronized StyleKey createKey(final String name,
                                         final boolean trans,
                                         final boolean inherited,
                                         final boolean listOfValues)
  {
    final StyleKey existingKey = findKeyByName(name);
    if (existingKey != null)
    {
      return existingKey;
    }
    final StyleKey createdKey = new StyleKey
            (name, trans, inherited, listOfValues, knownStyleKeys.size());
    knownStyleKeys.put(name, createdKey);
    return createdKey;
  }

  public StyleKey[] getKeys()
  {
    return (StyleKey[]) knownStyleKeys.values().toArray
            (new StyleKey[knownStyleKeys.size()]);
  }
}