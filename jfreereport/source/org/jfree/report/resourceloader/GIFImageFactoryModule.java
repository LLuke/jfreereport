package org.jfree.report.resourceloader;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import org.jfree.report.util.StringUtil;

public class GIFImageFactoryModule implements ImageFactoryModule
{
  private static final byte GIF_ID[] = {'G', 'I', 'F', '8'};

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

  public Image createImage (final byte[] imageData)
          throws IOException
  {
    return Toolkit.getDefaultToolkit().createImage(imageData);
  }

  public int getHeaderFingerprintSize ()
  {
    return GIF_ID.length;
  }
}
