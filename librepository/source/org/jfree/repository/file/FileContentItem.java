/**
 * ===========================================================
 * LibRepository : a free Java content repository access layer
 * ===========================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/librepository/
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
 * FileContentItem.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentItem;
import org.jfree.repository.ContentLocation;

/**
 * Creation-Date: 13.11.2006, 12:17:21
 *
 * @author Thomas Morgner
 */
public class FileContentItem extends FileContentEntity implements ContentItem
{
  public FileContentItem(final ContentLocation parent, final File backend)
  {
    super(parent, backend);
  }

  public String getMimeType() throws ContentIOException
  {
    final FileRepository fileRepository = (FileRepository) getRepository();
    return fileRepository.getMimeRegistry().getMimeType(this);
  }

  public OutputStream getOutputStream() throws ContentIOException, IOException
  {
    return new FileOutputStream (getBackend());
  }

  public InputStream getInputStream()
      throws ContentIOException, IOException
  {
    return new FileInputStream (getBackend());
  }

  public boolean isReadable()
  {
    return getBackend().canRead();
  }

  public boolean isWriteable()
  {
    return getBackend().canWrite();
  }
}
