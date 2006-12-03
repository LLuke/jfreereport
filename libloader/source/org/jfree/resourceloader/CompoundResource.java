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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.resourceloader;

/**
 * Creation-Date: 08.04.2006, 14:08:13
 *
 * @author Thomas Morgner
 */
public class CompoundResource implements Resource
{
  private ResourceKey source;
  private DependencyCollector dependencies;
  private Object product;

  public CompoundResource(final ResourceKey source,
                          final DependencyCollector dependencies,
                          final Object product)
  {
    if (source == null)
    {
      throw new NullPointerException("Source must not be null");
    }
    if (dependencies == null)
    {
      throw new NullPointerException("Dependecies must be given.");
    }
    if (product == null)
    {
      throw new NullPointerException("Product must not be null");
    }
    this.source = source;
    try
    {
      this.dependencies = (DependencyCollector) dependencies.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException
              ("Clone not supported? This should not happen.");
    }
    this.product = product;
  }

  public Object getResource()
  {
    return product;
  }

  public long getVersion(ResourceKey key)
  {
    return dependencies.getVersion(key);
  }

  /**
   * The primary source is also included in this set. The dependencies are given
   * as ResourceKey objects. The keys itself do not hold any state information.
   * <p/>
   * The dependencies do not track deep dependencies. So if Resource A depends
   * on Resource B which depends on Resource C, then A only knows about B, not
   * C.
   *
   * @return
   */
  public ResourceKey[] getDependencies()
  {
    return dependencies.getDependencies();
  }

  public ResourceKey getSource()
  {
    return source;
  }
}
