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
 * PNGImageFactoryModule.java
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
package org.jfree.resourceloader.factory.image;

import java.awt.Toolkit;
import java.awt.Image;

import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.SimpleResource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.factory.AbstractFactoryModule;

/**
 * Creation-Date: 05.04.2006, 17:35:12
 *
 * @author Thomas Morgner
 */
public class PNGImageFactoryModule extends AbstractFactoryModule
{
  private static final int[] FINGERPRINT = {137, 80, 78, 71, 13, 10, 26, 10};

  private static final String[] MIMETYPES =
          {
            "image/png",
            "application/png",
            "application/x-png"
          };

  private static final String[] FILEEXTENSIONS =
          {
            ".png",
          };

  public PNGImageFactoryModule()
  {
  }

  public int getHeaderFingerprintSize ()
  {
    return FINGERPRINT.length;
  }

  protected int[] getFingerPrint()
  {
    return FINGERPRINT;
  }

  protected String[] getMimeTypes()
  {
    return MIMETYPES;
  }

  protected String[] getFileExtensions()
  {
    return FILEEXTENSIONS;
  }

  public Resource create(ResourceData data, ResourceKey context)
          throws ResourceLoadingException
  {
    final long version = data.getVersion();
    final Image image =
            Toolkit.getDefaultToolkit().createImage(data.getResource());
    return new SimpleResource (data.getKey(), image, version);
  }
}
