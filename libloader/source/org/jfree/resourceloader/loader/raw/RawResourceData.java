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
 * $Id: RawResourceData.java,v 1.4 2006/12/03 16:41:16 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.loader.raw;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
  private ResourceKey rawKey;
  private byte[] data;

  public RawResourceData(final ResourceKey rawKey)
  {
    if (rawKey == null)
    {
      throw new NullPointerException();
    }
    this.rawKey = rawKey;
    this.data = (byte[]) rawKey.getIdentifier();
  }

  public byte[] getResource(ResourceManager caller)
      throws ResourceLoadingException
  {
    return (byte[]) data.clone();
  }

  public InputStream getResourceAsStream(ResourceManager caller)
      throws ResourceLoadingException
  {
    return new ByteArrayInputStream(data);
  }

  /**
   * Tries to read data into the given byte-array.
   *
   * @param caller
   * @param target
   * @param offset
   * @param length
   * @return the number of bytes read or -1 if no more data can be read.
   * @throws org.jfree.resourceloader.ResourceLoadingException
   *
   */
  public int getResource(ResourceManager caller,
                         byte[] target,
                         int offset,
                         int length) throws ResourceLoadingException
  {
    if (offset > data.length)
    {
      return -1;
    }

    final int remaining = data.length - offset;
    final int maxReadable = Math.min(target.length, Math.min (remaining, length));

    System.arraycopy(data, offset, target, 0, maxReadable);
    return maxReadable;
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
