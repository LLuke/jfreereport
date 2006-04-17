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
 * ClassloaderResourceData.java
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
package org.jfree.resourceloader.loader.resource;

import java.io.InputStream;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.loader.AbstractResourceData;
import org.jfree.resourceloader.loader.LoaderUtils;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 05.04.2006, 15:15:36
 *
 * @author Thomas Morgner
 */
public class ClassloaderResourceData extends AbstractResourceData
{
  private ClassloaderResourceKey key;

  public ClassloaderResourceData(final ClassloaderResourceKey key)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    this.key = key;
  }

  public InputStream getResourceAsStream() throws ResourceLoadingException
  {
    return ObjectUtilities.getResourceAsStream
            (key.getResourcePath(), ClassloaderResourceData.class);
  }

  public Object getAttribute(String key)
  {
    // we do not support attributes ...
    if (key.equals(ResourceData.FILENAME))
    {
      return LoaderUtils.getFileName(this.key.getResourcePath());
    }
    return null;
  }

  public long getVersion()
  {
    // We assume, that the data does never change
    // This way, we get the benefit of the cache. 
    return 0;
  }

  public ResourceKey getKey()
  {
    return key;
  }
}
