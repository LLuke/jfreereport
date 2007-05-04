/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://reporting.pentaho.org/libloader/
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
 * $Id: ResourceData.java,v 1.5 2007/04/01 13:43:17 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader;

import java.io.InputStream;

/**
 * A resource data object encapsulates the raw data of an resource at a given
 * point in the past.
 *
 * Any change to the resource increases the version number. Version numbers
 * are not needed to be checked regulary, but must be checked on each call to
 * 'getVersion()'.
 *
 * This definitly does *not* solve the problem of concurrent modifications; if
 * you need to be sure that the resource has not been altered between the last
 * call to 'getVersion' and 'getResource..' external locking mechanism have to
 * be implemented.
 *
 * @author Thomas Morgner
 */
public interface ResourceData
{
  public static final String CONTENT_LENGTH = "content-length";
  public static final String CONTENT_TYPE = "content-type";
  public static final String FILENAME = "filename";

  public InputStream getResourceAsStream(ResourceManager caller) throws ResourceLoadingException;

  /**
   * This is dangerous, especially if the resource is large.
   *
   * @param caller
   * @return
   * @throws ResourceLoadingException
   */
  public byte[] getResource(ResourceManager caller) throws ResourceLoadingException;

  /**
   * Tries to read data into the given byte-array.
   *
   * @param caller
   * @param target
   * @param offset
   * @param length
   * @return the number of bytes read or -1 if no more data can be read.
   * @throws ResourceLoadingException
   */
  public int getResource(ResourceManager caller, byte[] target, int offset, int length)
      throws ResourceLoadingException;

  public Object getAttribute (String key);
  public ResourceKey getKey();
  public long getVersion(ResourceManager caller)
          throws ResourceLoadingException;

}
