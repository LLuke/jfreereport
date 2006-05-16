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
 * DefaultResourceDataCacheEntry.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultResourceDataCacheEntry.java,v 1.1.1.1 2006/04/17 16:48:43 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.cache;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.ResourceLoadingException;

/**
 * Creation-Date: 06.04.2006, 09:53:37
 *
 * @author Thomas Morgner
 */
public class DefaultResourceDataCacheEntry implements ResourceDataCacheEntry
{
  private ResourceData data;
  private long version;

  public DefaultResourceDataCacheEntry(final ResourceData data,
                                       final ResourceManager manager)
          throws ResourceLoadingException
  {
    if (data == null)
    {
      throw new NullPointerException();
    }
    this.version = data.getVersion(manager);
    this.data = data;
  }

  public ResourceData getData()
  {
    return data;
  }

  public long getStoredVersion()
  {
    return version;
  }
}
