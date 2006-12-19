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
 * $Id: FileResourceKey.java,v 1.3 2006/12/03 16:41:16 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.loader.file;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.net.URL;
import java.net.MalformedURLException;

import org.jfree.resourceloader.AbstractResourceKey;

/**
 * Creation-Date: 05.04.2006, 14:27:31
 *
 * @author Thomas Morgner
 */
public class FileResourceKey extends AbstractResourceKey
{
  private transient File file;

  public FileResourceKey(final Map parameters)
  {
    super(parameters);
    Object maybeFile = parameters.get(AbstractResourceKey.CONTENT_KEY);
    if (maybeFile instanceof File == false)
    {
      throw new NullPointerException("File is not valid.");
    }
    this.file = (File) maybeFile;
  }

  /**
   * Returns the schema of this resource key. The schema can be mapped to a
   * known resource loader. If no resource loader is available for the given
   * schema, the resource will be unavailable.
   *
   * @return
   */
  public String getSchema()
  {
    return "file";
  }

  public File getFile()
  {
    return file;
  }

  /**
   * Creates a unique identifier for this key.
   * <p/>
   * The following statement must be true for all external forms generated by
   * this method: (key1.equals(key2) == key1.toExternalForm().equals(key2.toExternalForm())
   *
   * @return
   */
  public String toExternalForm()
  {
    try
    {
      return getFile().getCanonicalPath();
    }
    catch (IOException e)
    {
      return getFile().getAbsolutePath();
    }
  }

  public String toString()
  {
    return "FileResourceKey={file=" + toExternalForm() + "}";
  }

  /**
   * Tries to build an URL. This is a compatiblity method for supporting other
   * resource loader frameworks. The method may return null, if there is no URL
   * representation for the given resource-key.
   *
   * @return the URL or null.
   */
  public URL toURL()
  {
    try
    {
      return getFile().toURL();
    }
    catch (MalformedURLException e)
    {
      return null;
    }
  }
}
