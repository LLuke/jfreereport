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
 * ----------------------------
 * AbstractStyleKeyFactory.java
 * ----------------------------
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

package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ext.factory.objects.ClassFactory;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.targets.style.StyleKey;

import java.util.Hashtable;
import java.util.Iterator;

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
  private Hashtable knownKeys;
  
  /** A class factory. */
  private ClassFactory classFactory;

  /**
   * Creates a new factory. 
   */
  public AbstractStyleKeyFactory()
  {
    knownKeys = new Hashtable();
  }

  /**
   * Returns the class factory.
   * 
   * @return The class factory.
   */
  public ClassFactory getClassFactory()
  {
    return classFactory;
  }

  /**
   * Sets the class factory.
   * 
   * @param classFactory  the class factory.
   */
  public void setClassFactory(ClassFactory classFactory)
  {
    if (classFactory == null) 
    {
      throw new NullPointerException();
    }
    this.classFactory = classFactory;
  }

  /**
   * Registers a key.
   * 
   * @param key  the key.
   */
  public void addKey (StyleKey key)
  {
    knownKeys.put (key.getName(), key);
  }

  /**
   * Returns the key with the given name.
   * 
   * @param name  the name.
   * 
   * @return The key.
   */
  public StyleKey getStyleKey (String name)
  {
    return (StyleKey) knownKeys.get (name);
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
    if (k == null)
    {
      // no such key registered ...
      return null;
    }

    if (c == null)
    {
      throw new NullPointerException();
    }
    
    if (classFactory == null)
    {
      throw new NullPointerException("Class " + getClass());
    }
    
    ObjectDescription od = classFactory.getDescriptionForClass(c);
    if (od == null) 
    {
      return null;
    }

    od.setParameter("value", value);
    return od.createObject();
  }

  /**
   * Initialise.
   * 
   * @param parser  the parser.
   */
  public void init(Parser parser)
  {
    ClassFactory f = (ClassFactory) parser.getConfigurationValue(OBJECT_FACTORY_TAG);
    if (f == null)
    {
      throw new NullPointerException();
    }
    setClassFactory(f);
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
