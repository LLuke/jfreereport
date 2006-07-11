/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * AbstractCompoundValueReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AbstractCompoundValueReadHandler.java,v 1.2 2006/04/17 20:51:03 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.parser.stylehandler;

import java.util.Map;
import java.util.HashMap;

import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 03.12.2005, 19:10:30
 *
 * @author Thomas Morgner
 */
public class AbstractCompoundValueReadHandler
        implements CSSCompoundValueReadHandler
{
  private HashMap handlers;

  public AbstractCompoundValueReadHandler()
  {
    this.handlers = new HashMap();
  }

  protected synchronized void addHandler (StyleKey key, CSSValueReadHandler handler)
  {
    this.handlers.put(key, handler);
  }

  public synchronized StyleKey[] getAffectedKeys()
  {
    return (StyleKey[])
            handlers.keySet().toArray(new StyleKey[handlers.size()]);
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public synchronized Map createValues(LexicalUnit unit)
  {
    final Map map = new HashMap();
    final Map.Entry[] entries = (Map.Entry[])
            handlers.entrySet().toArray(new Map.Entry[handlers.size()]);
    while (unit != null)
    {
      for (int i = 0; i < entries.length; i++)
      {
        Map.Entry entry = entries[i];
        CSSValueReadHandler valueReadHandler = (CSSValueReadHandler) entry.getValue();
        StyleKey key = (StyleKey) entry.getKey();
        CSSValue value = valueReadHandler.createValue(key, unit);
        if (value != null)
        {
          map.put(key, value);
          break;
        }
      }
      unit = unit.getNextLexicalUnit();
    }
    return map;
  }
}
