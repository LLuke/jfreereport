package org.jfree.report.resourceloader;

import java.util.ArrayList;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

import org.jfree.util.ObjectUtilities;
import org.jfree.io.IOUtils;
import org.jfree.report.util.Log;

public class ImageFactory
{
  private static ImageFactory singleton;
  private ArrayList factoryModules;

  public static synchronized ImageFactory getInstance ()
  {
    if (singleton == null)
    {
      singleton = new ImageFactory();
    }
    return singleton;
  }

  private ImageFactory ()
  {
    factoryModules = new ArrayList();
  }

  public boolean registerModule (final String className)
  {
    try
    {
      final Class c = ObjectUtilities.getClassLoader
              (getClass()).loadClass(className);
      registerModule ((ImageFactoryModule) c.newInstance());
      return true;
    }
    catch(Exception e)
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

  public Image createImage (final URL url)
          throws IOException
  {
    final InputStream in = url.openStream();
    final Image image = createImage(in);
    in.close();
    return image;
  }

  public Image createImage (final InputStream in) throws IOException
  {
    final ByteArrayOutputStream bout = new ByteArrayOutputStream(32*1024);
    IOUtils.getInstance().copyStreams(in, bout, 16*1024);
    return createImage(bout.toByteArray());
  }

  public synchronized Image createImage (final byte[] data) throws IOException
  {
    for (int i = 0; i < factoryModules.size(); i++)
    {
      try
      {
        final ImageFactoryModule module = (ImageFactoryModule) factoryModules.get(i);
        if (data.length >= module.getHeaderFingerprintSize())
        {
          if (module.canHandleResourceByContent(data))
          {
            return module.createImage(data);
          }
        }
      }
      catch(IOException ioe)
      {
        // first try failed ..
        Log.info ("Failed to load image: Trying harder ..", ioe);
      }
    }

    // default failback ..
    // the JDK implementation might be able to handle some more modules
    return Toolkit.getDefaultToolkit().createImage(data);
  }
}
