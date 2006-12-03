/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.fonts.io;

import java.io.IOException;

import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceLoadingException;

/**
 * The current use of iText and its inherent dependence on the font filename
 * makes it not feasible to use libLoader for the font loading right now.
 *
 * @author Thomas Morgner
 */
public class ResourceFontDataInputSource implements FontDataInputSource
{
  private transient byte[] rawData;
  private ResourceManager loader;
  private ResourceKey source;

  public ResourceFontDataInputSource(final ResourceManager loader,
                                     final ResourceKey source)
  {
    if (loader == null)
    {
      throw new NullPointerException();
    }
    if (source == null)
    {
      throw new NullPointerException();
    }
    this.loader = loader;
    this.source = source;
  }

  public void readFullyAt(long position, byte[] buffer, int offset, int length)
          throws IOException
  {
    if (rawData == null)
    {
      try
      {
        final ResourceData data = loader.load(source);
        rawData = data.getResource(loader);
      }
      catch (ResourceLoadingException e)
      {
        throw new IOException("Failed to load the raw data.");
      }
    }

    final int iPos = (int) (position & 0x7fffffff);
    System.arraycopy(rawData, iPos, buffer, offset, length);
  }

  public void dispose()
  {
    rawData = null;
  }

  public String getFileName()
  {
    return source.toExternalForm();
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final ResourceFontDataInputSource that = (ResourceFontDataInputSource) o;

    if (!loader.equals(that.loader))
    {
      return false;
    }
    if (!source.equals(that.source))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = loader.hashCode();
    result = 29 * result + source.hashCode();
    return result;
  }
}
