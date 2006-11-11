/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * Java14ImageFactoryModule.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.report.ext.modules.factories.imageio;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;

import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.SimpleResource;
import org.jfree.resourceloader.factory.FactoryModule;

public class ImageIOFactoryModule implements FactoryModule
{
  public ImageIOFactoryModule ()
  {
  }

  private String extractFileSuffix (final String name)
  {
    final int lastDot = name.lastIndexOf('.');
    if (lastDot == -1)
    {
      return name;
    }
    return name.substring(lastDot + 1);
  }

  public boolean canHandleResourceByName (final String name)
  {
    final Iterator it =
            ImageIO.getImageReadersBySuffix(extractFileSuffix(name));
    return it.hasNext();
  }

  public boolean canHandleResourceByMimeType (final String name)
  {
    final Iterator it =
            ImageIO.getImageReadersByMIMEType(extractFileSuffix(name));
    return it.hasNext();
  }

  public int canHandleResource(final ResourceManager caller,
                               final ResourceData data)
          throws ResourceCreationException, ResourceLoadingException
  {
    try
    {
      final String mimeType = (String)
              data.getAttribute(ResourceData.CONTENT_TYPE);
      if (mimeType != null && canHandleResourceByMimeType(mimeType))
      {
        return RECOGNIZED_CONTENTTYPE;
      }

      final String fileName = (String)
              data.getAttribute(ResourceData.FILENAME);
      if (fileName != null && canHandleResourceByName(fileName))
      {
        return RECOGNIZED_CONTENTTYPE;
      }
      return REJECTED;
    }
    catch (Exception e)
    {
      throw new ResourceCreationException("Failed to load or check content", e);
    }
  }

  public Resource create(final ResourceManager caller,
                         final ResourceData data,
                         final ResourceKey context)
      throws ResourceCreationException, ResourceLoadingException
  {
    try
    {
      final long version = data.getVersion(caller);
      final BufferedImage image =
          ImageIO.read(data.getResourceAsStream(caller));
      return new SimpleResource (data.getKey(), image, version);
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("IOError while loading the image.");
    }
  }

  public int getHeaderFingerprintSize ()
  {
    return 0;
  }
}
