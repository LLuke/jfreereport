/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * Buffer.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  David R. Harris
 * Contributor(s):   Thomas Morgner
 *
 * $Id: Buffer.java,v 1.1 2003/02/25 20:58:07 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A block of raw mmeory. This is used to store various metafile
 * objects as they are read in from file.
 */
public class Buffer
{
  /** The memory itself. */
  protected byte bytes[] = null;

  /** The current length of the memory. */
  private int length;

  /**
   * Default Constructor. Defines a buffer without an initial size.
   */
  protected Buffer ()
  {
  }

  /**
   * Defines a new buffer with the given initial size in bytes.
   *
   * @param length the length of the buffer in bytes.
   */
  protected Buffer (int length)
  {
    setCapacity(length);
  }

  /**
   * The size of the stored data in the memory.
   */
  public final int getLength ()
  {
    return length;
  }

  /**
   * Extends the length to the given new size.
   *
   * @param len the new length.
   * @throws IllegalArgumentException if the length is shorter than the used
   * storage in memory.
   */
  protected void setLength (int len)
  {
    if (len > bytes.length)
      throw new IllegalArgumentException();

    this.length = len;
  }

  /**
   * Ensures that the buffer has enough space for the given number of bytes.
   *
   * @param capacity the new capacity that should be ensured.
   * @throws IllegalArgumentException if the capacity is smaller than the buffers length.
   */
  protected void setCapacity (int capacity)
  {
    if (capacity < getLength())
      throw new IllegalArgumentException();

    if (bytes == null || bytes.length == 0)
    {
      bytes = new byte[capacity];
    }
    else if (capacity != bytes.length)
    {
      byte[] old = bytes;
      bytes = new byte[capacity];
      System.arraycopy (old, 0, bytes, 0, Math.min (old.length, capacity));
    }
  }

  /**
   * Read <code>len</code> bytes into the memory from a stream and stores
   * the read bytes at the given offset.
   *
   * @param in the input stream that should be used
   * @param offset the offset
   * @param len the number bytes that should be read.
   */
  public void read (InputStream in, int offset, int len)
          throws IOException
  {
    // make sure, that all bytes can be read and create the buffer if needed.
    if (bytes == null || offset + len > bytes.length)
    {
      setCapacity (offset + len);
    }

    //in.readFully( bytes, offset, len );
    while (len > 0)
    {
      int blockSize = in.read (bytes, offset, len);
      if (blockSize <= 0)
      {
        throw new EOFException ();
      }
      offset += blockSize;
      len -= blockSize;
      setLength(offset);
    }
  }

  /**
   * Set the int value as big-endian.
   *
   * @param offset the offset where to set the int value.
   * @param value the integer value that should be set.
   */
  public void setInt (int offset, int value)
  {
    if (offset > (getLength() - 4))
      throw new IndexOutOfBoundsException();

    setShort (offset, value & 0x0ffff);
    setShort (offset+2, value >> 16);
  }

  /**
   * Return the 32-bit int at the given byte offset.
   *
   * @param offset the offset where the integer value is stored in the memory
   * @return the integer.
   */
  public int getInt (int offset)
  {
    if (offset > (getLength() - 4))
      throw new IndexOutOfBoundsException();

    return (getShort (offset) & 0x0ffff) | (getShort (offset + 2) << 16);
  }

  /**
   * Stores the given short as BigEndian value.
   *
   * @param offset the offset.
   * @param shortval the shortvalue.
   */
  public void setShort (int offset, int shortval)
  {
    if (offset > (getLength() - 2))
      throw new IndexOutOfBoundsException();

    bytes[offset] = (byte) (shortval & 0x0ff);
    bytes[offset + 1] = (byte) (shortval >> 8);
  }

  /**
   * Return the 16-bit int at the given byte offset.
   *
   * @param offset the offset from where to read the short.
   * @return the short.
   */
  public int getShort (int offset)
  {
    if (offset > (getLength() - 2))
      throw new IndexOutOfBoundsException();

    return (bytes[offset] & 0x0ff) | (bytes[offset + 1] << 8);
  }

  /**
   * Sets the byte at the given offset.
   *
   * @param offset the offset.
   * @param value the byte that should be set.
   */
  public void setByte (int offset, int value)
  {
    if (offset > (getLength() - 1))
      throw new IndexOutOfBoundsException();

    bytes[offset] = (byte) (value & 0x0ff);
  }

  /**
   * Return the 8-bit int at the given byte offset.
   *
   * @param offset the offset from where to read the byte
   * @return the byte read.
   */
  public int getByte (int offset)
  {
    if (offset > (getLength() - 1))
      throw new IndexOutOfBoundsException();

    return bytes[offset] & 0x0ff;
  }

  /**
   * Writes the given string as byte stream using the plattforms default
   * encoding.
   *
   * @param offset the offset, where to store the string.
   * @param str the string that should be stored in the Wmf.
   */
  public void setString (int offset, String str)
  {
    if ((offset + str.length()) > (getLength() - 1))
      throw new IndexOutOfBoundsException();
    
    byte[] b = str.getBytes();

    int len = getLength() - offset;

    for (int i = 0; i < len; i++)
    {
      bytes[offset + i] = b[offset];
    }
    if ((offset + len) < getLength())
    {
      bytes[offset + len] = 0;
    }
  }

  /**
   * Return the null-terminated string at the given byte offset with the
   * given maximum length.
   *
   * @param offset the offset where the string starts
   * @param len the maximum length of the string
   * @return the null-terminated string read.
   */
  public String getString (int offset, int len)
  {
    int i;
    for (i = 0; i < len; i++)
    {
      if (bytes[offset + i] == 0)
        break;
    }
    return new String (bytes, offset, i);
  }

  /**
   * Gets an input stream to read from the memory buffer.
   * @param offset the offse, from where to read.
   * @return the InputStream.
   */
  public InputStream getInputStream (int offset)
  {
    return new ByteArrayInputStream (bytes, offset, bytes.length - offset);
  }
}
