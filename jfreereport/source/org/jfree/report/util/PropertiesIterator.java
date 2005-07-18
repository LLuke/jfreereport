/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * $Id: PropertiesIterator.java,v 1.4 2005/02/23 21:06:05 taqua Exp $
 *
 * Changes
 * -------
 * 20-Aug-2002 : Initial version
 * 22-Aug-2002 : Removed logging statements
 * 01-Sep-2002 : Documentation
 * 05-Sep-2002 : More documentation
 */
package org.jfree.report.util;

import java.util.Iterator;
import java.util.Properties;

/**
 * The properties iterator iterates over a set of enumerated properties. The properties
 * are named by an optional prefix plus a number, which is counted up on each iteration:
 * <p/>
 * <ul><li>prefix_0 </li><li>prefix_1 </li><li>prefix_2 </li><li>... </li></ul>
 * <p/>
 * The iterator iterates over all subsequent numbered proprties until the number-sequence
 * is finished.
 *
 * @author Thomas Morgner
 */
public class PropertiesIterator implements Iterator
{
  /**
   * The underlying properties collection.
   */
  private Properties properties;

  /**
   * The property name prefix.
   */
  private String prefix;

  /**
   * An incremental counter.
   */
  private int count;

  /**
   * Creates a new properties iterator without an prefix.
   *
   * @param properties the underlying properties collection.
   */
  public PropertiesIterator (final Properties properties)
  {
    this(properties, null);
  }

  /**
   * Creates a new properties iterator with the given prefix.
   *
   * @param properties the underlying properties collection.
   * @param prefix     a prefix for generating property names (null permitted).
   */
  public PropertiesIterator (final Properties properties, final String prefix)
  {
    if (properties == null)
    {
      throw new NullPointerException();
    }
    this.properties = properties;
    this.prefix = prefix;
    this.count = 0;
  }

  /**
   * Returns true if there is a property in the underlying collection with a name that
   * matches the name returned by the getNextKey() method.
   *
   * @return true if there is another property with a name in the correct form.
   */
  public boolean hasNext ()
  {
    return properties.containsKey(getNextKey());
  }

  /**
   * Generates a property name in the form <prefix><count>. <P> The <count> begins at 0,
   * and is automatically incremented with each call to the next() method.
   *
   * @return the next key in the sequence
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
   * Returns the property with a name the same as the name generated by the getNextKey()
   * method, or null if there is no such property (that is, then end of the sequence has
   * been reached).
   *
   * @return the property or null.
   */
  public Object next ()
  {
    final String value = properties.getProperty(getNextKey());
    count++;
    return value;
  }

  /**
   * Always throws UnsupportedOperationException as remove is not implemented for this
   * iterator.
   *
   * @throws UnsupportedOperationException as remove is not supported.
   */
  public void remove ()
  {
    throw new UnsupportedOperationException();
  }

}
