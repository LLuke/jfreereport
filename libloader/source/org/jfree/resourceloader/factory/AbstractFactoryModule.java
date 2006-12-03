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
package org.jfree.resourceloader.factory;

import java.io.IOException;
import java.io.InputStream;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 05.04.2006, 17:44:42
 *
 * @author Thomas Morgner
 */
public abstract class AbstractFactoryModule implements FactoryModule
{
  protected AbstractFactoryModule()
  {
  }

  protected abstract int[] getFingerPrint();

  protected abstract String[] getMimeTypes();

  protected abstract String[] getFileExtensions();

  public int canHandleResource(final ResourceManager caller,
                               final ResourceData data)
          throws ResourceCreationException, ResourceLoadingException
  {
    try
    {
      if (canHandleResourceByContent(data.getResourceAsStream(caller)))
      {
        return RECOGNIZED_FINGERPRINT;
      }
      final String mimeType = (String)
              data.getAttribute(ResourceData.CONTENT_TYPE);
      if (mimeType != null && canHandleResourceByMimeType(mimeType))
      {
        return RECOGNIZED_CONTENTTYPE;
      }

      final String fileName = (String)
              data.getAttribute(ResourceData.FILENAME);
      if (fileName != null && canHandleResourceByName(fileName))
      {
        return RECOGNIZED_CONTENTTYPE;
      }
      return REJECTED;
    }
    catch (ResourceLoadingException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new ResourceCreationException("Failed to load or check content", e);
    }
  }

  protected boolean canHandleResourceByContent(final InputStream data)
          throws IOException
  {
    final int[] fingerprint = getFingerPrint();
    if (fingerprint.length == 0)
    {
      return false;
    }
    for (int i = 0; i < fingerprint.length; i++)
    {
      if (fingerprint[i] != data.read())
      {
        return false;
      }
    }
    return true;
  }

  protected boolean canHandleResourceByMimeType(final String name)
  {
    final String[] mimes = getMimeTypes();
    for (int i = 0; i < mimes.length; i++)
    {
      if (name.equals(mimes[i]))
      {
        return true;
      }
    }
    return false;
  }

  protected boolean canHandleResourceByName(final String name)
  {
    final String[] fexts = getFileExtensions();
    for (int i = 0; i < fexts.length; i++)
    {
      if (name.equalsIgnoreCase(fexts[i]))
      {
        return true;
      }
    }
    return false;
  }

}
