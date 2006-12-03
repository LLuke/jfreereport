/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Creation-Date: 09.04.2006, 16:12:13
 *
 * @author Thomas Morgner
 */
public class AttributeMap implements Serializable
{
  private HashMap namespaces;
  private static final String[] EMPTY_NAMESPACES = new String[0];

  public AttributeMap()
  {
  }

  public AttributeMap(final AttributeMap copy)
  {
    if (copy.namespaces == null)
    {
      return;
    }

    namespaces = (HashMap) copy.namespaces.clone();
    Iterator entries = namespaces.entrySet().iterator();
    while (entries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entries.next();
      final HashMap value = (HashMap) entry.getValue();
      entry.setValue(value.clone());
    }
  }

  public synchronized void setAttribute(String namespace,
                                        String attribute,
                                        Object value)
  {
    if (namespaces == null)
    {
      namespaces = new HashMap();
    }

    final HashMap attrs = (HashMap) namespaces.get(namespace);
    if (attrs == null)
    {
      if (value == null)
      {
        return;
      }

      HashMap newAtts = new HashMap();
      newAtts.put(attribute, value);
      namespaces.put(namespace, newAtts);
    }
    else
    {
      if (value == null)
      {
        attrs.remove(attribute);
        if (attrs.isEmpty())
        {
          namespaces.remove(namespace);
        }
      }
      else
      {
        attrs.put(attribute, value);
      }
    }
  }

  public synchronized Object getAttribute(String namespace, String attribute)
  {
    if (namespaces == null)
    {
      return null;
    }

    final HashMap attrs = (HashMap) namespaces.get(namespace);
    if (attrs == null)
    {
      return null;
    }
    else
    {
      return attrs.get(attribute);
    }
  }

  public synchronized Object getFirstAttribute(String attribute)
  {
    if (namespaces == null)
    {
      return null;
    }

    final Iterator entries = namespaces.entrySet().iterator();
    while (entries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entries.next();
      final HashMap map = (HashMap) entry.getValue();
      Object val = map.get(attribute);
      if (val != null)
      {
        return val;
      }
    }
    return null;
  }

  public synchronized Map getAttributes(final String namespace)
  {
    if (namespaces == null)
    {
      return null;
    }

    final HashMap attrs = (HashMap) namespaces.get(namespace);
    if (attrs == null)
    {
      return null;
    }
    else
    {
      return Collections.unmodifiableMap(attrs);
    }
  }

  public synchronized String[] getNameSpaces()
  {
    if (namespaces == null)
    {
      return EMPTY_NAMESPACES;
    }
    return (String[]) namespaces.keySet().toArray(new String[namespaces.size()]);
  }
}
