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
 * $Id: ReportBuilderHints.java,v 1.4 2003/07/23 13:56:31 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14.07.2003 : Initial version
 *  
 */

package org.jfree.report;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.report.util.NullOutputStream;

public class ReportBuilderHints implements Serializable
{
  public static class ParserHintKey implements Serializable
  {
    private Object primaryKey;
    private String hintKey;

    public ParserHintKey(Serializable primaryKey, String hintKey)
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
      if (this == o)
      { 
        return true;
      }
      if (!(o instanceof ParserHintKey))
      { 
        return false;
      }

      final ParserHintKey parserHintKey = (ParserHintKey) o;

      if (!primaryKey.equals(parserHintKey.primaryKey))
      { 
        return false;
      }
      if (!hintKey.equals(parserHintKey.hintKey))
      { 
        return false;
      }

      return true;
    }

    public int hashCode()
    {
      int result;
      result = primaryKey.hashCode();
      result = 29 * result + hintKey.hashCode();
      return result;
    }

    public String toString()
    {
      StringBuffer b = new StringBuffer();
      b.append ("ParserHintKey={");
      b.append (primaryKey);
      b.append ("; ");
      b.append (hintKey);
      b.append ("}");
      return b.toString();
    }
  }

  private HashMap map;

  public ReportBuilderHints()
  {
    this.map = new HashMap();
  }

  public void putHint (Serializable target, String hint, Serializable hintValue)
  {
    ParserHintKey pHint = new ParserHintKey(target, hint);
    if (hintValue == null)
    {
      map.remove(pHint);
    }
    else
    {
      map.put(pHint, hintValue);
    }
  }

  public Object getHint (Serializable target, String hint)
  {
    return map.get(new ParserHintKey(target, hint));
  }

  public Object getHint (Serializable target, String hint, Class objectType)
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

  private void serialize (Object o)
  {
    try
    {
      ObjectOutputStream out = new ObjectOutputStream(new NullOutputStream());
      out.writeObject(o);
    }
    catch (IOException ipoe)
    {
      ipoe.printStackTrace();
      throw new IllegalArgumentException("Not serializable:" + o);
    }
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
  public void addHintList (Serializable target, String hint, Serializable hintValue)
  {
    this.addHintList(target, hint, hintValue, true);
  }

  /**
   * Adds an hint into an ArrayList. If the hint is no list hint, a
   * IllegalArgumentException is thrown. If the speocified hint value is
   * already contained in that list, no action is taken.
   *
   * @param target the target object for which the hint is specified.
   * @param hint the hint name
   * @param hintValue the hint value (not null)
   * @param unique true, if the value should be unique within the list
   * @throws java.lang.IllegalArgumentException if the specified hint is no list type.
   */
  public void addHintList (Serializable target, String hint, Serializable hintValue, boolean unique)
  {
    if (hintValue == null)
    {
      throw new NullPointerException("Hintvalue is null.");
    }

    serialize(target);
    serialize(hintValue);

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
      putHint(target, hint, (Serializable) hintList);
    }
    if (unique == false || hintList.contains(hintValue) == false)
    {
      hintList.add(hintValue);
    }
  }
}
