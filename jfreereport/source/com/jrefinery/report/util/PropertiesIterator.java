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
 *
 * ChangeLog
 * ---------
 * 20-Aug-2002 : Initial version
 * 22-Aug-2002 : Removed logging statements
 * 01-Sep-2002 : Documentation
 * 05-Sep-2002 : More documentation
 */
package com.jrefinery.report.util;

import java.util.Iterator;
import java.util.Properties;

/**
 * The properties iterator iterates over a set of enumerated properties. The properties are named
 * by an optional prefix plus a number, which is counted up on each iteration:
 * <ul>
 * <li>prefix_0
 * <li>prefix_1
 * <li>prefix_2
 * <li>...
 * </ul>
 * The iterator iterates over all subsequent numbered proprties until the number-sequence is finished.
 */
public class PropertiesIterator implements Iterator
{
  private Properties properties;
  private String prefix;
  private int count;

  /**
   * Creates a new properties iterator without an prefix.
   */
  public PropertiesIterator(Properties properties)
  {
    this (properties, null);
  }

  /**
   * Creates a new properties iterator with the given prefix.
   */
  public PropertiesIterator(Properties properties, String prefix)
  {
    if (properties == null) throw new NullPointerException();
    this.properties = properties;
    this.prefix = prefix;
    this.count = 0;
  }

  /**
   * @returns true, if a next property is found in the sequence.
   */
  public boolean hasNext()
  {
    return properties.containsKey(getNextKey());
  }

  /**
   * Computes and returns the next possible key in the sequence. If the generated key does
   * not exist, the iteration will end.
   *
   * @returns the next key in the sequence
   */
  private String getNextKey ()
  {
    if (prefix == null)
    {
      return String.valueOf(count);
    }
    return prefix + String.valueOf(count);
  }

  /**
   * @returns the value of the next element in the sequence or null, if the end of the sequence
   * has been reached.
   */
  public Object next()
  {
    String value = properties.getProperty(getNextKey());
    count++;
    return value;
  }

  /**
   * Allways throws UnsupportedOperationException as remove is not implemented for this iterator.
   *
   * @throws UnsupportedOperationException as remove is not supported
   */
  public void remove()
  {
    throw new UnsupportedOperationException();
  }
}
