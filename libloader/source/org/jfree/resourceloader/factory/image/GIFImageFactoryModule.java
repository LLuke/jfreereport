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

import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.SimpleResource;
import org.jfree.resourceloader.factory.AbstractFactoryModule;

/**
 * Creation-Date: 05.04.2006, 17:35:12
 *
 * @author Thomas Morgner
 */
public class GIFImageFactoryModule extends AbstractFactoryModule
{
  private static final int FINGERPRINT[] = {'G', 'I', 'F', '8'};
  private static final String[] MIMETYPES =
          {
                  "image/gif",
                  "image/x-xbitmap",
                  "image/gi_"
          };

  private static final String[] FILEEXTENSIONS =
          {
            ".gif"
          };

  public GIFImageFactoryModule()
  {
  }

  public int getHeaderFingerprintSize ()
  {
    return GIFImageFactoryModule.FINGERPRINT.length;
  }

  protected int[] getFingerPrint()
  {
    return GIFImageFactoryModule.FINGERPRINT;
  }

  protected String[] getMimeTypes()
  {
    return GIFImageFactoryModule.MIMETYPES;
  }

  protected String[] getFileExtensions()
  {
    return GIFImageFactoryModule.FILEEXTENSIONS;
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
