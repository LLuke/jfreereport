package org.jfree.report.resourceloader;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import org.jfree.report.util.StringUtil;

public class GIFImageFactoryModule implements ImageFactoryModule
{
  private static final byte GIF_ID[] = {(byte) 'G', (byte) 'I', (byte) 'F', (byte) '8'};

  private static final String[] MIMETYPES = {
    "image/gif",
    "image/x-xbitmap",
    "image/gi_"
  };

  public GIFImageFactoryModule ()
  {
  }

  public boolean canHandleResourceByContent (final byte[] content)
  {
    for (int i = 0; i < GIF_ID.length; i++)
    {
      if (GIF_ID[i] != content[i])
      {
        return false;
      }
    }
    return true;
  }

  public boolean canHandleResourceByName (final String name)
  {
    return (StringUtil.endsWithIgnoreCase(name, ".gif"));
  }

  public Image createImage (final byte[] imageData,
                            final String fileName,
                            final String mimeType)
          throws IOException
  {
    return Toolkit.getDefaultToolkit().createImage(imageData);
  }

  public boolean canHandleResourceByMimeType (final String name)
  {
    for (int i = 0; i < MIMETYPES.length; i++)
    {
      if (name.equals(MIMETYPES[i]))
      {
        return true;
      }
    }
    return false;
  }

  public int getHeaderFingerprintSize ()
  {
    return GIF_ID.length;
  }
}
