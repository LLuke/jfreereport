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
 * $Id: FileFontDataInputSource.java,v 1.5 2006/12/03 18:11:59 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.fonts.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.EOFException;

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
          (final long position, final byte[] buffer, final int offset, final int length)
          throws IOException
  {
    if (fileReader == null)
    {
      this.fileReader = new RandomAccessFile(file, "r");
      if (position > fileReader.length())
      {
        throw new EOFException ("Given position is beyond the end of the file.");
      }

      fileReader.seek(position);
    }
    else
    {
      if (position > fileReader.length())
      {
        throw new EOFException ("Given position is beyond the end of the file.");
      }

      if (position != lastPosition)
      {
        fileReader.seek(position);
      }
    }

    try
    {
      fileReader.readFully(buffer, offset, length);
      lastPosition = position + length;
    }
    catch(IOException ioe)
    {
      lastPosition = -1;
      throw ioe;
    }
  }

  public int readAt(final long position,
                    final byte[] buffer,
                    final int offset,
                    final int length) throws IOException
  {
    if (fileReader == null)
    {
      this.fileReader = new RandomAccessFile(file, "r");
      if (position > fileReader.length())
      {
        return 0;
      }

      fileReader.seek(position);
    }
    else
    {
      if (position > fileReader.length())
      {
        return 0;
      }

      if (position != lastPosition)
      {
        fileReader.seek(position);
      }
    }

    try
    {
      final int readLength = (int) Math.min (length, fileReader.length() - position);
      fileReader.readFully(buffer, offset, readLength);
      lastPosition = position + readLength;
      return readLength;
    }
    catch(IOException ioe)
    {
      lastPosition = -1;
      throw ioe;
    }
  }

  public synchronized int readAt(final long position) throws IOException
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
    final int retval = fileReader.read();
    if (retval == -1)
    {
      return -1;
    }
    lastPosition = position + 1;
    return retval;
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

    final FileFontDataInputSource that = (FileFontDataInputSource) o;

    if (!file.equals(that.file))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return file.hashCode();
  }
}
