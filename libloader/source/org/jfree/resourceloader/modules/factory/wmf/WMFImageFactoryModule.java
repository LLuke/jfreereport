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
 * WMFImageFactoryModule.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: WMFImageFactoryModule.java,v 1.1.1.1 2006/04/17 16:48:40 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.modules.factory.wmf;

import java.awt.Image;
import java.io.IOException;

import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.SimpleResource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.pixie.wmf.WmfFile;

/**
 * Creation-Date: 05.04.2006, 17:58:42
 *
 * @author Thomas Morgner
 */
public class WMFImageFactoryModule extends AbstractWMFFactoryModule
{
  public WMFImageFactoryModule()
  {
  }

  public Resource create(final ResourceManager caller,
                         final ResourceData data,
                         final ResourceKey context)
          throws ResourceLoadingException
  {
    try
    {
      final long version = data.getVersion(caller);
      final WmfFile wmfFile = new WmfFile(data.getResourceAsStream(caller), -1, -1);
      final Image image = wmfFile.replay();
      return new SimpleResource (data.getKey(), image, version);
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("Failed to process WMF file", e);
    }
  }

}
