package org.jfree.report.resourceloader;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import org.jfree.report.util.StringUtil;

public class JPEGImageFactoryModule implements ImageFactoryModule
{
  private static final byte JFIF_ID[] = {0x4A, 0x46, 0x49, 0x46, 0x00};
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

  public JPEGImageFactoryModule ()
  {
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

  public boolean canHandleResourceByContent (final byte[] content)
  {
    if (content[0] != 0xFF && content[1] != 0xD8)
    {
      return false;
    }
    for (int i = 0; i < JFIF_ID.length; i++)
    {
      if (JFIF_ID[i] != content[6 + i])
      {
        return false;
      }
    }
    return true;
  }

  public boolean canHandleResourceByName (final String name)
  {
    return (StringUtil.endsWithIgnoreCase(name, ".jpg") ||
            StringUtil.endsWithIgnoreCase(name, ".jpeg"));
  }

  public int getHeaderFingerprintSize ()
  {
    return 11;
  }

  public Image createImage (final byte[] imageData,
                            final String fileName,
                            final String mimeType)
          throws IOException
  {
    return Toolkit.getDefaultToolkit().createImage(imageData);
  }
}
