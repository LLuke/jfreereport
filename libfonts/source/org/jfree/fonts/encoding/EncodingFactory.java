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
 * EncodingFactory.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: EncodingFactory.java,v 1.1 2006/04/29 15:12:55 taqua Exp $
 *
 * Changes
 * -------------------------
 * 29.04.2006 : Initial version
 */
package org.jfree.fonts.encoding;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import org.jfree.resourceloader.ResourceFactory;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.SimpleResource;

/**
 * Creation-Date: 29.04.2006, 14:32:15
 *
 * @author Thomas Morgner
 */
public class EncodingFactory implements ResourceFactory
{
  public EncodingFactory()
  {
  }

  public Resource create(final ResourceManager manager,
                         final ResourceData data,
                         final ResourceKey context)
          throws ResourceCreationException, ResourceLoadingException
  {
    try
    {
      InputStream in = data.getResourceAsStream(manager);
      ObjectInputStream oin = new ObjectInputStream(in);
      final Object ob = oin.readObject();
      // yes, that will be more generic in the future ...
      if (ob instanceof External8BitEncodingData == false)
      {
        throw new ResourceCreationException("This is no 8Bit Encoding data");
      }
      final External8BitEncodingData encData = (External8BitEncodingData) ob;
      final External8BitEncodingCore encCore =
              new External8BitEncodingCore(encData);
      return new SimpleResource(data.getKey(), encCore, data.getVersion(manager));
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("Failed to load resource", e);
    }
    catch (ClassNotFoundException e)
    {
      throw new ResourceCreationException
              ("Missing class definition: Failed to create encoding.");
    }
  }

  public Class getFactoryType()
  {
    return Encoding.class;
  }

  public void initializeDefaults()
  {
    // do nothing -- yet ..
  }
}
