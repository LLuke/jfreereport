/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ----------------------------
 * AbstractStyleKeyFactory.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractStyleKeyFactory.java,v 1.15 2003/06/27 14:25:19 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext.factory.stylekey;

import java.util.HashMap;
import java.util.Iterator;

import com.jrefinery.report.targets.style.StyleKey;
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
  public Object createBasicObject(final StyleKey k, final String value, final Class c, final ClassFactory fc)
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
   * Returns an iterator that provides access to the registered keys.
   *
   * @return The iterator.
   */
  public Iterator getRegisteredKeys()
  {
    return knownKeys.keySet().iterator();
  }
}
