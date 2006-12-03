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

package org.jfree.resourceloader.loader.jar;

import java.util.Map;
import java.util.HashMap;
import java.net.URL;
import java.net.MalformedURLException;

import org.jfree.resourceloader.loader.AbstractURLResourceLoader;
import org.jfree.resourceloader.loader.URLResourceKey;
import org.jfree.resourceloader.AbstractResourceKey;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;

/**
 * Creation-Date: 05.04.2006, 15:27:21
 *
 * @author Thomas Morgner
 */
public class JarResourceLoader extends AbstractURLResourceLoader
{
  public JarResourceLoader()
  {
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
      if (valueString.startsWith(getSchema() + ":"))
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
      if (valueString.startsWith(getSchema() + ":"))
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
            ("HttpResourceLoader: This does not look like a valid Jar-URL");
  }

  public String getSchema()
  {
    return "jar";
  }

}
