package org.jfree.report.resourceloader;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jfree.pixie.wmf.WmfFile;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.Drawable;

public class WmfImageFactoryModule implements ImageFactoryModule,
                                              DrawableFactoryModule
{
  private static final String[] MIMETYPES =
          {
            "application/x-msmetafile",
            "application/wmf",
            "application/x-wmf",
            "image/wmf",
            "image/x-wmf",
            "image/x-win-metafile",
            "zz-application/zz-winassoc-wmf"
          };

  public WmfImageFactoryModule ()
  {
  }

  public boolean canHandleResourceByContent (final byte[] content)
  {
    return (content[0] == 0xD7 && content[1] == 0xCD);
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

  public boolean canHandleResourceByName (final String name)
  {
    return StringUtil.endsWithIgnoreCase(name, ".wmf");
  }

  public int getHeaderFingerprintSize ()
  {
    return 2;
  }

  public Image createImage (final byte[] imageData,
                            final String fileName,
                            final String mimeType)
          throws IOException
  {
    final WmfFile wmfFile =
            new WmfFile(new ByteArrayInputStream(imageData), -1, -1);
    return wmfFile.replay();
  }

  public Drawable createDrawable (final byte[] imageData,
                                  final String fileName,
                                  final String mimeType)
          throws IOException
  {
    final WmfFile wmfFile =
            new WmfFile(new ByteArrayInputStream(imageData), -1, -1);
    return wmfFile;
  }
}
