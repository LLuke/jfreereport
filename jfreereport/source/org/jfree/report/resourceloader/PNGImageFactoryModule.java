package org.jfree.report.resourceloader;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import org.jfree.report.util.StringUtil;

public class PNGImageFactoryModule implements ImageFactoryModule
{
  public static final int[] PNGID = {137, 80, 78, 71, 13, 10, 26, 10};

  public PNGImageFactoryModule ()
  {
  }

  public boolean canHandleResourceByContent (final byte[] content)
  {
    for (int i = 0; i < PNGID.length; i++)
    {
      if (PNGID[i] != content[i])
      {
        return false;
      }
    }
    return true;
  }

  public boolean canHandleResourceByName (final String name)
  {
    return (StringUtil.endsWithIgnoreCase(name, ".png"));
  }

  public Image createImage (final byte[] imageData)
          throws IOException
  {
    return Toolkit.getDefaultToolkit().createImage(imageData);
  }

  public int getHeaderFingerprintSize ()
  {
    return PNGID.length;
  }
}
