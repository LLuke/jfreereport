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
 * ${name}
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BasicStyleSheet.java,v 1.2 2004/10/24 23:13:10 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14-May-2004 : Initial version
 *
 */

package org.jfree.layout.style;

import java.util.HashMap;

public class BasicStyleSheet implements StyleSheet
{
  private HashMap backend;
  private String name;

  public BasicStyleSheet (final String name)
  {
    backend = new HashMap();
    this.name = name;
  }

  public String getName ()
  {
    return name;
  }

  public Object getStyleProperty (final StyleKey key)
  {
    return getStyleProperty(key, null);
  }

  public Object getStyleProperty (final StyleKey key, final Object defaultValue)
  {
    final Object retval = backend.get(key);
    if (retval == null)
    {
      return defaultValue;
    }
    return retval;
  }

  public void setStyleProperty (final StyleKey key, final Object value)
  {
    backend.put(key, value);
  }

  public StyleKey[] getDefinedKeys ()
  {
    return (StyleKey[]) backend.keySet().toArray(new StyleKey[backend.size()]);
  }

  public Object clone ()
          throws CloneNotSupportedException
  {
    return super.clone();
  }
}
