package org.jfree.report.ext.modules.java14imageio;

import java.awt.Image;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.jfree.report.resourceloader.ImageFactoryModule;

public class Java14ImageFactoryModule implements ImageFactoryModule
{
  public Java14ImageFactoryModule ()
  {
  }

  public Image createImage (final byte[] imageData,
                            final String fileName,
                            final String mimeType)
          throws IOException
  {
    if (fileName != null)
    {
      final Iterator it =
              ImageIO.getImageReadersBySuffix(extractFileSuffix(fileName));
      while (it.hasNext())
      {
        try
        {
          final ImageReader reader = (ImageReader) it.next();
          return reader.read(0);
        }
        catch(IOException ioe)
        {
          // ignore, try an other decoder ...
        }
      }
    }
    if (mimeType != null)
    {
      final Iterator it =
              ImageIO.getImageReadersByMIMEType(extractFileSuffix(mimeType));
      while (it.hasNext())
      {
        try
        {
          final ImageReader reader = (ImageReader) it.next();
          return reader.read(0);
        }
        catch(IOException ioe)
        {
          // ignore, try an other decoder ...
        }
      }
    }
    throw new IOException("No suitable image reader found for image '" +
            fileName + "' with MimeType '" + mimeType + "'");
  }

  public boolean canHandleResourceByContent (final byte[] content)
  {
    return false;
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

  public int getHeaderFingerprintSize ()
  {
    return 0;
  }
}
