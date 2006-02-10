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
 * MapStringExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: MapIndirectExpression.java,v 1.1 2006/01/20 19:50:52 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20.01.2006 : Initial version
 */
package org.jfree.report.function.strings;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.report.function.AbstractExpression;

/**
 * Maps a string read from a column into an other string. The possible mappings
 * are given as (key, text) pairs. If the string from the column is null or
 * matches none of the defined keys, a fallback value is returned.
 *
 * If the fallback value is undefined, the original value is returned instead.
 *
 * @author Thomas Morgner
 */
public class MapIndirectExpression extends AbstractExpression
{
  private String field;
  private ArrayList keys;
  private ArrayList forwards;
  private boolean ignoreCase;
  private String fallbackForward;
  private String nullValue;

  public MapIndirectExpression()
  {
    keys = new ArrayList();
    forwards = new ArrayList();
  }

  public String getField()
  {
    return field;
  }

  public String getNullValue()
  {
    return nullValue;
  }

  public void setNullValue(final String nullValue)
  {
    this.nullValue = nullValue;
  }

  public void setField(final String field)
  {
    this.field = field;
  }

  public String getFallbackForward()
  {
    return fallbackForward;
  }

  public void setFallbackForward(final String fallbackForward)
  {
    this.fallbackForward = fallbackForward;
  }

  public void setKey (final int index, final String key)
  {
    if (keys.size() == index)
    {
      keys.add(key);
    }
    else
    {
      keys.set(index, key);
    }
  }

  public String getKey (final int index)
  {
    return (String) keys.get(index);
  }

  public int getKeyCount ()
  {
    return keys.size();
  }

  public String[] getKey ()
  {
    return (String[]) keys.toArray(new String[keys.size()]);
  }

  public void setKey (final String[] keys)
  {
    this.keys.clear();
    this.keys.addAll(Arrays.asList(keys));
  }


  public void setForward (final int index, final String value)
  {
    if (forwards.size() == index)
    {
      forwards.add(value);
    }
    else
    {
      forwards.set(index, value);
    }
  }

  public String getForward (final int index)
  {
    return (String) forwards.get(index);
  }

  public int getForwardCount ()
  {
    return forwards.size();
  }

  public String[] getForward ()
  {
    return (String[]) forwards.toArray(new String[forwards.size()]);
  }

  public void setForward (final String[] expression)
  {
    this.forwards.clear();
    this.forwards.addAll(Arrays.asList(expression));
  }


  /**
   * Return a completly separated copy of this function. The copy does no longer
   * share any changeable objects with the original function. Only the datarow
   * may be shared.
   *
   * @return a copy of this function.
   */
  public Object clone() throws CloneNotSupportedException
  {
    MapIndirectExpression co = (MapIndirectExpression) super.clone();
    co.forwards = (ArrayList) forwards.clone();
    co.keys = (ArrayList) keys.clone();
    return co;
  }

  public boolean isIgnoreCase()
  {
    return ignoreCase;
  }

  public void setIgnoreCase(final boolean ignoreCase)
  {
    this.ignoreCase = ignoreCase;
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on
   * the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    Object raw = getDataRow().get(getField());
    if (raw == null)
    {
      return getNullValue();
    }
    final String text = String.valueOf(raw);
    final int length = Math.min (keys.size(), forwards.size());
    for (int i = 0; i < length; i++)
    {
      final String key = (String) keys.get(i);
      if (isIgnoreCase())
      {
        if (text.equalsIgnoreCase(key))
        {
          String target = (String) forwards.get(i);
          return getDataRow().get(target);
        }
      }
      else
      {
        if (text.equals(key))
        {
          String target = (String) forwards.get(i);
          return getDataRow().get(target);
        }
      }
    }
    final String fallbackValue = getFallbackForward();
    if (fallbackValue != null)
    {
      return getDataRow().get(fallbackValue);
    }
    return raw;
  }
}
