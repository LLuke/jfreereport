/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * ReportProperties.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 26-May-2002 : Created ReportProperties as a small scale hashtable with protected string keys.
 *               This implementation guarantees that all keys are strings.
 */
package com.jrefinery.report.util;

import java.util.Hashtable;
import java.util.Enumeration;

public class ReportProperties
{
  private Hashtable properties;

  /**
   * Adds a property to this properties collection. If a property with the given name
   * exist, the property will be replaced with the new value. If the
   * value is null, the property will be removed.
   *
   * @param key The key.
   * @param value The value.
   */
  public void put (String key, Object value)
  {
    if (value == null)
    {
      this.properties.remove(key);
    }
    else
    {
      this.properties.put(key, value);
    }
  }

  public Object get (String key)
  {
    return properties.get (key);
  }

  public Object get (String key, Object def)
  {
    Object o = properties.get (key);
    if (o == null) return def;
    return o;
  }

  public Enumeration keys ()
  {
    return properties.keys();
  }

  public ReportProperties (ReportProperties props)
  {
    properties = new Hashtable(props.properties);
  }

  public ReportProperties ()
  {
    properties = new Hashtable();
  }

  public void clear ()
  {
    properties.clear();
  }

  public boolean containsKey (String key)
  {
    return properties.containsKey(key);
  }
}
