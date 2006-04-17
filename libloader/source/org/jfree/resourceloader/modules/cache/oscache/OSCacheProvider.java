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
 * OSCacheProvider.java
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
package org.jfree.resourceloader.modules.cache.oscache;

import org.jfree.resourceloader.cache.ResourceDataCacheProvider;
import org.jfree.resourceloader.cache.ResourceFactoryCacheProvider;
import org.jfree.resourceloader.cache.ResourceDataCache;
import org.jfree.resourceloader.cache.ResourceFactoryCache;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * Creation-Date: 13.04.2006, 16:32:20
 *
 * @author Thomas Morgner
 */
public class OSCacheProvider implements ResourceDataCacheProvider,
        ResourceFactoryCacheProvider
{
  private static GeneralCacheAdministrator dataCache;
  private static GeneralCacheAdministrator factoryCache;

  public OSCacheProvider()
  {
  }

  public ResourceDataCache createDataCache()
  {
    synchronized(OSCacheProvider.class)
    {
      if (dataCache == null)
      {
        // todo: Give properties ...
        dataCache = new GeneralCacheAdministrator();
      }
      return new OSResourceDataCache(dataCache);
    }
  }

  public ResourceFactoryCache createFactoryCache()
  {
    synchronized(OSCacheProvider.class)
    {
      if (factoryCache == null)
      {
        // todo: Give properties ...
        factoryCache = new GeneralCacheAdministrator();
      }
      return new OSResourceFactoryCache(factoryCache);
    }
  }
}
