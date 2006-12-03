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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.factory.image;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.InputStream;
import java.io.IOException;

import org.jfree.resourceloader.factory.AbstractFactoryModule;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.SimpleResource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 05.04.2006, 17:35:12
 *
 * @author Thomas Morgner
 */
public class JPEGImageFactoryModule extends AbstractFactoryModule
{
  private static final int FINGERPRINT_1[] = {0xFF, 0xD8, 0xFF, 0xE0};
  private static final int FINGERPRINT_2[] = {0x4A, 0x46, 0x49, 0x46, 0x00};

  private static final String[] MIMETYPES =
          {
            "image/jpeg",
            "image/jpg",
            "image/jp_",
            "application/jpg",
            "application/x-jpg",
            "image/pjpeg",
            "image/pipeg",
            "image/vnd.swiftview-jpeg",
            "image/x-xbitmap"
          };

  private static final String[] FILEEXTENSIONS =
          {
            ".jpg", ".jpeg"
          };

  public JPEGImageFactoryModule()
  {
  }

  public int getHeaderFingerprintSize ()
  {
    return -1;
  }

  protected boolean canHandleResourceByContent(final InputStream data)
          throws IOException
  {
    int[] fingerprint = FINGERPRINT_1;
    for (int i = 0; i < fingerprint.length; i++)
    {
      if (fingerprint[i] != data.read())
      {
        return false;
      }
    }

    if (data.read() == -1) return false;
    if (data.read() == -1) return false;

    fingerprint = FINGERPRINT_2;
    for (int i = 0; i < fingerprint.length; i++)
    {
      if (fingerprint[i] != data.read())
      {
        return false;
      }
    }
    return true;
  }

  protected int[] getFingerPrint()
  {
    return new int[0];
  }

  protected String[] getMimeTypes()
  {
    return JPEGImageFactoryModule.MIMETYPES;
  }

  protected String[] getFileExtensions()
  {
    return JPEGImageFactoryModule.FILEEXTENSIONS;
  }

  public Resource create(final ResourceManager caller,
                         final ResourceData data,
                         final ResourceKey context)
          throws ResourceLoadingException
  {
    final long version = data.getVersion(caller);
    final Image image =
            Toolkit.getDefaultToolkit().createImage(data.getResource(caller));
    return new SimpleResource (data.getKey(), image, version);
  }
}
