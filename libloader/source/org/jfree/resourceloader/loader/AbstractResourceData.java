/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libloader/
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.jfree.io.IOUtils;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 05.04.2006, 15:24:47
 *
 * @author Thomas Morgner
 */
public abstract class AbstractResourceData implements ResourceData, Serializable
{
  protected AbstractResourceData()
  {
  }

  public byte[] getResource(ResourceManager caller)
      throws ResourceLoadingException
  {
    try
    {
      final InputStream in = getResourceAsStream(caller);
      if (in == null)
      {
        throw new ResourceLoadingException("Unable to read Stream: No input stream: " + getKey());
      }
      final ByteArrayOutputStream bout = new ByteArrayOutputStream();
      IOUtils.getInstance().copyStreams(in, bout);
      in.close();
      return bout.toByteArray();
    }
    catch (ResourceLoadingException rle)
    {
      throw rle;
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("Unable to read Stream: ", e);
    }
  }

  public int getResource(final ResourceManager caller,
                         final byte[] target,
                         int offset,
                         int length) throws ResourceLoadingException
  {
    try
    {
      if (target == null)
      {
        throw new NullPointerException();
      }
      if (target.length < (offset + length))
      {
        throw new IndexOutOfBoundsException();
      }

      final InputStream in = getResourceAsStream(caller);
      if (in == null)
      {
        throw new ResourceLoadingException("Unable to read Stream: No input stream: " + getKey());
      }

      if (offset > 0)
      {
        long toBeSkipped = offset;
        long skipResult = in.skip(toBeSkipped);
        toBeSkipped -= skipResult;
        while (skipResult > 0 && toBeSkipped > 0)
        {
          skipResult = in.skip(offset);
          toBeSkipped -= skipResult;
        }

        if (toBeSkipped > 0)
        {
          // failed to read up to the offset ..
          throw new ResourceLoadingException
              ("Unable to read Stream: Skipping content failed: " + getKey());
        }
      }

      int bytesToRead = length;
      // the input stream does not supply accurate available() data
      // the zip entry does not know the size of the data
      int bytesRead = in.read(target, length - bytesToRead, bytesToRead);
      while (bytesRead > -1 && bytesToRead > 0)
      {
        bytesToRead -= bytesRead;
        bytesRead = in.read(target, length - bytesToRead, bytesToRead);
      }

      in.close();
      return length - bytesRead;
    }
    catch (ResourceLoadingException rle)
    {
      throw rle;
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("Unable to read Stream: ", e);
    }
  }
}
