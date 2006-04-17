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
 * AbstractURLResourceLoader.java
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
package org.jfree.resourceloader.loader;

import java.net.URL;
import java.net.MalformedURLException;

import org.jfree.resourceloader.ResourceLoader;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 05.04.2006, 15:32:36
 *
 * @author Thomas Morgner
 */
public abstract class AbstractURLResourceLoader implements ResourceLoader
{
  private ResourceManager manager;

  public AbstractURLResourceLoader()
  {
  }

  public void setResourceManager(ResourceManager manager)
  {
    this.manager = manager;
  }

  public ResourceManager getManager()
  {
    return manager;
  }

  public boolean isSupportedKeyValue(Object value)
  {
    if (value instanceof URL)
    {
      URL url = (URL) value;
      if (getSchema().equalsIgnoreCase(url.getProtocol()))
      {
        return true;
      }
    }
    else if (value instanceof String)
    {
      String valueString = (String) value;
      if (valueString.startsWith(getSchema() + "://"))
      {
        return true;
      }
    }
    return false;
  }

  public ResourceKey createKey(Object value) throws ResourceKeyCreationException
  {
    if (value instanceof URL)
    {
      URL url = (URL) value;
      if (getSchema().equalsIgnoreCase(url.getProtocol()))
      {
        return new URLResourceKey(url);
      }
    }
    else if (value instanceof String)
    {
      String valueString = (String) value;
      if (valueString.startsWith(getSchema() + "://"))
      {
        try
        {
          return new URLResourceKey(new URL(valueString));
        }
        catch (MalformedURLException e)
        {
          throw new ResourceKeyCreationException("Unable to create ResourceKey for " + valueString);
        }
      }
    }
    throw new ResourceKeyCreationException
            ("HttpResourceLoader: This does not look like a valid http-URL");
  }

  public ResourceKey deriveKey(ResourceKey parent, Object data)
          throws ResourceKeyCreationException
  {
    if (parent instanceof URLResourceKey == false)
    {
      throw new ResourceKeyCreationException
              ("Parent key format is not recognized.");
    }
    URLResourceKey key = (URLResourceKey) parent;
    if (data instanceof String)
    {
      try
      {
        return new URLResourceKey(new URL (key.getUrl(), (String) data));
      }
      catch (MalformedURLException e)
      {
        throw new ResourceKeyCreationException
                ("Unable to create ResourceKey for " + data);
      }
    }
    throw new ResourceKeyCreationException
            ("Additional parameter format is not recognized.");
  }

  public ResourceData load(ResourceKey key) throws ResourceLoadingException
  {
    if (key instanceof URLResourceKey == false)
    {
      throw new ResourceLoadingException
              ("Key format is not recognized.");
    }
    return new URLResourceData((URLResourceKey) key);
  }
}
