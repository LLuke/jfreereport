/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------------------------
 * StyleKey.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.style;

import java.io.Serializable;
import java.util.Hashtable;

public class StyleKey implements Serializable, Cloneable
{
  private static Hashtable definedKeys;

  private String name;
  private Class valueType;

  private StyleKey(String name, Class valueType)
  {
    setName(name);
    setValueType(valueType);
  }

  public String getName()
  {
    return name;
  }

  private void setName(String name)
  {
    if (name == null)
      throw new NullPointerException("Name must not be null");
    this.name = name;
  }

  public Class getValueType()
  {
    return valueType;
  }

  private void setValueType(Class valueType)
  {
    if (valueType == null)
      throw new NullPointerException("ValueType must not be null");

    this.valueType = valueType;
  }

  public static StyleKey getStyleKey (String name, Class valueType)
  {
    if (definedKeys == null)
    {
      definedKeys = new Hashtable();
    }
    StyleKey key = (StyleKey) definedKeys.get (name);
    if (key == null)
    {
      key = new StyleKey(name, valueType);
      definedKeys.put (name, key);
    }
    return key;
  }

  public static StyleKey getStyleKey (String name)
  {
    if (definedKeys == null)
    {
      return null;
    }
    else
      return (StyleKey) definedKeys.get (name);
  }
}
