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
 * PropertiesIterator.java
 * -----------------------
 */
package com.jrefinery.report.util;

import java.util.Iterator;
import java.util.Properties;

public class PropertiesIterator implements Iterator
{
  private Properties properties;
  private String prefix;
  private int count;

  public PropertiesIterator(Properties properties)
  {
    this (properties, null);
  }

  public PropertiesIterator(Properties properties, String prefix)
  {
    if (properties == null) throw new NullPointerException();
    this.properties = properties;
    this.prefix = prefix;
    this.count = 0;
  }

  public boolean hasNext()
  {
    return properties.containsKey(getNextKey());
  }

  private String getNextKey ()
  {
    if (prefix == null)
    {
      return String.valueOf(count);
    }
    return prefix + String.valueOf(count);
  }

  public Object next()
  {
    String value = properties.getProperty(getNextKey());
    count++;
    return value;
  }

  public void remove()
  {
    // is ignored
  }
}
