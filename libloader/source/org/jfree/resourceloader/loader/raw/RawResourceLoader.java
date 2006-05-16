/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libloader/
 * Project Lead:  Thomas Morgner;
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
 * ------------
 * RawResourceLoader.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RawResourceLoader.java,v 1.1.1.1 2006/04/17 16:48:39 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.loader.raw;

import java.util.Map;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoader;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.AbstractResourceKey;

/**
 * Creation-Date: 12.04.2006, 15:19:03
 *
 * @author Thomas Morgner
 */
public class RawResourceLoader implements ResourceLoader
{
  private ResourceManager resourceManager;

  public RawResourceLoader()
  {
  }

  public String getSchema()
  {
    return "raw:bytes";
  }

  public boolean isSupportedKeyValue(Map values)
  {
    final Object value = values.get(AbstractResourceKey.CONTENT_KEY);
    return (value instanceof byte[]);
  }

  public ResourceKey createKey(Map values) throws ResourceKeyCreationException
  {
    final Object value = values.get(AbstractResourceKey.CONTENT_KEY);
    if (value instanceof byte[])
    {
      return new RawResourceKey(values);
    }
    throw new ResourceKeyCreationException("The key data was not recognized.");
  }

  public ResourceKey deriveKey(ResourceKey parent, Map data)
          throws ResourceKeyCreationException
  {
    // we do not support derived keys ..
    throw new ResourceKeyCreationException(
            "Derived keys are not supported by this implementation.");
  }

  public ResourceData load(ResourceKey key) throws ResourceLoadingException
  {
    if (key instanceof RawResourceKey == false)
    {
      throw new ResourceLoadingException("The key type is not supported.");
    }
    return new RawResourceData((RawResourceKey) key);
  }

  public ResourceManager getResourceManager()
  {
    return resourceManager;
  }

  public void setResourceManager(ResourceManager manager)
  {
    this.resourceManager = manager;
  }
}
