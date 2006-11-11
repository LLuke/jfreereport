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
 * SvgSalamanderDrawableFactoryModule.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.report.ext.modules.factories.svg;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGUniverse;
import org.jfree.report.ext.modules.svgimage.SvgSalamanderDrawable;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.SimpleResource;
import org.jfree.ui.Drawable;

/**
 * Creation-Date: 21.12.2005, 20:08:51
 *
 * @author Thomas Morgner
 */
public class SVGDrawableFactoryModule extends AbstractSVGFactoryModule
{
  private SVGUniverse svgUniverse;

  public SVGDrawableFactoryModule()
  {
    svgUniverse = SVGCache.getSVGUniverse();
  }

  private Drawable createDrawable(final InputStream imageData,
                                 final String fileName)
  {
    URI uri = svgUniverse.loadSVG(imageData, fileName);
    return new SvgSalamanderDrawable(svgUniverse, uri);
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
    final Drawable drawable = createDrawable(stream, fileName);
    try
    {
      stream.close();
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("Unable to load the content.");
    }

    return new SimpleResource (data.getKey(), drawable, version);
  }
}
