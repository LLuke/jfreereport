/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * AttributeMap.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Iterator;

/**
 * Creation-Date: 09.04.2006, 16:12:13
 *
 * @author Thomas Morgner
 */
public class AttributeMap
{
  private HashMap namespaces;

  public AttributeMap()
  {
    namespaces = new HashMap();
  }

  public AttributeMap(final AttributeMap copy)
  {
    namespaces = (HashMap) copy.namespaces.clone();
    Iterator entries = namespaces.entrySet().iterator();
    while (entries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entries.next();
      final HashMap value = (HashMap) entry.getValue();
      entry.setValue(value.clone());
    }
  }

  public synchronized void setAttribute(String namespace, String attribute, Object value)
  {
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

  public synchronized Object getAttribute (String namespace, String attribute)
  {
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

  public synchronized Map getAttributes(final String namespace)
  {
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
    return (String[]) namespaces.keySet().toArray(new String[namespaces.size()]);
  }
}
