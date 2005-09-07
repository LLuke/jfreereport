/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * DrawableFactory.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DrawableFactory.java,v 1.5 2005/08/08 15:36:37 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.resourceloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.jfree.io.IOUtils;
import org.jfree.ui.Drawable;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

public class DrawableFactory
{
  private static DrawableFactory singleton;
  private ArrayList factoryModules;

  public static synchronized DrawableFactory getInstance ()
  {
    if (singleton == null)
    {
      singleton = new DrawableFactory();
    }
    return singleton;
  }

  private DrawableFactory ()
  {
    factoryModules = new ArrayList();
  }

  public boolean registerModule (final String className)
  {
    try
    {
      final Class c = ObjectUtilities.getClassLoader
              (getClass()).loadClass(className);
      registerModule((ImageFactoryModule) c.newInstance());
      return true;
    }
    catch (Exception e)
    {
      return false;
    }
  }

  public synchronized void registerModule (final ImageFactoryModule module)
  {
    if (factoryModules.contains(module) == false)
    {
      factoryModules.add(module);
    }
  }

  public Drawable createDrawable (final URL url)
          throws IOException
  {
    final InputStream in = url.openStream();
    final URLConnection uc = url.openConnection();
    final Drawable image = createDrawable(uc.getInputStream(),
            url.getFile(), uc.getContentType());
    in.close();
    return image;
  }

  public Drawable createDrawable (final InputStream in,
                                  final String file,
                                  final String contentType)
          throws IOException
  {
    final ByteArrayOutputStream bout = new ByteArrayOutputStream(32 * 1024);
    IOUtils.getInstance().copyStreams(in, bout, 16 * 1024);
    return createDrawable(bout.toByteArray(), file, contentType);
  }

  public synchronized Drawable createDrawable (final byte[] data,
                                               final String fileName,
                                               final String mimeType)
          throws IOException
  {
    // first pass: Search by content
    // this is the safest method to identify the image data
    // as names might be invalid and mimetypes might be forged ..
    for (int i = 0; i < factoryModules.size(); i++)
    {
      try
      {
        final DrawableFactoryModule module = (DrawableFactoryModule) factoryModules.get(i);
        if (module.getHeaderFingerprintSize() > 0 &&
                data.length >= module.getHeaderFingerprintSize())
        {
          if (module.canHandleResourceByContent(data))
          {
            return module.createDrawable(data, fileName, mimeType);
          }
        }
      }
      catch (IOException ioe)
      {
        // first try failed ..
        Log.info("Failed to load image: Trying harder ..", ioe);
      }
    }

    // second pass: Search by mime type
    // this is the second safest method to identify the image data
    // as names might be invalid and mimetypes might be forged ..
    if (mimeType != null && "".equals(mimeType) == false)
    {
      for (int i = 0; i < factoryModules.size(); i++)
      {
        try
        {
          final DrawableFactoryModule module = (DrawableFactoryModule) factoryModules.get(i);
          if (module.canHandleResourceByMimeType(mimeType))
          {
            return module.createDrawable(data, fileName, mimeType);
          }
        }
        catch (IOException ioe)
        {
          // first try failed ..
          Log.info("Failed to load image: Trying harder ..", ioe);
        }
      }
    }

    // third pass: Search by mime type
    // this is the final method to identify the image data
    // as names might be invalid and mimetypes might be forged ..
    if (mimeType != null && "".equals(mimeType) == false)
    {
      for (int i = 0; i < factoryModules.size(); i++)
      {
        try
        {
          final DrawableFactoryModule module = (DrawableFactoryModule) factoryModules.get(i);
          if (module.canHandleResourceByName(fileName))
          {
            return module.createDrawable(data, fileName, mimeType);
          }
        }
        catch (IOException ioe)
        {
          // first try failed ..
          Log.info("Failed to load image: Trying harder ..", ioe);
        }
      }
    }
    throw new IOException("Unable to load the drawable, no suitable loader found.");
  }
}
