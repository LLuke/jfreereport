package org.jfree.report.resourceloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.jfree.io.IOUtils;
import org.jfree.report.util.Log;
import org.jfree.ui.Drawable;
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
