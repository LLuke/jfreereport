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
 * FileFontDataInputSource.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 15.12.2005 : Initial version
 */
package org.jfree.fonts.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Creation-Date: 15.12.2005, 15:55:56
 *
 * @author Thomas Morgner
 */
public class FileFontDataInputSource implements FontDataInputSource
{
  private File file;
  private RandomAccessFile fileReader;
  private long lastPosition;

  public FileFontDataInputSource(final File file) throws IOException
  {
    this.file = file;
    this.fileReader = new RandomAccessFile(file, "r");
  }

  public synchronized void readFullyAt
          (long position, byte[] buffer, int offset, int length)
          throws IOException
  {
    if (fileReader == null)
    {
      this.fileReader = new RandomAccessFile(file, "r");
      fileReader.seek(position);
    }
    else if (position != lastPosition)
    {
      fileReader.seek(position);
    }
    fileReader.readFully(buffer, offset, length);
    lastPosition = position + length;
  }

  public synchronized void dispose()
  {
    if (this.fileReader == null)
    {
      return;
    }
    try
    {
      this.fileReader.close();
    }
    catch (IOException e)
    {
      // we can safely ignore that one.
    }
    this.fileReader = null;
  }

  public File getFile()
  {
    return file;
  }

  public String getFileName()
  {
    return file.getPath();
  }
}
