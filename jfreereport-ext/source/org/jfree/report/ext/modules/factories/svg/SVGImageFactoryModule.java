/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * SVGImageFactoryModule.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.report.ext.modules.factories.svg;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;

import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import org.jfree.util.Log;
import org.jfree.ui.Drawable;
import org.jfree.report.ext.modules.svgimage.SvgSalamanderDrawable;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.SimpleResource;

/**
 * Creation-Date: 21.12.2005, 20:08:51
 *
 * @author Thomas Morgner
 */
public class SVGImageFactoryModule extends AbstractSVGFactoryModule
{
  private SVGUniverse svgUniverse;

  public SVGImageFactoryModule()
  {
    svgUniverse = SVGCache.getSVGUniverse();
  }

  private Image createImage(final InputStream imageData, final String fileName)
  {
    URI uri = svgUniverse.loadSVG(imageData, fileName);
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
    return image;
  }

  public Resource create(final ResourceManager caller,
                         final ResourceData data,
                         final ResourceKey context)
      throws ResourceCreationException, ResourceLoadingException
  {
    final long version = data.getVersion(caller);
    final String fileName = (String)
            data.getAttribute(ResourceData.FILENAME);
    final InputStream stream = data.getResourceAsStream(caller);
    final Image image = createImage(stream, fileName);
    try
    {
      stream.close();
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("Unable to load the content.");
    }

    return new SimpleResource (data.getKey(), image, version);
  }
}
