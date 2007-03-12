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
 * $Id: AttributeMap.java,v 1.4 2007/03/06 14:35:59 taqua Exp $
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
public class AttributeMap implements Serializable, Cloneable
{
  private static final long serialVersionUID = -7442871030874215436L;
  private static final String[] EMPTY_NAMESPACES = new String[0];

  private HashMap namespaces;

  public AttributeMap()
  {
  }

  public AttributeMap(final AttributeMap copy)
  {
    if (copy == null)
    {
      return;
    }
    
    if (copy.namespaces == null)
    {
      return;
    }

    namespaces = (HashMap) copy.namespaces.clone();
    final Iterator entries = namespaces.entrySet().iterator();
    while (entries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entries.next();
      final HashMap value = (HashMap) entry.getValue();
      entry.setValue(value.clone());
    }
  }


  public synchronized Object clone()
      throws CloneNotSupportedException
  {
    final AttributeMap map = (AttributeMap) super.clone();
    map.namespaces = (HashMap) namespaces.clone();
    final Iterator entries = map.namespaces.entrySet().iterator();
    while (entries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entries.next();
      final HashMap value = (HashMap) entry.getValue();
      entry.setValue(value.clone());
    }
    return map;
  }

  public synchronized void setAttribute(final String namespace,
                                        final String attribute,
                                        final Object value)
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

      final HashMap newAtts = new HashMap();
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

  public synchronized Object getAttribute(final String namespace,
                                          final String attribute)
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

  public synchronized Object getFirstAttribute(final String attribute)
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
      final Object val = map.get(attribute);
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
      return AttributeMap.EMPTY_NAMESPACES;
    }
    return (String[]) namespaces.keySet().toArray(new String[namespaces.size()]);
  }
}
