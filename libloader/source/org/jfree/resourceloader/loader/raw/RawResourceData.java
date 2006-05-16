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
 * RawResourceData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RawResourceData.java,v 1.1.1.1 2006/04/17 16:48:39 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.loader.raw;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 12.04.2006, 15:06:48
 *
 * @author Thomas Morgner
 */
public class RawResourceData implements ResourceData
{
  private RawResourceKey rawKey;

  public RawResourceData(final RawResourceKey rawKey)
  {
    if (rawKey == null) throw new NullPointerException();
    this.rawKey = rawKey;
  }

  public byte[] getResource(ResourceManager caller) throws ResourceLoadingException
  {
    return rawKey.getData();
  }

  public InputStream getResourceAsStream(ResourceManager caller) throws ResourceLoadingException
  {
    return new ByteArrayInputStream (rawKey.getData());
  }

  /**
   * We do not support attributes.
   *
   * @param key
   * @return
   */
  public Object getAttribute(String key)
  {
    return null;
  }

  public ResourceKey getKey()
  {
    return rawKey;
  }

  public long getVersion(ResourceManager caller)
          throws ResourceLoadingException
  {
    return -1;
  }
}
