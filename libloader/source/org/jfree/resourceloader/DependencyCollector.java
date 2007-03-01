/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libloader/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id: DependencyCollector.java,v 1.2 2006/12/03 16:41:15 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader;

import java.util.HashMap;
import java.io.Serializable;

/**
 * Creation-Date: 08.04.2006, 14:12:14
 *
 * @author Thomas Morgner
 */
public class DependencyCollector implements Serializable, Cloneable
{
  private HashMap dependencies;

  public DependencyCollector(final ResourceKey source,
                             final long version)
  {
    if (source == null)
    {
      throw new NullPointerException();
    }
    dependencies = new HashMap();
    dependencies.put(source, new Long(version));
  }

  public ResourceKey[] getDependencies()
  {
    return (ResourceKey[]) dependencies.keySet().toArray
            (new ResourceKey[dependencies.size()]);
  }

  public void add(final Resource dependentResource)
  {
    final ResourceKey[] depKeys = dependentResource.getDependencies();
    for (int i = 0; i < depKeys.length; i++)
    {
      final ResourceKey depKey = depKeys[i];
      final long version = dependentResource.getVersion(depKey);
      add(depKey, version);
    }
  }

  public void add(final ResourceKey resourceKey, final long version)
  {
    dependencies.put(resourceKey, new Long(version));
  }

  public long getVersion(final ResourceKey key)
  {
    final Long l = (Long) dependencies.get(key);
    if (l == null)
    {
      return -1;
    }
    return l.longValue();
  }

  public Object clone () throws CloneNotSupportedException
  {
    final DependencyCollector dc = (DependencyCollector) super.clone();
    dc.dependencies = (HashMap) dependencies.clone();
    return dc;
  }
}
