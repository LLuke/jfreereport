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
 * ParserHints.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 14.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.parser.base;

import java.io.Serializable;
import java.util.HashMap;

public class ParserHints implements Serializable
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

  public ParserHints()
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

}
