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
 * ------------------------------
 * ReportBuilderHints.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportBuilderHints.java,v 1.1 2003/07/14 19:38:42 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14.07.2003 : Initial version
 *  
 */

package org.jfree.report;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ReportBuilderHints implements Serializable
{
  public static class ParserHintKey implements Serializable
  {
    private Object primaryKey;
    private String hintKey;

    public ParserHintKey(Object primaryKey, String hintKey)
    {
      if (primaryKey == null)
      {
        throw new NullPointerException();
      }
      if (hintKey == null)
      {
        throw new NullPointerException();
      }
      this.primaryKey = primaryKey;
      this.hintKey = hintKey;
    }

    public Object getPrimaryKey()
    {
      return primaryKey;
    }

    public String getHintKey()
    {
      return hintKey;
    }

    public boolean equals(Object o)
    {
      if (this == o) return true;
      if (!(o instanceof ParserHintKey)) return false;

      final ParserHintKey parserHintKey = (ParserHintKey) o;

      if (!hintKey.equals(parserHintKey.hintKey)) return false;
      if (!primaryKey.equals(parserHintKey.primaryKey)) return false;

      return true;
    }

    public int hashCode()
    {
      int result;
      result = primaryKey.hashCode();
      result = 29 * result + hintKey.hashCode();
      return result;
    }
  }

  private HashMap map;

  public ReportBuilderHints()
  {
    this.map = new HashMap();
  }

  public void putHint (Object target, String hint, Object hintValue)
  {
    ParserHintKey pHint = new ParserHintKey(target, hint);
    map.put(pHint, hintValue);
  }

  public Object getHint (Object target, String hint)
  {
    return map.get(new ParserHintKey(target, hint));
  }

  public Object getHint (Object target, String hint, Class objectType)
  {
    Object o = map.get(new ParserHintKey(target, hint));
    if (o == null)
    {
      return null;
    }
    if (objectType.isAssignableFrom(o.getClass()))
    {
      return o;
    }
    return null;
  }

  /**
   * Adds an hint into an ArrayList. If the hint is no list hint, a
   * IllegalArgumentException is thrown. If the speocified hint value is
   * already contained in that list, no action is taken.
   *
   * @param target the target object for which the hint is specified.
   * @param hint the hint name
   * @param hintValue the hint value (not null)
   * @throws java.lang.IllegalArgumentException if the specified hint is no list type.
   */
  public void addHintList (Object target, String hint, Object hintValue)
  {
    if (hintValue == null)
    {
      throw new NullPointerException("Hintvalue is null.");
    }

    Object o = getHint(target, hint);
    List hintList = null;
    if (o != null)
    {
      if (o instanceof List == false)
      {
        throw new IllegalArgumentException("The parser hint " + hint + " is no list type.");
      }
      hintList = (List) o;
    }
    else
    {
      hintList = new ArrayList();
      putHint(target, hint, hintList);
    }
    if (hintList.contains(hintValue) == false)
    {
      hintList.add(hintValue);
    }
  }
}
