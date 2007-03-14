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
 * $Id: ResourceKey.java,v 1.5 2007/02/22 20:03:20 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The key is an unique identifier for the resource. Most of the time,
 * this may be an URL, but other (especially database based) schemas are
 * possible.
 *
 * A resource key must provide an 'equals' implementation. ResourceKeys should
 * be implemented as immutable classes, so that they can be safely stored in
 * collections or on external storages (like caches).
 *
 * @author Thomas Morgner
 */
public final class ResourceKey implements Serializable
{
  private static final Map EMPTY_MAP =
      Collections.unmodifiableMap(new HashMap());

  private Map factoryParameters;
  private Integer hashCode;
  private Object schema;
  private Object identifier;
  private ResourceKey parent;

  public ResourceKey(final Object schema,
                     final Object identifier,
                     final Map factoryParameters)
  {
    if (schema == null)
    {
      throw new NullPointerException();
    }
    if (identifier == null)
    {
      throw new NullPointerException();
    }

    this.schema = schema;
    this.identifier = identifier;
    if (factoryParameters != null)
    {
      this.factoryParameters =
          Collections.unmodifiableMap(new HashMap(factoryParameters));
    }
    else
    {
      this.factoryParameters = EMPTY_MAP;
    }
  }

  public ResourceKey(final ResourceKey parent,
                     final Object schema,
                     final Object identifier,
                     final Map factoryParameters)
  {
    this(schema, identifier, factoryParameters);
    this.parent = parent;
  }

  public ResourceKey getParent()
  {
    return parent;
  }

  public Map getFactoryParameters ()
  {
    return factoryParameters;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final ResourceKey that = (ResourceKey) o;

    if (!factoryParameters.equals(that.factoryParameters))
    {
      return false;
    }
    if (!identifier.equals(that.identifier))
    {
      return false;
    }
    if (!schema.equals(that.schema))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    if (hashCode == null)
    {
      int result;
      result = factoryParameters.hashCode();
      result = 29 * result + schema.hashCode();
      result = 29 * result + identifier.hashCode();
      hashCode = new Integer(result);
    }
    return hashCode.intValue();
  }

  public Object getIdentifier()
  {
    return identifier;
  }

  /**
   * Returns the schema of this resource key. The schema is an internal
   * identifier to locate the resource-loader implementation that was
   * responsible for creating the key in the first place.
   *
   * The schema has no meaning outside the resource loading framework.
   *
   * @return
   */
  public Object getSchema ()
  {
    return schema;
  }


  public String toString()
  {
    return "ResourceKey{" +
           "schema=" + schema +
           ", identifier=" + identifier +
           ", factoryParameters=" + factoryParameters +
           ", parent=" + parent +
           '}';
  }
}

