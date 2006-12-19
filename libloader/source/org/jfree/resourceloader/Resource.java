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
 * $Id: Resource.java,v 1.2 2006/12/03 16:41:15 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader;

import java.io.Serializable;

/**
 * A resource is a wrapper around the final product. It shall not hold any
 * references to the ResourceData object used to create the resource (to allow
 * efficient 2-stage caching).
 *
 * Although this interfaces declares to be serializable, this might not be the
 * case for some of the content contained in the resource object. Cache
 * implementors should be aware of that issue and should act accordingly
 * (for instance by not caching that object).
 *
 * @author Thomas Morgner
 */
public interface Resource extends Serializable
{
  public Object getResource () throws ResourceException;

  public long getVersion(ResourceKey key);

  /**
   * The primary source is also included in this set. The dependencies are
   * given as ResourceKey objects. The keys itself do not hold any state
   * information.
   *
   * The dependencies do not track deep dependencies. So if Resource A depends
   * on Resource B which depends on Resource C, then A only knows about B, not
   * C.
   *
   * @return
   */
  public ResourceKey[] getDependencies();
  public ResourceKey getSource();
}
