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
 * WmfImageFactoryModule.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
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
