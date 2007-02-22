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
 * $Id: ResourceLoader.java,v 1.3 2006/12/03 16:41:15 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader;

import java.util.Map;
import java.net.URL;

/**
 * A resource loader knows how to get binary rawdata from a location specified
 * by an resource key. A resource key is a wrapper around any kind of data that
 * is suitable to identify a resource location. The resource key can also hold
 * configuration data for the factory.
 *
 * If the storage system is hierarchical, a new resource key can be derived from
 * a given path-string.
 *
 * @author Thomas Morgner
 */
public interface ResourceLoader
{
  /**
   * Checks, whether this resource loader implementation was responsible for
   * creating this key.
   *
   * @param key
   * @return
   */
  public boolean isSupportedKey (ResourceKey key);

  /**
   * Creates a new resource key from the given object and the factory keys.
   *
   * @param value
   * @param factoryKeys
   * @return the created key or null, if the format was not recognized.
   * @throws ResourceKeyCreationException if creating the key failed.
   */
  public ResourceKey createKey (Object value,
                                Map factoryKeys)
      throws ResourceKeyCreationException;

  /**
   * Derives a new resource key from the given key. If neither a path nor new
   * factory-keys are given, the parent key is returned.
   *
   * @param parent the parent
   * @param path the derived path (can be null).
   * @param factoryKeys the optional factory keys (can be null).
   * @return the derived key.
   * @throws ResourceKeyCreationException if the key cannot be derived for any
   * reason.
   */
  public ResourceKey deriveKey (ResourceKey parent,
                                String path,
                                Map factoryKeys)
      throws ResourceKeyCreationException;

  /**
   * Loads the binary data represented by this key.
   *
   * @param key
   * @return
   * @throws ResourceLoadingException
   */
  public ResourceData load (ResourceKey key)
      throws ResourceLoadingException;

  public void setResourceManager (ResourceManager manager);

  public URL toURL (ResourceKey key);
}
