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
 * $Id: AbstractURLResourceLoader.java,v 1.1.1.1 2006/04/17 16:48:31 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.loader;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.HashMap;

import org.jfree.resourceloader.ResourceLoader;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.AbstractResourceKey;

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

  public boolean isSupportedKeyValue(Map values)
  {
    final Object value = values.get(AbstractResourceKey.CONTENT_KEY);
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

  public ResourceKey createKey(Map values) throws ResourceKeyCreationException
  {
    final Object value = values.get(AbstractResourceKey.CONTENT_KEY);
    if (value instanceof URL)
    {
      URL url = (URL) value;
      if (getSchema().equalsIgnoreCase(url.getProtocol()))
      {
        return new URLResourceKey(values);
      }
    }
    else if (value instanceof String)
    {
      String valueString = (String) value;
      if (valueString.startsWith(getSchema() + "://"))
      {
        try
        {
          final URL url = new URL(valueString);
          final Map derivedValues = new HashMap (values);
          derivedValues.put(AbstractResourceKey.CONTENT_KEY, url);
          return new URLResourceKey(derivedValues);
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

  public ResourceKey deriveKey(ResourceKey parent, Map data)
          throws ResourceKeyCreationException
  {
    if (parent instanceof URLResourceKey == false)
    {
      throw new ResourceKeyCreationException
              ("Parent key format is not recognized.");
    }
    URLResourceKey key = (URLResourceKey) parent;
    final Object deriveUrl = data.get(AbstractResourceKey.CONTENT_KEY);
    if (deriveUrl instanceof String == false)
    {
      throw new ResourceKeyCreationException
              ("Additional parameter format is not recognized.");
    }

    try
    {
      final URL url = new URL (key.getUrl(), (String) deriveUrl);
      final Map derivedValues = new HashMap (parent.getParameters());
      derivedValues.putAll(data);
      derivedValues.put(AbstractResourceKey.CONTENT_KEY, url);
      return new URLResourceKey(derivedValues);
    }
    catch (MalformedURLException e)
    {
      throw new ResourceKeyCreationException
              ("Unable to create ResourceKey for " + data);
    }
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
