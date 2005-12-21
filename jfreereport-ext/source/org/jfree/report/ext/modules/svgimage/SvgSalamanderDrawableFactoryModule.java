/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * SvgSalamanderDrawableFactoryModule.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 21.12.2005 : Initial version
 */
package org.jfree.report.ext.modules.svgimage;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URI;

import org.jfree.report.resourceloader.DrawableFactoryModule;
import org.jfree.report.resourceloader.ImageFactoryModule;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.Drawable;
import org.jfree.util.Log;
import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;

/**
 * Creation-Date: 21.12.2005, 20:08:51
 *
 * @author Thomas Morgner
 */
public class SvgSalamanderDrawableFactoryModule
        implements ImageFactoryModule, DrawableFactoryModule
{
  private static final String[] MIMETYPES = new String[]{
      "image/svg-xml", "image/svg+xml"
  };

  private SVGUniverse svgUniverse;

  public SvgSalamanderDrawableFactoryModule()
  {
    svgUniverse = SVGCache.getSVGUniverse();
  }

  public boolean canHandleResourceByMimeType(String name)
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

  public boolean canHandleResourceByName(String name)
  {
    return StringUtil.endsWithIgnoreCase(name, ".svg");
  }

  public boolean canHandleResourceByContent(byte[] content)
  {
    return false;
  }

  public int getHeaderFingerprintSize()
  {
    return 0;
  }

  public Image createImage(byte[] imageData, String fileName, String mimeType)
          throws IOException
  {
    ByteArrayInputStream bin = new ByteArrayInputStream(imageData);
    URI uri = svgUniverse.loadSVG(bin, fileName);
    SVGDiagram diagram = svgUniverse.getDiagram(uri);
    int height = (int) diagram.getHeight();
    int width = (int) diagram.getWidth();
    Image image = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D imageGraphics2D = (Graphics2D) image.getGraphics();
    try
    {
      diagram.render(imageGraphics2D);
    }
    catch (SVGException e)
    {
      // failed, log, then ignore
      Log.warn ("Failed to render SVG image " + fileName, e);
    }
    imageGraphics2D.dispose();
    bin.close();
    return image;
  }

  public Drawable createDrawable(final byte[] imageData,
                                 final String fileName,
                                 final String mimeType) throws IOException
  {
    ByteArrayInputStream bin = new ByteArrayInputStream(imageData);
    URI uri = svgUniverse.loadSVG(bin, fileName);
    bin.close();
    return new SvgSalamanderDrawable(svgUniverse, uri);
  }

}
